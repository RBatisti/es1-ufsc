package br.ufsc.condominio.view.ViewDeUsuários;

import java.util.Scanner;

public class ViewDeUsuarios {
    public static void exibirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- GERENCIAMENTO DE USUÁRIOS ---");
            System.out.println("1. Cadastrar novo usuário");
            System.out.println("2. Editar usuário existente");
            System.out.println("3. Excluir usuário");
            System.out.println("4. Listar todos os usuários");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome do novo usuário: ");
                    String nome = scanner.nextLine();
                    System.out.print("É um Síndico ou Condômino? ");
                    String perfil = scanner.nextLine();
                    System.out.println(">> Usuário " + nome + " criado com o perfil: " + perfil);
                    break;
                case 2:
                    System.out.print("Digite o ID ou Nome do usuário que deseja editar: ");
                    String userEdit = scanner.nextLine();
                    System.out.println(">> Abrindo modo de edição para: " + userEdit);
                    break;
                case 3:
                    System.out.print("Digite o ID ou Nome do usuário que deseja excluir: ");
                    String userDel = scanner.nextLine();
                    System.out.println(">> Usuário " + userDel + " removido do sistema.");
                    break;
                case 4:
                    System.out.println(">> Lista de usuários cadastrados:");
                    // TODO: Chamar controller para listar todos
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}