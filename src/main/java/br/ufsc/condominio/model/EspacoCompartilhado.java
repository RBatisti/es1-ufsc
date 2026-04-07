package br.ufsc.condominio.model;

public class EspacoCompartilhado {
    private String nome;

    public EspacoCompartilhado(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
