package br.ufsc.condominio.model;

public class Condomino extends Usuario{
    private String unidade;

    public Condomino(String nome, String CPF, String email, java.util.Date dataNascimento, Genero genero, String unidade) {
        super(nome, CPF, email, dataNascimento, genero);
        this.unidade = unidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
