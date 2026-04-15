package br.ufsc.condominio.model.PrestacaoContas;

import java.util.Date;

public class TransacaoFinanceira {
    private float valor;
    private Date data;
    private String descricao;
    private boolean entrada;
    private String categoria;

    public TransacaoFinanceira(float valor, Date data, String descricao, boolean entrada, String categoria) {
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.entrada = entrada;
        this.categoria = categoria;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getValor() {
        return valor;
    }


}
