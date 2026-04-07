package br.ufsc.condominio.view.ViewDeChamados;

import java.util.Scanner;

public class ViewDeChamados {
    public static void exibirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MÓDULO DE CHAMADOS ---");
            System.out.println("1. Abrir um novo chamado (Acesso: Condômino)");
            System.out.println("2. Acompanhar meus chamados (Acesso: Condômino)");
            System.out.println("3. Visualizar todos os chamados pendentes (Acesso: Síndico)");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Descreva o problema/solicitação: ");
                    String descricao = scanner.nextLine();
                    System.out.println(">> Chamado registrado com sucesso! O síndico será notificado.");
                    break;
                case 2:
                case 3:
                    System.out.println(">> Buscando chamados no sistema...");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
