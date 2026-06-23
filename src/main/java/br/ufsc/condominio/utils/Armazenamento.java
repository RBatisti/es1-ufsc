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
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.model.PrestacaoContas.TransacaoFinanceira;
import br.ufsc.condominio.persistencia.NotificacaoDAO;
import br.ufsc.condominio.persistencia.UsuarioDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    private Armazenamento() {
        try {
            usuarios = new UsuarioDAO().listarTodos();
        } catch (SQLException e) {
            System.err.println("Aviso: não foi possível carregar usuários do banco de dados. " + e.getMessage());
            usuarios = new ArrayList<>();
        }

        try {
            avisos = notificacaoDAO.listarAvisos();
        } catch (SQLException e) {
            System.err.println("Aviso: não foi possível carregar avisos do banco de dados. " + e.getMessage());
            avisos = new ArrayList<>();
        }

        try {
            chamados = notificacaoDAO.listarChamados();
        } catch (SQLException e) {
            System.err.println("Aviso: não foi possível carregar chamados do banco de dados. " + e.getMessage());
            chamados = new ArrayList<>();
        }

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
        try {
            new UsuarioDAO().salvar(usuario);
            // Novo usuário passa a enxergar todos os avisos existentes como não lidos.
            notificacaoDAO.registrarUsuarioEmAvisos(usuario.getCPF());
        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário no banco de dados: " + e.getMessage());
        }
        usuarios.add(usuario);
    }

    public void atualizarUsuario(Usuario usuario) {
        try {
            new UsuarioDAO().atualizar(usuario);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário no banco de dados: " + e.getMessage());
        }
    }

    public boolean removerUsuario(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCPF().equals(cpf)) {
                try {
                    new UsuarioDAO().remover(cpf);
                } catch (SQLException e) {
                    System.err.println("Erro ao remover usuário do banco de dados: " + e.getMessage());
                }
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
        try {
            notificacaoDAO.salvar(aviso);
        } catch (SQLException e) {
            System.err.println("Erro ao salvar aviso no banco de dados: " + e.getMessage());
        }
        avisos.add(aviso);
    }

    public void marcarAvisoVisualizado(Aviso aviso, String cpf) {
        aviso.marcarComoVisto(cpf);
        try {
            notificacaoDAO.marcarAvisoVisualizado(aviso.getId(), cpf);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar visualização do aviso: " + e.getMessage());
        }
    }

    // --- Chamados ---

    public List<Chamado> getChamados() {
        return chamados;
    }

    public void adicionarChamado(Chamado chamado) {
        try {
            notificacaoDAO.salvar(chamado);
        } catch (SQLException e) {
            System.err.println("Erro ao salvar chamado no banco de dados: " + e.getMessage());
        }
        chamados.add(chamado);
    }

    public void atualizarChamado(Chamado chamado) {
        try {
            notificacaoDAO.atualizarChamado(chamado);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar chamado no banco de dados: " + e.getMessage());
        }
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
