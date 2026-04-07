package br.ufsc.condominio.view.ViewDeContas;

import java.util.Scanner;

public class ViewDeContas {
    public static void exibirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- PRESTAÇÃO DE CONTAS ---");
            System.out.println("1. Ver relatório de despesas do mês atual");
            System.out.println("2. Ver balanço anual");
            System.out.println("3. Registrar nova despesa do condomínio (Acesso: Síndico)");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                case 2:
                    System.out.println(">> Gerando relatório de transparência financeira...");
                    // TODO: Buscar dados de gastos
                    break;
                case 3:
                    System.out.print("Descrição da despesa (ex: Manutenção Elevador): ");
                    String despesa = scanner.nextLine();
                    System.out.print("Valor (R$): ");
                    double valor = scanner.nextDouble();
                    scanner.nextLine(); // Limpar buffer
                    System.out.println(">> Despesa de R$" + valor + " referente a '" + despesa + "' registrada.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}