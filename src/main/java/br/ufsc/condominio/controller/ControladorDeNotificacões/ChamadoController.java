package br.ufsc.condominio.controller.ControladorDeNotificacões;

import br.ufsc.condominio.utils.Armazenamento;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.StatusChamado;

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

    public List<Chamado> listarPorStatus(StatusChamado status) {
        List<Chamado> resultado = new ArrayList<>();
        for (Chamado c : armazenamento.getChamados()) {
            if (c.getStatusChamado() == status) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public boolean alterarStatus(int indice, StatusChamado novoStatus) {
        List<Chamado> chamados = armazenamento.getChamados();
        if (indice < 0 || indice >= chamados.size()) {
            return false;
        }
        chamados.get(indice).setStatusChamado(novoStatus);
        return true;
    }

    public void marcarTodosComoVistosPeloSindico() {
        for (Chamado c : armazenamento.getChamados()) {
            c.setVisualizadoPeloSindico(true);
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
