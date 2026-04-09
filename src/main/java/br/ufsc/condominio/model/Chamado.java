package br.ufsc.condominio.model;

import java.util.Date;

public class Chamado extends Notificacao {
    private StatusChamado statusChamado;
    private String cpfCondomino;
    private boolean visualizadoPeloSindico;

    public Chamado(String mensagem, Date data, CategoriaNotificacao categoria, StatusChamado statusChamado, String cpfCondomino) {
        super(mensagem, data, categoria);
        this.statusChamado = statusChamado;
        this.cpfCondomino = cpfCondomino;
        this.visualizadoPeloSindico = false;
    }

    public StatusChamado getStatusChamado() {
        return statusChamado;
    }

    public void setStatusChamado(StatusChamado statusChamado) {
        this.statusChamado = statusChamado;
    }

    public String getCpfCondomino() {
        return cpfCondomino;
    }

    public boolean isVisualizadoPeloSindico() {
        return visualizadoPeloSindico;
    }

    public void setVisualizadoPeloSindico(boolean visualizadoPeloSindico) {
        this.visualizadoPeloSindico = visualizadoPeloSindico;
    }
}
