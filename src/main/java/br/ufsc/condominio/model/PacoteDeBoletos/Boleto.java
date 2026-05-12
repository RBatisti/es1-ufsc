package br.ufsc.condominio.model.PacoteDeBoletos;

import java.time.LocalDate;

public class Boleto {
    private String unidade;
    private String mesReferencia;
    private double valorBase;
    private double multa;
    private double juros;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private StatusBoleto status;
    private String comprovante;
    private boolean sindicoNotificadoInadimplencia;

    public Boleto(String unidade, String mesReferencia, double valorBase, LocalDate dataVencimento) {
        this.unidade = unidade;
        this.mesReferencia = mesReferencia;
        this.valorBase = valorBase;
        this.dataVencimento = dataVencimento;
        this.multa = 0;
        this.juros = 0;
        this.status = StatusBoleto.PENDENTE;
        this.sindicoNotificadoInadimplencia = false;
    }

    public double getValorTotal() {
        return valorBase + multa + juros;
    }

    public String getUnidade() { return unidade; }
    public String getMesReferencia() { return mesReferencia; }
    public double getValorBase() { return valorBase; }
    public double getMulta() { return multa; }
    public double getJuros() { return juros; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public StatusBoleto getStatus() { return status; }
    public String getComprovante() { return comprovante; }
    public boolean isSindicoNotificadoInadimplencia() { return sindicoNotificadoInadimplencia; }

    public void setMulta(double multa) { this.multa = multa; }
    public void setJuros(double juros) { this.juros = juros; }
    public void setStatus(StatusBoleto status) { this.status = status; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public void setComprovante(String comprovante) { this.comprovante = comprovante; }
    public void setSindicoNotificadoInadimplencia(boolean v) { this.sindicoNotificadoInadimplencia = v; }
}
