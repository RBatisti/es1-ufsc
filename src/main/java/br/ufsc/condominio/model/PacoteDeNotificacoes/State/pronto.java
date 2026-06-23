package br.ufsc.condominio.model.PacoteDeNotificacoes.State;

import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;

public class pronto implements StatusChamado {
    public void avancar(Chamado chamado) {
        throw new IllegalStateException("O chamado já está concluído e não pode avançar de status.");
    }    

    @Override
    public String toString() {
        return "PRONTO";
    }
}
