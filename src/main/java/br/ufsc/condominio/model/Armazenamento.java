package br.ufsc.condominio.model;

import java.util.List;

public class Armazenamento {
    private Armazenamento instancia;
    private List<Usuario> usuarios;

    public Armazenamento getInstancia() {
        if (instancia == null) {
            instancia = new Armazenamento();
        }
        return instancia;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
