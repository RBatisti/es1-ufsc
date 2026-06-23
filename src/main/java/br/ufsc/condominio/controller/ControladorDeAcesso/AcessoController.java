package br.ufsc.condominio.controller.ControladorDeAcesso;

import br.ufsc.condominio.controller.ControladorDeNotificacões.AvisoController;
import br.ufsc.condominio.model.PacoteDeAcesso.Acesso;
import br.ufsc.condominio.model.PacoteDeAcesso.SolicitacaoAcesso;
import br.ufsc.condominio.model.PacoteDeAcesso.StatusSolicitacao;
import br.ufsc.condominio.model.PacoteDeAcesso.Visitante;
import br.ufsc.condominio.model.PacoteDeUsuarios.Porteiro;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.utils.Armazenamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AcessoController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();
    private final AvisoController avisoController = new AvisoController();

    public Visitante verificaAutorizacao(String cpf) {
        List<Visitante> autorizados = armazenamento.getAutorizados();
        int n = autorizados.size();
        for (int i = 0; i < n; i++) {
            Visitante a = autorizados.get(i);
            String cpf_aut = a.getCpf();
            LocalDateTime hora_inicio = a.getHoraInicio();
            LocalDateTime hora_fim = a.getHoraFim();
            boolean valid = Armazenamento.betweenDates(hora_inicio, hora_fim);
            if (cpf.equals(cpf_aut) && valid) {
                boolean t = registrarAcesso(a);
                if (t) {
                    return a;
                }
            }
        }
        return null;
    }

    public boolean registrarAcesso(Visitante a) {
        Usuario user = armazenamento.getCurrentUser();
        if (!(user instanceof Porteiro)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        Acesso b = new Acesso(a, now, (Porteiro) user);
        armazenamento.adicionarAcesso(b);
        avisoController.gerarAvisoAcesso(b);
        return true;
    }

    public List<Acesso> listarAcessos() {
        return armazenamento.getAcessos();
    }

    public List<Acesso> listarAcessosAtivos() {
        List<Acesso> resultado = new ArrayList<>();
        for (Acesso a : armazenamento.getAcessos()) {
            if (a.getDataHoraSaida() == null) resultado.add(a);
        }
        return resultado;
    }

    public void registrarSaida(Acesso acesso) {
        acesso.setDataHoraSaida(LocalDateTime.now());
    }

    public SolicitacaoAcesso criarSolicitacao(String nome, String cpf, String unidade) {
        Usuario user = armazenamento.getCurrentUser();
        if (!(user instanceof Porteiro)) return null;
        SolicitacaoAcesso s = new SolicitacaoAcesso(nome, cpf, unidade, (Porteiro) user);
        armazenamento.adicionarSolicitacao(s);
        avisoController.notificarSolicitacaoAcesso(s);
        return s;
    }

    public List<SolicitacaoAcesso> listarSolicitacoes() {
        return armazenamento.getSolicitacoes();
    }

    public List<SolicitacaoAcesso> listarSolicitacoesPendentesPorUnidade(String unidade) {
        List<SolicitacaoAcesso> resultado = new ArrayList<>();
        for (SolicitacaoAcesso s : armazenamento.getSolicitacoes()) {
            if (s.getUnidadeDestino().equals(unidade) && s.getStatus() == StatusSolicitacao.PENDENTE) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    public void responderSolicitacao(SolicitacaoAcesso s, boolean autorizado, String motivo) {
        s.setStatus(autorizado ? StatusSolicitacao.AUTORIZADO : StatusSolicitacao.NEGADO);
        if (motivo != null && !motivo.isBlank()) s.setMotivo(motivo);
    }

    public boolean registrarAcessoManual(SolicitacaoAcesso s) {
        Usuario user = armazenamento.getCurrentUser();
        if (!(user instanceof Porteiro)) return false;
        Visitante v = new Visitante(s.getNomeVisitante(), s.getCpfVisitante(),
                s.getUnidadeDestino(), s.getDataHora(), LocalDateTime.now());
        armazenamento.adicionarAcesso(new Acesso(v, LocalDateTime.now(), (Porteiro) user));
        s.setAcessoRegistrado(true);
        return true;
    }

    public boolean emTimeout(SolicitacaoAcesso s) {
        return s.getStatus() == StatusSolicitacao.PENDENTE
                && LocalDateTime.now().isAfter(s.getDataHora().plusMinutes(3));
    }
}
