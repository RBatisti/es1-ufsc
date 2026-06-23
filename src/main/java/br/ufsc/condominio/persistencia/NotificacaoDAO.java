package br.ufsc.condominio.persistencia;

import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Notificacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO implements Dao<Notificacao> {

    private final ConexaoBancoDados conexao;

    public NotificacaoDAO() {
        this.conexao = ConexaoBancoDados.getInstancia();
    }

    @Override
    public void salvar(Notificacao notificacao) throws SQLException {
        String sql = """
                INSERT INTO notificacao (mensagem, data, categoria, tipo, cpf_criador)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, notificacao.getMensagem());
            stmt.setTimestamp(2, notificacao.getData() != null
                    ? new Timestamp(notificacao.getData().getTime()) : null);
            stmt.setString(3, notificacao.getCategoria() != null
                    ? notificacao.getCategoria().name() : null);
            stmt.setString(4, notificacao.getClass().getSimpleName().toUpperCase());
            stmt.setNull(5, Types.VARCHAR);

            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    notificacao.setId(chaves.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Notificacao notificacao) throws SQLException {
        String sql = """
                UPDATE notificacao SET mensagem=?, data=?, categoria=?
                WHERE id_notificacao=?
                """;
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
        String mensagem = rs.getString("mensagem");
        Timestamp ts = rs.getTimestamp("data");
        java.util.Date data = ts != null ? new java.util.Date(ts.getTime()) : null;
        String catStr = rs.getString("categoria");
        CategoriaNotificacao categoria = catStr != null
                ? CategoriaNotificacao.valueOf(catStr) : null;

        Notificacao n = new Notificacao(mensagem, data, categoria);
        n.setId(rs.getInt("id_notificacao"));
        return n;
    }
}
