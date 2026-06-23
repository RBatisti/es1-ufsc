package br.ufsc.condominio.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBancoDados {
    private static ConexaoBancoDados instancia;

    private static final String URL     = "jdbc:postgresql://localhost:5432/condominio";
    private static final String USUARIO = "admin";
    private static final String SENHA   = "admin";

    private ConexaoBancoDados() {}

    public static ConexaoBancoDados getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBancoDados();
        }
        return instancia;
    }

    public Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
