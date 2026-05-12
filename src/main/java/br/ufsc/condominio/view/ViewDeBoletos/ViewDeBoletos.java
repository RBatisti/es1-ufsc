package br.ufsc.condominio.view.ViewDeBoletos;

import br.ufsc.condominio.controller.ControladorDeBoletos.BoletoController;
import br.ufsc.condominio.model.PacoteDeBoletos.Boleto;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.utils.Armazenamento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ViewDeBoletos {

    private static final BoletoController boletoController = new BoletoController();
    private static final Armazenamento armazenamento = Armazenamento.getInstancia();
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void exibirMenu(Scanner scanner, Usuario usuarioLogado) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- BOLETOS ---");
            if (usuarioLogado instanceof Sindico) {
                System.out.println("1. Gerar boleto para unidade");
                System.out.println("2. Ver todos os boletos");
                System.out.println("3. Emitir boletos do mês (todos os condôminos)");
                System.out.println("4. Definir taxa condominial padrão (atual: R$ "
                        + String.format("%.2f", armazenamento.getTaxaCondominialPadrao()) + ")");
            } else {
                System.out.println("1. Ver meu boleto atual");
                System.out.println("2. Ver histórico de boletos");
            }
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (usuarioLogado instanceof Sindico) {
                switch (opcao) {
                    case 1: gerarBoleto(scanner); break;
                    case 2: listarTodos(); break;
                    case 3: emitirBoletosDoMes(scanner); break;
                    case 4: definirTaxaPadrao(scanner); break;
                    case 0: break;
                    default: System.out.println("Opção inválida!");
                }
            } else {
                Condomino condomino = (Condomino) usuarioLogado;
                switch (opcao) {
                    case 1: verEPagarBoleto(scanner, condomino); break;
                    case 2: verHistorico(condomino); break;
                    case 0: break;
                    default: System.out.println("Opção inválida!");
                }
            }
        }
    }

    private static void gerarBoleto(Scanner scanner) {
        System.out.print("Unidade: ");
        String unidade = scanner.nextLine();
        System.out.print("Mês de referência (MM/yyyy): ");
        String mes = scanner.nextLine();
        System.out.print("Valor base (R$): ");
        double valor = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Data de vencimento (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();

        LocalDate vencimento = parsarData(dataStr);
        if (vencimento == null) {
            System.out.println("Data inválida. Boleto não gerado.");
            return;
        }

        boletoController.gerarBoleto(unidade, mes, valor, vencimento);
        System.out.println("Boleto gerado e condômino notificado!");
    }

    private static void emitirBoletosDoMes(Scanner scanner) {
        System.out.print("Mês de referência (MM/yyyy): ");
        String mes = scanner.nextLine();
        System.out.print("Data de vencimento (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();

        LocalDate vencimento = parsarData(dataStr);
        if (vencimento == null) {
            System.out.println("Data inválida. Operação cancelada.");
            return;
        }

        System.out.printf("Confirma emissão para todos os condôminos? Taxa padrão: R$ %.2f (s/n): ",
                armazenamento.getTaxaCondominialPadrao());
        if (!scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Operação cancelada.");
            return;
        }

        int total = boletoController.gerarBoletosDoMes(mes, vencimento);
        System.out.println(total + " boleto(s) emitido(s) e condôminos notificados!");
    }

    private static void definirTaxaPadrao(Scanner scanner) {
        System.out.printf("Taxa atual: R$ %.2f%n", armazenamento.getTaxaCondominialPadrao());
        System.out.print("Novo valor (R$): ");
        try {
            double novo = scanner.nextDouble();
            scanner.nextLine();
            if (novo <= 0) {
                System.out.println("Valor inválido.");
                return;
            }
            armazenamento.setTaxaCondominialPadrao(novo);
            System.out.printf("Taxa condominial padrão atualizada para R$ %.2f%n", novo);
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Valor inválido.");
        }
    }

    private static void listarTodos() {
        List<Boleto> boletos = boletoController.listarTodos();
        if (boletos.isEmpty()) {
            System.out.println("Nenhum boleto cadastrado.");
            return;
        }
        System.out.println("\n-- Todos os Boletos --");
        for (Boleto b : boletos) {
            System.out.printf("[%s] Unidade %-5s | %s | R$ %8.2f | Venc: %s%n",
                    b.getStatus(), b.getUnidade(), b.getMesReferencia(),
                    b.getValorTotal(), b.getDataVencimento().format(FORMATO_DATA));
        }
    }

    private static void verEPagarBoleto(Scanner scanner, Condomino condomino) {
        Boleto boleto = boletoController.consultarBoletoAberto(condomino.getUnidade());
        if (boleto == null) {
            System.out.println("Nenhum boleto em aberto.");
            return;
        }

        exibirDetalhes(boleto);

        System.out.print("Deseja pagar agora? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            boletoController.pagar(boleto);
            System.out.println("Pagamento registrado!");
            System.out.println("Comprovante: " + boleto.getComprovante());
        }
    }

    private static void verHistorico(Condomino condomino) {
        List<Boleto> boletos = boletoController.listarDoCondomino(condomino.getUnidade());
        if (boletos.isEmpty()) {
            System.out.println("Nenhum boleto encontrado.");
            return;
        }
        System.out.println("\n-- Seu Histórico de Boletos --");
        for (Boleto b : boletos) {
            String pago = b.getComprovante() != null ? " | Comprovante: " + b.getComprovante() : "";
            System.out.printf("[%s] %s | R$ %.2f%s%n",
                    b.getStatus(), b.getMesReferencia(), b.getValorTotal(), pago);
        }
    }

    private static void exibirDetalhes(Boleto boleto) {
        System.out.println("\n-- Boleto " + boleto.getMesReferencia() + " --");
        System.out.printf("Valor base:  R$ %.2f%n", boleto.getValorBase());
        if (boleto.getMulta() > 0)  System.out.printf("Multa (2%%): R$ %.2f%n", boleto.getMulta());
        if (boleto.getJuros() > 0)  System.out.printf("Juros:      R$ %.2f%n", boleto.getJuros());
        System.out.printf("Total:       R$ %.2f%n", boleto.getValorTotal());
        System.out.println("Vencimento: " + boleto.getDataVencimento().format(FORMATO_DATA));
        System.out.println("Status:     " + boleto.getStatus());
    }

    private static LocalDate parsarData(String str) {
        try {
            return LocalDate.parse(str, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
