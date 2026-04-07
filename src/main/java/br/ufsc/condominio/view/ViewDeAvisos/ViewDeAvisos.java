package br.ufsc.condominio.view.ViewDeAvisos;

import java.util.Scanner;

public class ViewDeAvisos {
    public static void exibirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MÓDULO DE AVISOS ---");
            System.out.println("1. Enviar novo aviso (Acesso: Síndico)");
            System.out.println("2. Visualizar mural de avisos");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o título do aviso: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Digite a mensagem: ");
                    String mensagem = scanner.nextLine();
                    System.out.println(">> Aviso '" + titulo + "' enviado com sucesso aos condôminos!");
                    // TODO: Chamar o Controller correspondente aqui
                    break;
                case 2:
                    System.out.println(">> Listando avisos recentes...");
                    // TODO: Chamar Controller para buscar lista de avisos
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}