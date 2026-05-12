package br.ufsc.condominio.view.ViewDePreAutorizacao;

import br.ufsc.condominio.controller.ControladorDeAcesso.AcessoController;
import br.ufsc.condominio.model.PacoteDeAcesso.SolicitacaoAcesso;
import br.ufsc.condominio.model.PacoteDeAcesso.Visitante;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.utils.Armazenamento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ViewDePreAutorizacao {

    private static final Armazenamento armazenamento = Armazenamento.getInstancia();
    private static final AcessoController acessoController = new AcessoController();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void exibirMenu(Scanner scanner, Condomino condomino) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- PRÉ-AUTORIZAÇÕES ---");
            System.out.println("1. Cadastrar pré-autorização");
            System.out.println("2. Listar pré-autorizações");
            System.out.println("3. Remover pré-autorização");
            System.out.println("4. Responder solicitações da portaria");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: cadastrar(scanner, condomino); break;
                case 2: listar(condomino); break;
                case 3: remover(scanner, condomino); break;
                case 4: responderSolicitacoes(scanner, condomino); break;
                case 0: break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrar(Scanner scanner, Condomino condomino) {
        System.out.print("Nome do visitante: ");
        String nome = scanner.nextLine();
        System.out.print("CPF do visitante: ");
        String cpf = scanner.nextLine();
        System.out.print("Data/hora início (dd/MM/yyyy HH:mm): ");
        String inicioStr = scanner.nextLine();
        System.out.print("Data/hora fim (dd/MM/yyyy HH:mm): ");
        String fimStr = scanner.nextLine();

        LocalDateTime inicio = parsarDataHora(inicioStr);
        LocalDateTime fim = parsarDataHora(fimStr);
        if (inicio == null || fim == null) {
            System.out.println("Data/hora inválida. Pré-autorização não cadastrada.");
            return;
        }

        armazenamento.adicionarAutorizado(new Visitante(nome, cpf, condomino.getUnidade(), inicio, fim));
        System.out.println("Pré-autorização cadastrada com sucesso!");
    }

    private static void listar(Condomino condomino) {
        List<Visitante> todos = armazenamento.getAutorizados();
        System.out.println("\n-- Suas Pré-autorizações --");
        boolean encontrado = false;
        for (Visitante v : todos) {
            if (v.getUnidadeDestino().equals(condomino.getUnidade())) {
                System.out.println(v.getNome() + " | CPF: " + v.getCpf()
                        + " | " + v.getHoraInicio().format(FORMATO)
                        + " até " + v.getHoraFim().format(FORMATO));
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("Nenhuma pré-autorização cadastrada.");
    }

    private static void remover(Scanner scanner, Condomino condomino) {
        System.out.print("CPF do visitante a remover: ");
        String cpf = scanner.nextLine();
        boolean removido = armazenamento.removerAutorizado(cpf, condomino.getUnidade());
        System.out.println(removido ? "Pré-autorização removida." : "Visitante não encontrado.");
    }

    private static void responderSolicitacoes(Scanner scanner, Condomino condomino) {
        List<SolicitacaoAcesso> pendentes = acessoController.listarSolicitacoesPendentesPorUnidade(condomino.getUnidade());
        if (pendentes.isEmpty()) {
            System.out.println("Nenhuma solicitação pendente para sua unidade.");
            return;
        }

        System.out.println("\n-- Solicitações Pendentes da Portaria --");
        for (int i = 0; i < pendentes.size(); i++) {
            SolicitacaoAcesso s = pendentes.get(i);
            System.out.printf("[%d] %s (CPF: %s) | Chegada: %s%n",
                    i + 1, s.getNomeVisitante(), s.getCpfVisitante(),
                    s.getDataHora().format(FORMATO));
        }

        System.out.print("Selecione uma solicitação (0 = voltar): ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        if (idx <= 0 || idx > pendentes.size()) return;

        SolicitacaoAcesso sel = pendentes.get(idx - 1);
        System.out.print("Autorizar entrada? (s/n): ");
        boolean autorizado = scanner.nextLine().equalsIgnoreCase("s");
        String motivo = null;
        if (!autorizado) {
            System.out.print("Motivo da negação: ");
            motivo = scanner.nextLine();
        }
        acessoController.responderSolicitacao(sel, autorizado, motivo);
        System.out.println(autorizado
                ? "Entrada autorizada! O porteiro será informado ao verificar as solicitações."
                : "Entrada negada. Registro salvo.");
    }

    private static LocalDateTime parsarDataHora(String str) {
        try {
            return LocalDateTime.parse(str, FORMATO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
