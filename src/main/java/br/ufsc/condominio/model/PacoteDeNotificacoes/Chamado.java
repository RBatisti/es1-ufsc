package br.ufsc.condominio.model.PacoteDeNotificacoes;

import java.util.Date;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.StatusChamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.nao_iniciado;

public class Chamado extends Notificacao {
    private StatusChamado statusChamado;
    private String cpfCondomino;
    private boolean visualizadoPeloSindico;

    public Chamado(String mensagem, Date data, CategoriaNotificacao categoria, String cpfCondomino) {
        super(mensagem, data, categoria);
        this.statusChamado = new nao_iniciado();
        this.cpfCondomino = cpfCondomino;
        this.visualizadoPeloSindico = false;
    }

    public StatusChamado getStatusChamado() {
        return statusChamado;
    }

    public void setStatusChamado(StatusChamado statusChamado) {
        this.statusChamado = statusChamado;
    }

    public void avancarStatus() {
        this.statusChamado.avancar(this);
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
