package br.ufsc.condominio.model.PacoteDeAcesso;

import java.time.LocalDateTime;

public class Visitante {
    private String nome;
    private String cpf;
    private String unidadeDestino;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;

    public Visitante(String nome, String cpf, String unidadeDestino, LocalDateTime horaInicio, LocalDateTime horaFim) {
        this.nome = nome;
        this.cpf = cpf;
        this.unidadeDestino = unidadeDestino;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getUnidadeDestino() { return unidadeDestino; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public LocalDateTime getHoraFim() { return horaFim; }
}
