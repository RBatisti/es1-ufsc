package br.ufsc.condominio.view.ViewDeContas;

import br.ufsc.condominio.controller.ControladorDePrestacãoDeContas.ContasController;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PrestacaoContas.TransacaoFinanceira;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ViewDeContas {

    private static final ContasController contasController = new ContasController();

    public static void exibirMenu(Scanner scanner, Usuario usuarioLogado) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- PRESTAÇÃO DE CONTAS ---");
            System.out.println("1. Ver extrato financeiro");
            if (usuarioLogado instanceof Sindico) {
                System.out.println("2. Registrar entrada");
                System.out.println("3. Registrar saída");
            }
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    verExtrato();
                    break;
                case 2:
                    if (usuarioLogado instanceof Sindico) {
                        registrarTransacao(scanner, true);
                    } else {
                        System.out.println("Opção inválida!");
                    }
                    break;
                case 3:
                    if (usuarioLogado instanceof Sindico) {
                        registrarTransacao(scanner, false);
                    } else {
                        System.out.println("Opção inválida!");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void verExtrato() {
        List<TransacaoFinanceira> transacoes = contasController.listar();
        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }

        System.out.println("\n-- Extrato Financeiro --");
        for (TransacaoFinanceira t : transacoes) {
            String tipo = t.isEntrada() ? "[ENTRADA]" : "[SAIDA]  ";
            System.out.println(tipo + " R$ " + String.format("%.2f", t.getValor()) + " - " + t.getDescricao());
        }
        System.out.printf("Saldo atual: R$ %.2f%n", contasController.calcularSaldo());
    }

    private static void registrarTransacao(Scanner scanner, boolean entrada) {
        String tipo = entrada ? "entrada" : "saída";
        System.out.print("Descrição da " + tipo + ": ");
        String descricao = scanner.nextLine();
        System.out.print("Valor (R$): ");
        float valor = scanner.nextFloat();
        scanner.nextLine();

        contasController.registrarTransacao(new TransacaoFinanceira(valor, new Date(), descricao, entrada, ""));
        System.out.println("Transação registrada com sucesso!");
    }
}
