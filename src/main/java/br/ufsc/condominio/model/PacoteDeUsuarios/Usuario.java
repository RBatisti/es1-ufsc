package br.ufsc.condominio.model.PacoteDeUsuarios;

import java.util.Date;

public class Usuario {
    private String nome;
    private String CPF;
    private Date dataNascimento;
    private Genero genero;
    private String senha;
    private String email;

    protected Usuario(String nome, String CPF, String email, java.util.Date dataNascimento, Genero genero, String senha) {
        this.nome = nome;
        this.CPF = CPF;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return CPF;
    }
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
}
