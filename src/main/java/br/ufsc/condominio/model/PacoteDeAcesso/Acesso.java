package br.ufsc.condominio.model.PacoteDeAcesso;

import br.ufsc.condominio.model.PacoteDeUsuarios.Porteiro;

import java.time.LocalDateTime;

public class Acesso {
    private Visitante visitante;
    private LocalDateTime dataHora;
    private LocalDateTime dataHoraSaida;
    private Porteiro porteiro;

    public Acesso(Visitante a, LocalDateTime now, Porteiro porteiro) {
        this.visitante = a;
        this.dataHora = now;
        this.dataHoraSaida = null;
        this.porteiro = porteiro;
    }

    public Visitante getVisitante() { return visitante; }
    public LocalDateTime getDataHora() { return dataHora; }
    public LocalDateTime getDataHoraSaida() { return dataHoraSaida; }
    public Porteiro getPorteiro() { return porteiro; }
    public void setDataHoraSaida(LocalDateTime dataHoraSaida) { this.dataHoraSaida = dataHoraSaida; }
}
