package br.ufsc.condominio.model.PacoteDeEspaçosCompartilhados;

import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;

import java.util.Date;

public class Reserva {
    private EspacoCompartilhado espacoCompartilhado;
    private Date inicio;
    private Date fim;
    private Condomino condomino;

    public Reserva(EspacoCompartilhado espacoCompartilhado, Date inicio, Date fim, Condomino condomino) {
        this.espacoCompartilhado = espacoCompartilhado;
        this.inicio = inicio;
        this.fim = fim;
        this.condomino = condomino;
    }

    public EspacoCompartilhado getEspacoCompartilhado() {
        return espacoCompartilhado;
    }

    public void setEspacoCompartilhado(EspacoCompartilhado espacoCompartilhado) {
        this.espacoCompartilhado = espacoCompartilhado;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Condomino getCondomino() {
        return condomino;
    }

    public void setCondomino(Condomino condomino) {
        this.condomino = condomino;
    }
}
