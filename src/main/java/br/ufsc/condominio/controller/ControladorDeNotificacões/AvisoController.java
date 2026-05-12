package br.ufsc.condominio.controller.ControladorDeNotificacões;

import br.ufsc.condominio.model.PacoteDeAcesso.Acesso;
import br.ufsc.condominio.model.PacoteDeAcesso.SolicitacaoAcesso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Aviso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;
import br.ufsc.condominio.utils.Armazenamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AvisoController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();

    public void enviarAviso(Aviso aviso) {
        armazenamento.adicionarAviso(aviso);
    }

    public List<Aviso> listar(String cpf) {
        List<Aviso> resultado = new ArrayList<>();
        for (Aviso a : armazenamento.getAvisos()) {
            if (a.isVisibleTo(cpf)) resultado.add(a);
        }
        return resultado;
    }

    public List<Aviso> listarParaCondomino(String cpf, Date dataCadastro) {
        List<Aviso> resultado = new ArrayList<>();
        long umMesEmMs = 30L * 24 * 60 * 60 * 1000;
        for (Aviso a : armazenamento.getAvisos()) {
            if (a.isVisibleTo(cpf) && !a.getData().before(new Date(dataCadastro.getTime() - umMesEmMs))) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public void gerarAvisoAcesso(Acesso acesso) {
        String unidade = acesso.getVisitante().getUnidadeDestino();
        String mensagem = "Visitante " + acesso.getVisitante().getNome()
                + " aguarda na portaria para a unidade " + unidade;
        List<String> dest = new ArrayList<>();
        String cpf = armazenamento.buscarCondominoPorUnidade(unidade);
        if (cpf != null) dest.add(cpf);
        armazenamento.adicionarAviso(new Aviso(mensagem, new Date(), CategoriaNotificacao.IMPORTANTE, dest));
    }

    public void notificarSolicitacaoAcesso(SolicitacaoAcesso s) {
        String mensagem = "PORTARIA: Visitante " + s.getNomeVisitante()
                + " sem pré-autorização aguarda na portaria. Responda em até 3 minutos.";
        List<String> dest = new ArrayList<>();
        String cpf = armazenamento.buscarCondominoPorUnidade(s.getUnidadeDestino());
        if (cpf != null) dest.add(cpf);
        armazenamento.adicionarAviso(new Aviso(mensagem, new Date(), CategoriaNotificacao.IMPORTANTE, dest));
    }

    public int contarNaoLidos(String cpf, Date dataCadastro) {
        List<Aviso> avisos = (dataCadastro == null) ? listar(cpf) : listarParaCondomino(cpf, dataCadastro);
        int count = 0;
        for (Aviso a : avisos) {
            if (!a.foiVistoPor(cpf)) {
                count++;
            }
        }
        return count;
    }
}
