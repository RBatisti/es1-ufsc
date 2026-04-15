package br.ufsc.condominio.controller.ControladorDePrestacãoDeContas;

import br.ufsc.condominio.utils.Armazenamento;
import br.ufsc.condominio.model.PrestacaoContas.TransacaoFinanceira;

import java.util.List;

public class ContasController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();

    public void registrarTransacao(TransacaoFinanceira transacao) {
        armazenamento.adicionarTransacao(transacao);
    }

    public List<TransacaoFinanceira> listar() {
        return armazenamento.getTransacoes();
    }

    public float calcularSaldo() {
        float saldo = 0;
        for (TransacaoFinanceira t : armazenamento.getTransacoes()) {
            if (t.isEntrada()) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }
}
