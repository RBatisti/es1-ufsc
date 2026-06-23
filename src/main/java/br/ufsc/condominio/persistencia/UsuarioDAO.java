package br.ufsc.condominio.persistencia;

import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeUsuarios.Genero;
import br.ufsc.condominio.model.PacoteDeUsuarios.Porteiro;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements Dao<Usuario> {

    // Mapeamento fixo baseado nos INSERTs do esquema-bd.sql
    private static final int TIPO_SINDICO    = 1;
    private static final int TIPO_CONDOMINO  = 2;
    private static final int TIPO_PORTEIRO   = 3;
    private static final int GENERO_MASCULINO = 1;
    private static final int GENERO_FEMININO  = 2;

    private final ConexaoBancoDados conexao;

    public UsuarioDAO() {
        this.conexao = ConexaoBancoDados.getInstancia();
    }

    @Override
    public void salvar(Usuario usuario) throws SQLException {
        String sql = """
                INSERT INTO usuario (cpf, nome, data_nascimento, id_genero, senha, email, id_tipo_usuario, unidade, data_cadastro)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getCPF());
            stmt.setString(2, usuario.getNome());
            stmt.setDate(3, usuario.getDataNascimento() != null
                    ? new java.sql.Date(usuario.getDataNascimento().getTime()) : null);
            stmt.setInt(4, generoParaId(usuario.getGenero()));
            stmt.setString(5, usuario.getSenha());
            stmt.setString(6, usuario.getEmail());
            stmt.setInt(7, tipoParaId(usuario));

            if (usuario instanceof Condomino c) {
                stmt.setObject(8, c.getUnidade() != null ? Integer.parseInt(c.getUnidade()) : null);
                stmt.setDate(9, c.getDataCadastro() != null
                        ? new java.sql.Date(c.getDataCadastro().getTime()) : null);
            } else {
                stmt.setNull(8, Types.INTEGER);
                stmt.setNull(9, Types.DATE);
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = """
                UPDATE usuario SET nome=?, data_nascimento=?, id_genero=?, senha=?, email=?, id_tipo_usuario=?, unidade=?, data_cadastro=?
                WHERE cpf=?
                """;
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, usuario.getDataNascimento() != null
                    ? new java.sql.Date(usuario.getDataNascimento().getTime()) : null);
            stmt.setInt(3, generoParaId(usuario.getGenero()));
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getEmail());
            stmt.setInt(6, tipoParaId(usuario));

            if (usuario instanceof Condomino c) {
                stmt.setObject(7, c.getUnidade() != null ? Integer.parseInt(c.getUnidade()) : null);
                stmt.setDate(8, c.getDataCadastro() != null
                        ? new java.sql.Date(c.getDataCadastro().getTime()) : null);
            } else {
                stmt.setNull(7, Types.INTEGER);
                stmt.setNull(8, Types.DATE);
            }

            stmt.setString(9, usuario.getCPF());
            stmt.executeUpdate();
        }
    }

    /** Usuario usa CPF como PK, não int. Use {@link #remover(String)} no lugar. */
    @Override
    public boolean remover(int id) {
        throw new UnsupportedOperationException("Usuario usa CPF como chave primária. Use remover(String cpf).");
    }

    public boolean remover(String cpf) throws SQLException {
        String sql = "DELETE FROM usuario WHERE cpf = ?";
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            return stmt.executeUpdate() > 0;
        }
    }

    /** Usuario usa CPF como PK, não int. Use {@link #buscarPorCpf(String)} no lugar. */
    @Override
    public Usuario buscarPorId(int id) {
        throw new UnsupportedOperationException("Usuario usa CPF como chave primária. Use buscarPorCpf(String cpf).");
    }

    public Usuario buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";
        try (Connection conn = conexao.abrirConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = conexao.abrirConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearUsuario(rs));
        }
        return lista;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        String cpf = rs.getString("cpf");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");
        java.sql.Date dataNasc = rs.getDate("data_nascimento");
        Date dataNascimento = dataNasc != null ? new Date(dataNasc.getTime()) : null;
        Genero genero = idParaGenero(rs.getInt("id_genero"));
        int tipo = rs.getInt("id_tipo_usuario");

        return switch (tipo) {
            case TIPO_SINDICO -> new Sindico.Builder()
                    .cpf(cpf).nome(nome).email(email).senha(senha)
                    .dataNascimento(dataNascimento).genero(genero).build();
            case TIPO_PORTEIRO -> new Porteiro.Builder()
                    .cpf(cpf).nome(nome).email(email).senha(senha)
                    .dataNascimento(dataNascimento).genero(genero).build();
            default -> { // TIPO_CONDOMINO
                String unidade = rs.getObject("unidade") != null ? rs.getString("unidade") : null;
                yield new Condomino.Builder()
                        .cpf(cpf).nome(nome).email(email).senha(senha)
                        .dataNascimento(dataNascimento).genero(genero).unidade(unidade).build();
            }
        };
    }

    private int tipoParaId(Usuario u) {
        if (u instanceof Sindico)   return TIPO_SINDICO;
        if (u instanceof Porteiro)  return TIPO_PORTEIRO;
        return TIPO_CONDOMINO;
    }

    private int generoParaId(Genero g) {
        return g == Genero.MASCULINO ? GENERO_MASCULINO : GENERO_FEMININO;
    }

    private Genero idParaGenero(int id) {
        return id == GENERO_MASCULINO ? Genero.MASCULINO : Genero.FEMININO;
    }
}
