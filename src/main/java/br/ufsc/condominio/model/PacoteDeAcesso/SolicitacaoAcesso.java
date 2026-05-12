package br.ufsc.condominio.model.PacoteDeAcesso;

import br.ufsc.condominio.model.PacoteDeUsuarios.Porteiro;

import java.time.LocalDateTime;

public class SolicitacaoAcesso {
    private String nomeVisitante;
    private String cpfVisitante;
    private String unidadeDestino;
    private LocalDateTime dataHora;
    private Porteiro porteiro;
    private StatusSolicitacao status;
    private String motivo;
    private boolean acessoRegistrado;

    public SolicitacaoAcesso(String nomeVisitante, String cpfVisitante, String unidadeDestino, Porteiro porteiro) {
        this.nomeVisitante = nomeVisitante;
        this.cpfVisitante = cpfVisitante;
        this.unidadeDestino = unidadeDestino;
        this.dataHora = LocalDateTime.now();
        this.porteiro = porteiro;
        this.status = StatusSolicitacao.PENDENTE;
        this.motivo = null;
        this.acessoRegistrado = false;
    }

    public String getNomeVisitante() { return nomeVisitante; }
    public String getCpfVisitante() { return cpfVisitante; }
    public String getUnidadeDestino() { return unidadeDestino; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Porteiro getPorteiro() { return porteiro; }
    public StatusSolicitacao getStatus() { return status; }
    public String getMotivo() { return motivo; }
    public boolean isAcessoRegistrado() { return acessoRegistrado; }

    public void setStatus(StatusSolicitacao status) { this.status = status; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setAcessoRegistrado(boolean acessoRegistrado) { this.acessoRegistrado = acessoRegistrado; }
}
