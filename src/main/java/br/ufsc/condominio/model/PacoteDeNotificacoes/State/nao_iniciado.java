package br.ufsc.condominio.model.PacoteDeNotificacoes.State;

import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;

public class nao_iniciado implements StatusChamado {
    public void avancar(Chamado chamado) {
        chamado.setStatusChamado(new em_andamento());
    }

    @Override
    public String toString() {
        return "NAO_INICIADO";
    }
}
