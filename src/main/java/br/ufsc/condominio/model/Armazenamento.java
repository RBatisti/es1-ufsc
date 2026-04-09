package br.ufsc.condominio.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Armazenamento {
    private static Armazenamento instancia;

    private List<Usuario> usuarios;
    private List<Aviso> avisos;
    private List<Chamado> chamados;
    private List<EspacoCompartilhado> espacos;
    private List<Reserva> reservas;
    private List<TransacaoFinanceira> transacoes;

    private Armazenamento() {
        Date nascimento = Date.from(LocalDate.of(1980, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        usuarios = new ArrayList<>();
        usuarios.add(new Sindico("Nicolas", "012.345.678-12", "nicolas@gmail.com", nascimento, Genero.MASCULINO, "123"));
        usuarios.add(new Condomino("Ana Lima", "111.222.333-44", "ana@gmail.com", nascimento, Genero.FEMININO, "101", "456"));

        avisos = new ArrayList<>();
        chamados = new ArrayList<>();
        espacos = new ArrayList<>();
        reservas = new ArrayList<>();
        transacoes = new ArrayList<>();
    }

    public static Armazenamento getInstancia() {
        if (instancia == null) {
            instancia = new Armazenamento();
        }
        return instancia;
    }

    // --- Usuarios ---

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public boolean removerUsuario(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCPF().equals(cpf)) {
                usuarios.remove(u);
                return true;
            }
        }
        return false;
    }

    public Usuario buscarPorCpf(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCPF().equals(cpf)) {
                return u;
            }
        }
        return null;
    }

    // --- Avisos ---

    public List<Aviso> getAvisos() {
        return avisos;
    }

    public void adicionarAviso(Aviso aviso) {
        avisos.add(aviso);
    }

    // --- Chamados ---

    public List<Chamado> getChamados() {
        return chamados;
    }

    public void adicionarChamado(Chamado chamado) {
        chamados.add(chamado);
    }

    // --- Espacos ---

    public List<EspacoCompartilhado> getEspacos() {
        return espacos;
    }

    public void adicionarEspaco(EspacoCompartilhado espaco) {
        espacos.add(espaco);
    }

    // --- Reservas ---

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public void removerReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    // --- Transacoes ---

    public List<TransacaoFinanceira> getTransacoes() {
        return transacoes;
    }

    public void adicionarTransacao(TransacaoFinanceira transacao) {
        transacoes.add(transacao);
    }
}
