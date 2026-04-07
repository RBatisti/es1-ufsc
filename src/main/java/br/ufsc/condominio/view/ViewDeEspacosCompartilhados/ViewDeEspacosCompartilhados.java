package br.ufsc.condominio.view.ViewDeEspacosCompartilhados;

import java.util.Scanner;

public class ViewDeEspacosCompartilhados {
    public static void exibirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- ESPAÇOS COMPARTILHADOS ---");
            System.out.println("1. Ver espaços disponíveis e regras");
            System.out.println("2. Solicitar reserva de espaço");
            System.out.println("3. Ver minhas reservas");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println(">> Espaços: 1. Salão de Festas, 2. Churrasqueira, 3. Quadra");
                    break;
                case 2:
                    System.out.print("Qual espaço deseja reservar? ");
                    String espaco = scanner.nextLine();
                    System.out.print("Digite a data (DD/MM/AAAA): ");
                    String data = scanner.nextLine();
                    System.out.println(">> Solicitação para " + espaco + " no dia " + data + " enviada para aprovação.");
                    break;
                case 3:
                    System.out.println(">> Listando suas reservas agendadas...");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}