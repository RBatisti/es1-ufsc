package br.ufsc.condominio.model.PacoteDeUsuarios;

import java.util.Date;

public class Porteiro extends Usuario {
    private Porteiro(String nome, String CPF, String email, Date dataNascimento, Genero genero, String senha) {
        super(nome, CPF, email, dataNascimento, genero, senha);
    }

    public static class Builder {
        private String nome;
        private String CPF;
        private String email;
        private Date dataNascimento;
        private Genero genero;
        private String senha;

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder cpf(String CPF) {
            this.CPF = CPF;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder dataNascimento(Date dataNascimento) {
            this.dataNascimento = dataNascimento;
            return this;
        }

        public Builder genero(Genero genero) {
            this.genero = genero;
            return this;
        }

        public Builder senha(String senha) {
            this.senha = senha;
            return this;
        }

        public Porteiro build() {
            if (nome == null || nome.isBlank()) {
                throw new IllegalStateException("Nome é obrigatório.");
            }

            if (email == null || email.isBlank()) {
                throw new IllegalStateException("Email é obrigatório.");
            }

            if (CPF == null || CPF.isBlank()) {
                throw new IllegalStateException("CPF é obrigatório.");
            }

            if (senha == null || senha.isBlank()) {
                throw new IllegalStateException("Senha é obrigatória.");
            }

            return new Porteiro(
                    this.nome,
                    this.CPF,
                    this.email,
                    this.dataNascimento,
                    this.genero,
                    this.senha
            );
        }
    }
}
