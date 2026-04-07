package br.ufsc.condominio.controller.ControladorDePrestacãoDeContas;

import br.ufsc.condominio.controller.MainController;
import br.ufsc.condominio.model.TransacaoFinanceira;
import java.util.ArrayList;
import java.util.List;

public class ContasController {

    private List<TransacaoFinanceira> historicoTransacoes;
    private MainController mainController;

    public ContasController(MainController mainController) {
        this.mainController = mainController;
        this.historicoTransacoes = new ArrayList<>();
    }



    public void registrarReceita(TransacaoFinanceira receita) {
        historicoTransacoes.add(receita);
        System.out.println("Receita registrada com sucesso: " + receita.getDescricao());
    }

    public void registrarDespesa(TransacaoFinanceira despesa) {
        historicoTransacoes.add(despesa);
        System.out.println("Despesa registrada com sucesso: " + despesa.getDescricao());
    }

    public double calcularSaldoAtual() {
        double saldo = 0.0;

//        for (TransacaoFinanceira t : historicoTransacoes) {
//            if (t.getTipo().equalsIgnoreCase("RECEITA")) {
//                saldo += t.getValor();
//            } else if (t.getTipo().equalsIgnoreCase("DESPESA")) {
//                saldo -= t.getValor();
//            }
//        }
        return saldo;
    }

    public List<TransacaoFinanceira> listarTodasTransacoes() {
        return historicoTransacoes;
    }

    public List<TransacaoFinanceira> gerarRelatorioMensal(int mes, int ano) {
        List<TransacaoFinanceira> relatorioMensal = new ArrayList<>();
//
//        for (TransacaoFinanceira t : historicoTransacoes) {
//            if (t.getMes() == mes && t.getAno() == ano) {
//                relatorioMensal.add(t);
//            }
//        }

        return relatorioMensal;
    }
}