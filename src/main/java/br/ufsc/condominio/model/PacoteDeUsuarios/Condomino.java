package br.ufsc.condominio.model.PacoteDeUsuarios;

public class Condomino extends Usuario{
    private String unidade;

    public Condomino(String nome, String CPF, String email, java.util.Date dataNascimento, Genero genero, String unidade, String senha) {
        super(nome, CPF, email, dataNascimento, genero, senha);
        this.unidade = unidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
