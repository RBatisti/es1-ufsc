package br.ufsc.condominio.view.ViewDeAvisos;

import br.ufsc.condominio.controller.ControladorDeNotificacões.AvisoController;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Aviso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;

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
        String catStr = scanner.nextLine().trim().toUpperCase();

        CategoriaNotificacao categoria;
        if (catStr.equals("IMPORTANTE")) {
            categoria = CategoriaNotificacao.IMPORTANTE;
        } else if (catStr.equals("NAO_IMPORTANTE")) {
            categoria = CategoriaNotificacao.NAO_IMPORTANTE;
        } else {
            System.out.println("Categoria inválida. Use IMPORTANTE ou NAO_IMPORTANTE.");
            return;
        }

        avisoController.enviarAviso(new Aviso(mensagem, new Date(), categoria));
        System.out.println("Aviso enviado a todos os condôminos!");
    }

    private static void verAvisos(Usuario usuarioLogado) {
        List<Aviso> avisos = (usuarioLogado instanceof br.ufsc.condominio.model.PacoteDeUsuarios.Condomino)
                ? avisoController.listarParaCondomino(usuarioLogado.getCPF(), ((br.ufsc.condominio.model.PacoteDeUsuarios.Condomino) usuarioLogado).getDataCadastro())
                : avisoController.listar(usuarioLogado.getCPF());
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
