package br.ufsc.condominio.view.ViewDeChamados;

import br.ufsc.condominio.controller.ControladorDeNotificacões.ChamadoController;
import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Chamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.StatusChamado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.em_andamento;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.nao_iniciado;
import br.ufsc.condominio.model.PacoteDeNotificacoes.State.pronto;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ViewDeChamados {

    private static final ChamadoController chamadoController = new ChamadoController();

    public static void exibirMenu(Scanner scanner, Usuario usuarioLogado) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MÓDULO DE CHAMADOS ---");
            if (usuarioLogado instanceof Sindico) {
                System.out.println("1. Ver todos os chamados");
                System.out.println("2. Alterar status de um chamado");
                System.out.println("3. Filtrar chamados por status");
            } else {
                System.out.println("1. Abrir novo chamado");
                System.out.println("2. Ver meus chamados");
            }
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (usuarioLogado instanceof Sindico) {
                switch (opcao) {
                    case 1:
                        verTodosChamados();
                        break;
                    case 2:
                        alterarStatusChamado(scanner);
                        break;
                    case 3:
                        filtrarChamadosPorStatus(scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } else {
                switch (opcao) {
                    case 1:
                        abrirChamado(scanner, usuarioLogado);
                        break;
                    case 2:
                        verMeusChamados(usuarioLogado);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        }
    }

    private static void abrirChamado(Scanner scanner, Usuario usuarioLogado) {
        System.out.print("Descreva o problema: ");
        String descricao = scanner.nextLine();
        System.out.print("Categoria (IMPORTANTE / NAO_IMPORTANTE): ");
        String catStr = scanner.nextLine();

        CategoriaNotificacao categoria;
        if (catStr.equalsIgnoreCase("IMPORTANTE")) {
            categoria = CategoriaNotificacao.IMPORTANTE;
        } else {
            categoria = CategoriaNotificacao.NAO_IMPORTANTE;
        }

        chamadoController.abrirChamado(new Chamado(descricao, new Date(), categoria, usuarioLogado.getCPF()));
        System.out.println("Chamado registrado! O síndico será notificado.");
    }

    private static void verMeusChamados(Usuario usuarioLogado) {
        List<Chamado> chamados = chamadoController.listarDoCondomino(usuarioLogado.getCPF());
        if (chamados.isEmpty()) {
            System.out.println("Você não tem chamados registrados.");
            return;
        }

        System.out.println("\n-- Meus Chamados --");
        for (Chamado c : chamados) {
            String visto = c.isVisualizadoPeloSindico() ? "[Visto pelo síndico]" : "[Aguardando síndico]";
            System.out.println(visto + " [" + c.getStatusChamado() + "] " + c.getMensagem());
        }
    }

    private static void verTodosChamados() {
        List<Chamado> chamados = chamadoController.listarTodos();
        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado.");
            chamadoController.marcarTodosComoVistosPeloSindico();
            return;
        }

        System.out.println("\n-- Todos os Chamados --");
        for (int i = 0; i < chamados.size(); i++) {
            Chamado c = chamados.get(i);
            String novo = c.isVisualizadoPeloSindico() ? "" : " [NOVO]";
            System.out.println("[" + i + "] [" + c.getStatusChamado() + "]" + novo + " CPF: " + c.getCpfCondomino() + " - " + c.getMensagem());
        }
        chamadoController.marcarTodosComoVistosPeloSindico();
    }

    private static void filtrarChamadosPorStatus(Scanner scanner) {
        System.out.println("\nFiltrar por status:");
        System.out.println("1. NAO_INICIADO");
        System.out.println("2. EM_ANDAMENTO");
        System.out.println("3. PRONTO");
        System.out.print("Escolha: ");
        int opcaoStatus = scanner.nextInt();
        scanner.nextLine();

        StatusChamado statusFiltro;
        switch (opcaoStatus) {
            case 1: statusFiltro = new nao_iniciado(); break;
            case 2: statusFiltro = new em_andamento(); break;
            case 3: statusFiltro = new pronto(); break;
            default:
                System.out.println("Status inválido.");
                return;
        }

        List<Chamado> chamados = chamadoController.listarPorStatus(statusFiltro.getClass());
        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado com o status " + statusFiltro + ".");
            return;
        }

        System.out.println("\n-- Chamados com status " + statusFiltro + " --");
        for (Chamado c : chamados) {
            String novo = c.isVisualizadoPeloSindico() ? "" : " [NOVO]";
            System.out.println("[" + c.getStatusChamado() + "]" + novo + " CPF: " + c.getCpfCondomino() + " - " + c.getMensagem());
        }
    }

    private static void alterarStatusChamado(Scanner scanner) {
        List<Chamado> chamados = chamadoController.listarTodos();
        if (chamados.isEmpty()) {
            System.out.println("Nenhum chamado registrado.");
            return;
        }

        System.out.println("\n-- Chamados --");
        for (int i = 0; i < chamados.size(); i++) {
            Chamado c = chamados.get(i);
            System.out.println("[" + i + "] [" + c.getStatusChamado() + "] CPF: " + c.getCpfCondomino() + " - " + c.getMensagem());
        }

        System.out.print("Número do chamado: ");
        int indice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Avançar status do chamado? (S/N): ");
        String opcaoStatus = scanner.nextLine().trim().toUpperCase();

        // Verifica se o usuário de fato escolheu "S"
        if (opcaoStatus.equals("S")) {

            try {
                boolean sucesso = chamadoController.alterarStatus(indice);
                if (sucesso) {
                    System.out.println("Status atualizado!");
                } else {
                    System.out.println("Número de chamado inválido");
                }
            } catch (IllegalStateException e) {
                System.out.println(e);
            }
        } else if (opcaoStatus.equals("N")) {
            System.out.println("Operação cancelada.");
        } else {
            System.out.println("Opção inválida.");
        }
}
}
