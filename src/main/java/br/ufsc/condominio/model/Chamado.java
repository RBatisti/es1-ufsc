package br.ufsc.condominio.model;

import java.util.Date;

public class Chamado extends Notificacao {
    private StatusChamado statusChamado;

    public Chamado(String mensagem, Date data, CategoriaNotificacao categoria, StatusChamado statusChamado) {
        super(mensagem, data, categoria);
        this.statusChamado = statusChamado;
    }

    public StatusChamado getStatusChamado() {
        return statusChamado;
    }

    public void setStatusChamado(StatusChamado statusChamado) {
        this.statusChamado = statusChamado;
    }
}
