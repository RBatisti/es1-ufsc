package br.ufsc.condominio.model.PacoteDeNotificacoes.State;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;

public interface StatusChamado {
    public void avancar(Chamado chamado);
}
