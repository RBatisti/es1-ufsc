package br.ufsc.condominio.model.PacoteDeNotificacoes;

import java.util.Date;

public class Notificacao {
    private int id;
    private String mensagem;
    private Date data;
    private CategoriaNotificacao categoria;

    public Notificacao(String mensagem, Date data, CategoriaNotificacao categoria) {
        this.mensagem = mensagem;
        this.data = data;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public CategoriaNotificacao getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaNotificacao categoria) {
        this.categoria = categoria;
    }
}
