package br.ufsc.condominio.view.ViewDeAvisos;

import br.ufsc.condominio.controller.ControladorDeNotificacões.AvisoController;
import br.ufsc.condominio.model.Aviso;
import br.ufsc.condominio.model.CategoriaNotificacao;
import br.ufsc.condominio.model.Sindico;
import br.ufsc.condominio.model.Usuario;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ViewDeAvisos {

    private static final AvisoController avisoController = new AvisoController();

    public static void exibirMenu(Scanner scanner, Usuario usuarioLogado) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MÓDULO DE AVISOS ---");
            if (usuarioLogado instanceof Sindico) {
                System.out.println("1. Enviar novo aviso");
            }
            System.out.println("2. Ver histórico de avisos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (usuarioLogado instanceof Sindico) {
                        enviarAviso(scanner);
                    } else {
                        System.out.println("Acesso negado.");
                    }
                    break;
                case 2:
                    verAvisos(usuarioLogado);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void enviarAviso(Scanner scanner) {
        System.out.print("Mensagem do aviso: ");
        String mensagem = scanner.nextLine();
        System.out.print("Categoria (IMPORTANTE / NAO_IMPORTANTE): ");
        String catStr = scanner.nextLine();

        CategoriaNotificacao categoria;
        if (catStr.equalsIgnoreCase("IMPORTANTE")) {
            categoria = CategoriaNotificacao.IMPORTANTE;
        } else {
            categoria = CategoriaNotificacao.NAO_IMPORTANTE;
        }

        avisoController.enviarAviso(new Aviso(mensagem, new Date(), categoria));
        System.out.println("Aviso enviado a todos os condôminos!");
    }

    private static void verAvisos(Usuario usuarioLogado) {
        List<Aviso> avisos = avisoController.listar();
        if (avisos.isEmpty()) {
            System.out.println("Nenhum aviso cadastrado.");
            return;
        }

        System.out.println("\n-- Histórico de Avisos --");
        for (Aviso a : avisos) {
            String lido = a.foiVistoPor(usuarioLogado.getCPF()) ? "[LIDO]    " : "[NAO LIDO]";
            System.out.println(lido + " [" + a.getCategoria() + "] " + a.getMensagem());
            a.marcarComoVisto(usuarioLogado.getCPF());
        }
    }
}
