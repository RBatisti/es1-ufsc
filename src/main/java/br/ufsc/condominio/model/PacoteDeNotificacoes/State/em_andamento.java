package br.ufsc.condominio.model.PacoteDeNotificacoes.State;

import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;
import br.ufsc.condominio.utils.Armazenamento;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Aviso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class em_andamento implements StatusChamado {
    
    public void avancar(Chamado chamado) {
        chamado.setStatusChamado(new pronto());

        Armazenamento armazenamento = Armazenamento.getInstancia();
        
        List<String> dest = new ArrayList<>();
        if (chamado.getCpfCondomino() != null) {
            dest.add(chamado.getCpfCondomino());
        }

        Aviso aviso = new Aviso(
            "O seu chamado já foi atendido.", 
            new Date(), 
            CategoriaNotificacao.IMPORTANTE,
            dest
        );
        armazenamento.adicionarAviso(aviso);
    }

    @Override
    public String toString() {
        return "EM_ANDAMENTO";
    }
}
