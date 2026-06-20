package br.ufsc.condominio.model.PacoteDeUsuarios;

import java.util.Date;

public class Condomino extends Usuario{
    private String unidade;
    private Date dataCadastro;

    private Condomino(String nome, String CPF, String email, Date dataNascimento, Genero genero, String unidade, String senha) {
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

    public static class Builder {
        private String nome;
        private String CPF;
        private String email;
        private Date dataNascimento;
        private Genero genero;
        private String unidade;
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

        public Builder unidade(String unidade) {
            this.unidade = unidade;
            return this;
        }

        public Builder senha(String senha) {
            this.senha = senha;
            return this;
        }

        public Condomino build() {
            if (nome == null || nome.isBlank()) {
                throw new IllegalStateException("Nome é obrigatório.");
            }

            if (email == null || email.isBlank()) {
                throw new IllegalStateException("Email é obrigatório");
            }

            if (CPF == null || CPF.isBlank()) {
                throw new IllegalStateException("CPF é obrigatório.");
            }

            if (senha == null || senha.isBlank()) {
                throw new IllegalStateException("Senha é obrigatória.");
            }

            return new Condomino(
                    this.nome,
                    this.CPF,
                    this.email,
                    this.dataNascimento,
                    this.genero,
                    this.unidade,
                    this.senha
            );
        }
    }
}
