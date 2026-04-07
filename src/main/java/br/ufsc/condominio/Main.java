package br.ufsc.condominio;

import br.ufsc.condominio.view.ViewDeAvisos.ViewDeAvisos;
import br.ufsc.condominio.view.ViewDeChamados.ViewDeChamados;
import br.ufsc.condominio.view.ViewDeContas.ViewDeContas;
import br.ufsc.condominio.view.ViewDeEspacosCompartilhados.ViewDeEspacosCompartilhados;
import br.ufsc.condominio.view.ViewDeUsuários.ViewDeUsuarios;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE CONDOMÍNIO ===");
            System.out.println("1. Gerenciar Avisos");
            System.out.println("2. Gerenciar Chamados");
            System.out.println("3. Espaços Compartilhados");
            System.out.println("4. Prestação de Contas");
            System.out.println("5. Gerenciar Usuários");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    ViewDeAvisos.exibirMenu(scanner);
                    break;
                case 2:
                    ViewDeChamados.exibirMenu(scanner);
                    break;
                case 3:
                    ViewDeEspacosCompartilhados.exibirMenu(scanner);
                    break;
                case 4:
                    ViewDeContas.exibirMenu(scanner);
                    break;
                case 5:
                    ViewDeUsuarios.exibirMenu(scanner);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }
}