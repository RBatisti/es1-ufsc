package br.ufsc.condominio.model.PacoteDeUsuarios;

import java.util.Date;

public class Condomino extends Usuario{
    private String unidade;
    private Date dataCadastro;

    public Condomino(String nome, String CPF, String email, java.util.Date dataNascimento, Genero genero, String unidade, String senha) {
        super(nome, CPF, email, dataNascimento, genero, senha);
        this.unidade = unidade;
        this.dataCadastro = new Date();
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }
}
