package br.ufsc.condominio.view.ViewDeAcesso;

import br.ufsc.condominio.controller.ControladorDeAcesso.AcessoController;
import br.ufsc.condominio.model.PacoteDeAcesso.Acesso;
import br.ufsc.condominio.model.PacoteDeAcesso.SolicitacaoAcesso;
import br.ufsc.condominio.model.PacoteDeAcesso.StatusSolicitacao;
import br.ufsc.condominio.model.PacoteDeAcesso.Visitante;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ViewDeAcesso {

    private static final AcessoController acessoController = new AcessoController();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    public static void exibirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- CONTROLE DE ACESSO ---");
            System.out.println("1. Verificar pré-autorização e registrar entrada");
            System.out.println("2. Acionar condômino (visitante sem pré-autorização)");
            System.out.println("3. Verificar respostas de solicitações");
            System.out.println("4. Registrar saída de visitante");
            System.out.println("5. Ver histórico de acessos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: verificarERegistrar(scanner); break;
                case 2: acionarCondomino(scanner); break;
                case 3: verificarSolicitacoes(scanner); break;
                case 4: registrarSaida(scanner); break;
                case 5: verHistorico(); break;
                case 0: break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void verificarERegistrar(Scanner scanner) {
        System.out.print("CPF do visitante: ");
        String cpf = scanner.nextLine();

        Visitante visitante = acessoController.verificaAutorizacao(cpf);
        if (visitante == null) {
            System.out.println("Sem pré-autorização válida para este visitante.");
        } else {
            System.out.println("Acesso autorizado e registrado!");
            System.out.println("Visitante: " + visitante.getNome() + " | Unidade: " + visitante.getUnidadeDestino());
        }
    }

    private static void acionarCondomino(Scanner scanner) {
        System.out.print("Nome do visitante: ");
        String nome = scanner.nextLine();
        System.out.print("CPF do visitante: ");
        String cpf = scanner.nextLine();
        System.out.print("Unidade de destino: ");
        String unidade = scanner.nextLine();

        SolicitacaoAcesso s = acessoController.criarSolicitacao(nome, cpf, unidade);
        if (s == null) {
            System.out.println("Erro ao criar solicitação.");
        } else {
            System.out.println("Condômino notificado. Aguarde a resposta (até 3 minutos).");
        }
    }

    private static void verificarSolicitacoes(Scanner scanner) {
        List<SolicitacaoAcesso> solicitacoes = acessoController.listarSolicitacoes();
        if (solicitacoes.isEmpty()) {
            System.out.println("Nenhuma solicitação registrada.");
            return;
        }

        System.out.println("\n-- Solicitações de Acesso --");
        for (int i = 0; i < solicitacoes.size(); i++) {
            SolicitacaoAcesso s = solicitacoes.get(i);
            String statusStr = resolverStatusDisplay(s);
            System.out.printf("[%d] %s | Unidade %s | %s | %s%n",
                    i + 1, s.getNomeVisitante(), s.getUnidadeDestino(),
                    s.getDataHora().format(FMT), statusStr);
        }

        System.out.print("Selecione para agir (0 = voltar): ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx <= 0 || idx > solicitacoes.size()) return;

        SolicitacaoAcesso sel = solicitacoes.get(idx - 1);
        agirSobreSolicitacao(scanner, sel);
    }

    private static String resolverStatusDisplay(SolicitacaoAcesso s) {
        switch (s.getStatus()) {
            case PENDENTE:
                return acessoController.emTimeout(s) ? "TIMEOUT" : "PENDENTE";
            case AUTORIZADO:
                return s.isAcessoRegistrado() ? "AUTORIZADO (acesso registrado)" : "AUTORIZADO (pendente registro)";
            case NEGADO:
                return "NEGADO" + (s.getMotivo() != null ? " — " + s.getMotivo() : "");
            default:
                return s.getStatus().toString();
        }
    }

    private static void agirSobreSolicitacao(Scanner scanner, SolicitacaoAcesso sel) {
        if (sel.getStatus() == StatusSolicitacao.AUTORIZADO && !sel.isAcessoRegistrado()) {
            System.out.print("Condômino autorizou. Deseja registrar a entrada? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                acessoController.registrarAcessoManual(sel);
                System.out.println("Acesso registrado!");
            }
        } else if (sel.getStatus() == StatusSolicitacao.PENDENTE && acessoController.emTimeout(sel)) {
            System.out.println("Tempo limite de 3 minutos excedido. Siga o protocolo do condomínio.");
            System.out.print("Deseja registrar a negação de acesso? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Motivo: ");
                String motivo = scanner.nextLine();
                acessoController.responderSolicitacao(sel, false, motivo);
                System.out.println("Negação registrada.");
            }
        } else {
            System.out.println("Nenhuma ação disponível para esta solicitação.");
        }
    }

    private static void registrarSaida(Scanner scanner) {
        List<Acesso> ativos = acessoController.listarAcessosAtivos();
        if (ativos.isEmpty()) {
            System.out.println("Nenhum visitante ativo no condomínio.");
            return;
        }

        System.out.println("\n-- Visitantes no Condomínio --");
        for (int i = 0; i < ativos.size(); i++) {
            Acesso a = ativos.get(i);
            System.out.printf("[%d] %s | Unidade %s | Entrada: %s%n",
                    i + 1, a.getVisitante().getNome(),
                    a.getVisitante().getUnidadeDestino(),
                    a.getDataHora().format(FMT));
        }

        System.out.print("Selecione para registrar saída (0 = voltar): ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx <= 0 || idx > ativos.size()) return;

        acessoController.registrarSaida(ativos.get(idx - 1));
        System.out.println("Saída registrada!");
    }

    public static void verHistorico() {
        List<Acesso> acessos = acessoController.listarAcessos();
        if (acessos.isEmpty()) {
            System.out.println("Nenhum acesso registrado.");
            return;
        }
        System.out.println("\n-- Histórico de Acessos --");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Acesso a : acessos) {
            String saida = a.getDataHoraSaida() != null
                    ? " | Saída: " + a.getDataHoraSaida().format(fmt)
                    : " | Ainda no condomínio";
            System.out.printf("Entrada: %s | %s | Unidade %s | Porteiro: %s%s%n",
                    a.getDataHora().format(fmt), a.getVisitante().getNome(),
                    a.getVisitante().getUnidadeDestino(), a.getPorteiro().getNome(), saida);
        }
    }
}
