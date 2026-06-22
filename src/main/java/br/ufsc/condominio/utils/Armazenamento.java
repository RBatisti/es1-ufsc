package br.ufsc.condominio.utils;

import br.ufsc.condominio.model.PacoteDeAcesso.Acesso;
import br.ufsc.condominio.model.PacoteDeAcesso.SolicitacaoAcesso;
import br.ufsc.condominio.model.PacoteDeBoletos.Boleto;
import br.ufsc.condominio.model.PacoteDeAcesso.Visitante;
import br.ufsc.condominio.model.PacoteDeEspaçosCompartilhados.EspacoCompartilhado;
import br.ufsc.condominio.model.PacoteDeEspaçosCompartilhados.Reserva;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Aviso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeUsuarios.Genero;
import br.ufsc.condominio.model.PacoteDeUsuarios.Porteiro;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.model.PrestacaoContas.TransacaoFinanceira;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private List<Visitante> autorizados;
    private List<Acesso> acessos;
    private List<SolicitacaoAcesso> solicitacoes;
    private List<Boleto> boletos;
    private double taxaCondominialPadrao = 500.00;
    private int limiteReservasFuturas = 3;
    private Usuario currentUser;

    private Armazenamento() {
        Date nascimento = Date.from(LocalDate.of(1980, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        usuarios = new ArrayList<>();
        usuarios.add(new Sindico.Builder()
                .nome("Zé")
                .cpf("012.345.678-12")
                .email("s")
                .dataNascimento(nascimento)
                .genero(Genero.MASCULINO)
                .senha("123")
                .build());
        usuarios.add(new Porteiro.Builder()
                .nome("João")
                .cpf("999.888.777-66")
                .email("p")
                .dataNascimento(nascimento)
                .genero(Genero.MASCULINO)
                .senha("123")
                .build());

        Condomino.Builder condominoBuilder = new Condomino.Builder()
                .nome("Olivia")
                .cpf("111.222.333-44")
                .email("c")
                .dataNascimento(nascimento)
                .genero(Genero.FEMININO)
                .unidade("205")
                .senha("123");
        usuarios.add(condominoBuilder.build());

        avisos = new ArrayList<>();
        chamados = new ArrayList<>();
        espacos = new ArrayList<>();
        reservas = new ArrayList<>();
        transacoes = new ArrayList<>();
        autorizados = new ArrayList<>();
        acessos = new ArrayList<>();
        solicitacoes = new ArrayList<>();
        boletos = new ArrayList<>();
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

    // --- Utilitário ---
    public static boolean betweenDates(LocalDateTime horaInicio, LocalDateTime horaFim) {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(horaInicio) && !now.isAfter(horaFim);
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

    // --- Autorizados ---

    public List<Visitante> getAutorizados() {
        return autorizados;
    }

    public void adicionarAutorizado(Visitante visitante) {
        autorizados.add(visitante);
    }

    public boolean removerAutorizado(String cpf, String unidade) {
        for (Visitante v : autorizados) {
            if (v.getCpf().equals(cpf) && v.getUnidadeDestino().equals(unidade)) {
                autorizados.remove(v);
                return true;
            }
        }
        return false;
    }

    // --- Acessos ---

    public List<Acesso> getAcessos() {
        return acessos;
    }

    public void adicionarAcesso(Acesso acesso) {
        acessos.add(acesso);
    }

    // --- Solicitacoes de Acesso ---

    public List<SolicitacaoAcesso> getSolicitacoes() {
        return solicitacoes;
    }

    public void adicionarSolicitacao(SolicitacaoAcesso s) {
        solicitacoes.add(s);
    }

    // --- Utilitário de Usuários ---

    public String buscarCondominoPorUnidade(String unidade) {
        for (Usuario u : usuarios) {
            if (u instanceof Condomino && ((Condomino) u).getUnidade().equals(unidade)) {
                return u.getCPF();
            }
        }
        return null;
    }

    // --- Boletos ---

    public List<Boleto> getBoletos() {
        return boletos;
    }

    public void adicionarBoleto(Boleto boleto) {
        boletos.add(boleto);
    }

    // --- Taxa condominial padrão ---

    public double getTaxaCondominialPadrao() { return taxaCondominialPadrao; }
    public void setTaxaCondominialPadrao(double taxa) { this.taxaCondominialPadrao = taxa; }

    // --- Limite de reservas futuras por condômino ---

    public int getLimiteReservasFuturas() { return limiteReservasFuturas; }
    public void setLimiteReservasFuturas(int limite) { this.limiteReservasFuturas = limite; }

    // --- Sessão ---

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario usuario) {
        this.currentUser = usuario;
    }
}
