package br.ufsc.condominio.controller.ControladorDeNotificacões;

import br.ufsc.condominio.utils.Armazenamento;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.StatusChamado;

import java.util.ArrayList;
import java.util.List;

public class ChamadoController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();

    public void abrirChamado(Chamado chamado) {
        armazenamento.adicionarChamado(chamado);
    }

    public List<Chamado> listarDoCondomino(String cpf) {
        List<Chamado> resultado = new ArrayList<>();
        for (Chamado c : armazenamento.getChamados()) {
            if (c.getCpfCondomino().equals(cpf)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Chamado> listarTodos() {
        return armazenamento.getChamados();
    }

    public List<Chamado> listarPorStatus(Class<? extends StatusChamado> status) {

        List<Chamado> resultado = new ArrayList<>();
        for (Chamado c : armazenamento.getChamados()) {
            if (status.isInstance(c.getStatusChamado())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public boolean alterarStatus(int indice) {
        List<Chamado> chamados = armazenamento.getChamados();
        if (indice < 0 || indice >= chamados.size()) {
            return false;
        }
        Chamado chamado = chamados.get(indice);
        chamado.avancarStatus();
        armazenamento.atualizarChamado(chamado);
        return true;
    }

    public void marcarTodosComoVistosPeloSindico() {
        for (Chamado c : armazenamento.getChamados()) {
            if (!c.isVisualizadoPeloSindico()) {
                c.setVisualizadoPeloSindico(true);
                armazenamento.atualizarChamado(c);
            }
        }
    }

    public int contarNaoVistosPeloSindico() {
        int count = 0;
        for (Chamado c : armazenamento.getChamados()) {
            if (!c.isVisualizadoPeloSindico()) {
                count++;
            }
        }
        return count;
    }
}
