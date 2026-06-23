package br.ufsc.condominio.persistencia;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    void salvar(T entidade) throws SQLException;
    void atualizar(T entidade) throws SQLException;
    boolean remover(int id) throws SQLException;
    T buscarPorId(int id) throws SQLException;
    List<T> listarTodos() throws SQLException;
}
