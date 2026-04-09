package br.ufsc.condominio.view.ViewDeEspacosCompartilhados;

import br.ufsc.condominio.controller.ControladorDeEspacosCompartilhados.EspacoCompartilhadoController;
import br.ufsc.condominio.model.Condomino;
import br.ufsc.condominio.model.EspacoCompartilhado;
import br.ufsc.condominio.model.Reserva;
import br.ufsc.condominio.model.Sindico;
import br.ufsc.condominio.model.Usuario;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ViewDeEspacosCompartilhados {

    private static final EspacoCompartilhadoController espacoController = new EspacoCompartilhadoController();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat FORMATO_EXIBICAO = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void exibirMenu(Scanner scanner, Usuario usuarioLogado) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- ESPAÇOS COMPARTILHADOS ---");
            System.out.println("1. Ver espaços");
            if (usuarioLogado instanceof Sindico) {
                System.out.println("2. Criar espaço");
                System.out.println("3. Ver todas as reservas");
                System.out.println("4. Cancelar uma reserva");
            } else {
                System.out.println("2. Fazer reserva");
                System.out.println("3. Ver minhas reservas");
                System.out.println("4. Cancelar uma reserva");
            }
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (usuarioLogado instanceof Sindico) {
                switch (opcao) {
                    case 1: verEspacos(usuarioLogado); break;
                    case 2: criarEspaco(scanner); break;
                    case 3: verTodasReservas(); break;
                    case 4: cancelarReservaSindico(scanner); break;
                    case 0: break;
                    default: System.out.println("Opção inválida!");
                }
            } else {
                switch (opcao) {
                    case 1: verEspacos(usuarioLogado); break;
                    case 2: fazerReserva(scanner, (Condomino) usuarioLogado); break;
                    case 3: verMinhasReservas(usuarioLogado); break;
                    case 4: cancelarReservaCondomino(scanner, usuarioLogado); break;
                    case 0: break;
                    default: System.out.println("Opção inválida!");
                }
            }
        }
    }

    private static void verEspacos(Usuario usuarioLogado) {
        List<EspacoCompartilhado> espacos = espacoController.listarEspacos();
        if (espacos.isEmpty()) {
            System.out.println("Nenhum espaço cadastrado.");
            return;
        }

        System.out.println("\n-- Espaços Compartilhados (agora: " + FORMATO_EXIBICAO.format(new Date()) + ") --");
        for (EspacoCompartilhado e : espacos) {
            Reserva ativa = espacoController.getReservaAtiva(e);
            if (ativa != null) {
                if (usuarioLogado instanceof Sindico) {
                    System.out.println("[OCUPADO] " + e.getNome() + " — " + ativa.getCondomino().getNome()
                            + " até " + FORMATO_EXIBICAO.format(ativa.getFim()));
                } else {
                    System.out.println("[OCUPADO] " + e.getNome()
                            + " — disponível a partir de " + FORMATO_EXIBICAO.format(ativa.getFim()));
                }
            } else {
                System.out.println("[DISPONÍVEL] " + e.getNome());
            }
        }
    }

    private static void criarEspaco(Scanner scanner) {
        System.out.print("Nome do espaço: ");
        String nome = scanner.nextLine();
        if (espacoController.buscarPorNome(nome) != null) {
            System.out.println("Já existe um espaço com esse nome.");
            return;
        }
        espacoController.criarEspaco(new EspacoCompartilhado(nome));
        System.out.println("Espaço '" + nome + "' criado com sucesso!");
    }

    private static void fazerReserva(Scanner scanner, Condomino condomino) {
        List<EspacoCompartilhado> espacos = espacoController.listarEspacos();
        if (espacos.isEmpty()) {
            System.out.println("Nenhum espaço cadastrado.");
            return;
        }

        System.out.println("\nEspaços disponíveis:");
        for (EspacoCompartilhado e : espacos) {
            System.out.println("  - " + e.getNome());
        }

        System.out.print("Nome do espaço: ");
        String nome = scanner.nextLine();
        EspacoCompartilhado espaco = espacoController.buscarPorNome(nome);
        if (espaco == null) {
            System.out.println("Espaço não encontrado.");
            return;
        }

        System.out.print("Início (dd/MM/yyyy HH:mm): ");
        Date inicio = parsarDataHora(scanner.nextLine());
        System.out.print("Fim    (dd/MM/yyyy HH:mm): ");
        Date fim = parsarDataHora(scanner.nextLine());

        if (inicio == null || fim == null) {
            System.out.println("Data/hora inválida.");
            return;
        }

        boolean sucesso = espacoController.agendarReserva(espaco, condomino, inicio, fim);
        if (sucesso) {
            System.out.println("Reserva agendada para '" + espaco.getNome() + "' de "
                    + FORMATO_EXIBICAO.format(inicio) + " até " + FORMATO_EXIBICAO.format(fim) + ".");
        } else {
            System.out.println("Não foi possível agendar: horário inválido ou conflito com outra reserva.");
        }
    }

    private static void verMinhasReservas(Usuario usuarioLogado) {
        List<Reserva> reservas = espacoController.listarReservasDoCondomino(usuarioLogado.getCPF());
        if (reservas.isEmpty()) {
            System.out.println("Você não tem reservas.");
            return;
        }

        System.out.println("\n-- Minhas Reservas --");
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            System.out.println("[" + i + "] " + r.getEspacoCompartilhado().getNome()
                    + " | " + FORMATO_EXIBICAO.format(r.getInicio())
                    + " → " + FORMATO_EXIBICAO.format(r.getFim()));
        }
    }

    private static void verTodasReservas() {
        List<Reserva> reservas = espacoController.listarTodasReservas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva registrada.");
            return;
        }

        System.out.println("\n-- Todas as Reservas --");
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            System.out.println("[" + i + "] " + r.getEspacoCompartilhado().getNome()
                    + " | " + r.getCondomino().getNome() + " (CPF: " + r.getCondomino().getCPF() + ")"
                    + " | " + FORMATO_EXIBICAO.format(r.getInicio())
                    + " → " + FORMATO_EXIBICAO.format(r.getFim()));
        }
    }

    private static void cancelarReservaCondomino(Scanner scanner, Usuario usuarioLogado) {
        verMinhasReservas(usuarioLogado);
        List<Reserva> reservas = espacoController.listarReservasDoCondomino(usuarioLogado.getCPF());
        if (reservas.isEmpty()) return;

        System.out.print("Número da reserva a cancelar: ");
        int indice = scanner.nextInt();
        scanner.nextLine();

        if (espacoController.cancelarReservaDoCondomino(indice, usuarioLogado.getCPF())) {
            System.out.println("Reserva cancelada.");
        } else {
            System.out.println("Número inválido.");
        }
    }

    private static void cancelarReservaSindico(Scanner scanner) {
        verTodasReservas();
        List<Reserva> reservas = espacoController.listarTodasReservas();
        if (reservas.isEmpty()) return;

        System.out.print("Número da reserva a cancelar: ");
        int indice = scanner.nextInt();
        scanner.nextLine();

        if (espacoController.cancelarQualquerReserva(indice)) {
            System.out.println("Reserva cancelada.");
        } else {
            System.out.println("Número inválido.");
        }
    }

    private static Date parsarDataHora(String texto) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(texto.trim(), FORMATO);
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
