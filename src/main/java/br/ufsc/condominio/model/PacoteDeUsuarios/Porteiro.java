package br.ufsc.condominio.model.PacoteDeUsuarios;

import java.util.Date;

public class Porteiro extends Usuario {
    public Porteiro(String nome, String CPF, String email, Date dataNascimento, Genero genero, String senha) {
        super(nome, CPF, email, dataNascimento, genero, senha);
    }
}
