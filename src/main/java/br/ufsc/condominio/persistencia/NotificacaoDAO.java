package br.ufsc.condominio.persistencia;

import br.ufsc.condominio.model.PacoteDeNotificacoes.Aviso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Notificacao;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.StatusChamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.em_andamento;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.nao_iniciado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.pronto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pela persistência de notificações e de seus subtipos
 * {@link Aviso} e {@link Chamado}, distribuídos entre as tabelas
 * {@code notificacao}, {@code aviso}, {@code aviso_destinatario} e {@code chamado}.
 */
public class NotificacaoDAO implements Dao<Notificacao> {

    // id_status conforme INSERTs do esquema-bd.sql
    private static final int STATUS_NAO_INICIADO = 1;
    private static final int STATUS_EM_ANDAMENTO = 2;
    private static final int STATUS_PRONTO       = 3;

    private final ConexaoBancoDados conexao;

    public NotificacaoDAO() {
        this.conexao = ConexaoBancoDados.getInstancia();
    }

    @Override
    public void salvar(Notificacao notificacao) throws SQLException {
        if (notificacao instanceof Aviso aviso) {
            salvarAviso(aviso);
        } else if (notificacao instanceof Chamado chamado) {
            salvarChamado(chamado);
        } else {
            salvarNotificacaoSimples(notificacao);
        }
    }

    // ------------------------------------------------------------------
    // Inserção da notificação base
    // ------------------------------------------------------------------

    /** Insere uma linha em {@code notificacao} e devolve o id gerado. */
    private int inserirNotificacao(Connection conn, Notificacao n, String tipo) throws SQLException {
        String sql = """
                INSERT INTO notificacao (mensagem, data, categoria, tipo, cpf_criador)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, n.getMensagem());
            stmt.setTimestamp(2, n.getData() != null ? new Timestamp(n.getData().getTime()) : null);
            stmt.setString(3, n.getCategoria() != null ? n.getCategoria().name() : null);
            stmt.setString(4, tipo);
            if (n instanceof Chamado c && c.getCpfCondomino() != null) {
                stmt.setString(5, c.getCpfCondomino());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                chaves.next();
                return chaves.getInt(1);
            }
        }
    }

    private void salvarNotificacaoSimples(Notificacao notificacao) throws SQLException {
        try (Connection conn = conexao.abrirConexao()) {
            int id = inserirNotificacao(conn, notificacao, "NOTIFICACAO");
            notificacao.setId(id);
        }
    }

    // ------------------------------------------------------------------
    // Avisos
    // ------------------------------------------------------------------

    private void salvarAviso(Aviso aviso) throws SQLException {
        Connection conn = null;
        try {
            conn = conexao.abrirConexao();
            conn.setAutoCommit(false);

            int idNotificacao = inserirNotificacao(conn, aviso, "AVISO");

            int idAviso;
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO aviso (id_notificacao) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idNotificacao);
                stmt.executeUpdate();
                try (ResultSet chaves = stmt.getGeneratedKeys()) {
                    chaves.next();
                    idAviso = chaves.getInt(1);
                }
            }
            aviso.setId(idAviso);

            // Destinatários explícitos; se for broadcast (lista vazia), materializa
            // uma linha por usuário cadastrado para que a leitura possa ser persistida.
            List<String> destinatarios = aviso.getDestinatarios().isEmpty()
                    ? listarCpfsUsuarios(conn)
                    : aviso.getDestinatarios();

            try (PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO aviso_destinatario (id_aviso, cpf_destinatario, visualizado)
                    VALUES (?, ?, ?)
                    """)) {
                for (String cpf : destinatarios) {
                    stmt.setInt(1, idAviso);
                    stmt.setString(2, cpf);
                    stmt.setBoolean(3, aviso.foiVistoPor(cpf));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            fechar(conn);
        }
    }

    private List<String> listarCpfsUsuarios(Connection conn) throws SQLException {
        List<String> cpfs = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT cpf FROM usuario");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) cpfs.add(rs.getString(1));
        }
        return cpfs;
    }

    /**
     * Registra um usuário recém-criado em todos os avisos existentes como NÃO lido,
     * para que ele passe a enxergá-los (inclusive os broadcast anteriores ao cadastro).
     */
    public void registrarUsuarioEmAvisos(String cpf) throws SQLException {
        String sql = """
                INSERT INTO aviso_destinatario (id_aviso, cpf_destinatario, visualizado)
                SELECT a.id_aviso, ?, FALSE
                FROM aviso a
                WHERE NOT EXISTS (
                    SELECT 1 FROM aviso_destinatario ad
                    WHERE ad.id_aviso = a.id_aviso AND ad.cpf_destinatario = ?
                )
                """;
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, cpf);
            stmt.executeUpdate();
        }
    }

    /** Persiste que o aviso foi visualizado pelo destinatário informado. */
    public void marcarAvisoVisualizado(int idAviso, String cpf) throws SQLException {
        String sql = """
                UPDATE aviso_destinatario SET visualizado = TRUE
                WHERE id_aviso = ? AND cpf_destinatario = ?
                """;
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAviso);
            stmt.setString(2, cpf);
            stmt.executeUpdate();
        }
    }

    public List<Aviso> listarAvisos() throws SQLException {
        String sql = """
                SELECT a.id_aviso, n.mensagem, n.data, n.categoria
                FROM aviso a JOIN notificacao n ON a.id_notificacao = n.id_notificacao
                ORDER BY a.id_aviso
                """;
        List<Aviso> lista = new ArrayList<>();
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapearAviso(conn, rs));
        }
        return lista;
    }

    private Aviso mapearAviso(Connection conn, ResultSet rs) throws SQLException {
        int idAviso = rs.getInt("id_aviso");
        Aviso aviso = new Aviso(rs.getString("mensagem"), lerData(rs), lerCategoria(rs),
                carregarDestinatarios(conn, idAviso));
        aviso.setId(idAviso);
        for (String cpf : carregarVistos(conn, idAviso)) aviso.marcarComoVisto(cpf);
        return aviso;
    }

    private List<String> carregarDestinatarios(Connection conn, int idAviso) throws SQLException {
        List<String> destinatarios = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT cpf_destinatario FROM aviso_destinatario WHERE id_aviso=?")) {
            stmt.setInt(1, idAviso);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) destinatarios.add(rs.getString(1));
            }
        }
        return destinatarios;
    }

    private List<String> carregarVistos(Connection conn, int idAviso) throws SQLException {
        List<String> vistos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT cpf_destinatario FROM aviso_destinatario WHERE id_aviso=? AND visualizado=TRUE")) {
            stmt.setInt(1, idAviso);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) vistos.add(rs.getString(1));
            }
        }
        return vistos;
    }

    // ------------------------------------------------------------------
    // Chamados
    // ------------------------------------------------------------------

    private void salvarChamado(Chamado chamado) throws SQLException {
        Connection conn = null;
        try {
            conn = conexao.abrirConexao();
            conn.setAutoCommit(false);

            int idNotificacao = inserirNotificacao(conn, chamado, "CHAMADO");

            int idChamado;
            try (PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO chamado (id_notificacao, id_status, cpf_condomino, visualizado_sindico)
                    VALUES (?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idNotificacao);
                stmt.setInt(2, statusParaId(chamado.getStatusChamado()));
                stmt.setString(3, chamado.getCpfCondomino());
                stmt.setBoolean(4, chamado.isVisualizadoPeloSindico());
                stmt.executeUpdate();
                try (ResultSet chaves = stmt.getGeneratedKeys()) {
                    chaves.next();
                    idChamado = chaves.getInt(1);
                }
            }
            chamado.setId(idChamado);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            fechar(conn);
        }
    }

    /** Persiste o status atual e o flag de visualização do chamado. */
    public void atualizarChamado(Chamado chamado) throws SQLException {
        String sql = "UPDATE chamado SET id_status=?, visualizado_sindico=? WHERE id_chamado=?";
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, statusParaId(chamado.getStatusChamado()));
            stmt.setBoolean(2, chamado.isVisualizadoPeloSindico());
            stmt.setInt(3, chamado.getId());
            stmt.executeUpdate();
        }
    }

    public List<Chamado> listarChamados() throws SQLException {
        String sql = """
                SELECT c.id_chamado, c.id_status, c.cpf_condomino, c.visualizado_sindico,
                       n.mensagem, n.data, n.categoria
                FROM chamado c JOIN notificacao n ON c.id_notificacao = n.id_notificacao
                ORDER BY c.id_chamado
                """;
        List<Chamado> lista = new ArrayList<>();
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapearChamado(rs));
        }
        return lista;
    }

    private Chamado mapearChamado(ResultSet rs) throws SQLException {
        Chamado chamado = new Chamado(rs.getString("mensagem"), lerData(rs), lerCategoria(rs),
                rs.getString("cpf_condomino"));
        chamado.setId(rs.getInt("id_chamado"));
        chamado.setStatusChamado(idParaStatus(rs.getInt("id_status")));
        chamado.setVisualizadoPeloSindico(rs.getBoolean("visualizado_sindico"));
        return chamado;
    }

    private int statusParaId(StatusChamado status) {
        if (status instanceof em_andamento) return STATUS_EM_ANDAMENTO;
        if (status instanceof pronto) return STATUS_PRONTO;
        return STATUS_NAO_INICIADO;
    }

    private StatusChamado idParaStatus(int id) {
        return switch (id) {
            case STATUS_EM_ANDAMENTO -> new em_andamento();
            case STATUS_PRONTO -> new pronto();
            default -> new nao_iniciado();
        };
    }

    // ------------------------------------------------------------------
    // Operações genéricas (notificacao base)
    // ------------------------------------------------------------------

    @Override
    public void atualizar(Notificacao notificacao) throws SQLException {
        String sql = "UPDATE notificacao SET mensagem=?, data=?, categoria=? WHERE id_notificacao=?";
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notificacao.getMensagem());
            stmt.setTimestamp(2, notificacao.getData() != null
                    ? new Timestamp(notificacao.getData().getTime()) : null);
            stmt.setString(3, notificacao.getCategoria() != null
                    ? notificacao.getCategoria().name() : null);
            stmt.setInt(4, notificacao.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean remover(int id) throws SQLException {
        String sql = "DELETE FROM notificacao WHERE id_notificacao = ?";
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Notificacao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM notificacao WHERE id_notificacao = ?";
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearNotificacao(rs);
            }
        }
        return null;
    }

    @Override
    public List<Notificacao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM notificacao";
        List<Notificacao> lista = new ArrayList<>();
        try (Connection conn = conexao.abrirConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearNotificacao(rs));
        }
        return lista;
    }

    private Notificacao mapearNotificacao(ResultSet rs) throws SQLException {
        Notificacao n = new Notificacao(rs.getString("mensagem"), lerData(rs), lerCategoria(rs));
        n.setId(rs.getInt("id_notificacao"));
        return n;
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    private java.util.Date lerData(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("data");
        return ts != null ? new java.util.Date(ts.getTime()) : null;
    }

    private CategoriaNotificacao lerCategoria(ResultSet rs) throws SQLException {
        String catStr = rs.getString("categoria");
        return catStr != null ? CategoriaNotificacao.valueOf(catStr) : null;
    }

    private void fechar(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
