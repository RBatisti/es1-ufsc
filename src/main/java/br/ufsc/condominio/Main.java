package br.ufsc.condominio;

import br.ufsc.condominio.controller.ControladorDeAcesso.AcessoController;
import br.ufsc.condominio.controller.ControladorDeNotificacões.AvisoController;
import br.ufsc.condominio.controller.ControladorDeNotificacões.ChamadoController;
import br.ufsc.condominio.controller.ControladorDeUsuários.UsersController;
import br.ufsc.condominio.model.PacoteDeAcesso.Acesso;
import br.ufsc.condominio.model.PacoteDeAcesso.SolicitacaoAcesso;
import br.ufsc.condominio.model.PacoteDeAcesso.StatusSolicitacao;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeUsuarios.Porteiro;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.utils.Armazenamento;

import java.time.format.DateTimeFormatter;
import br.ufsc.condominio.view.ViewDeAcesso.ViewDeAcesso;
import br.ufsc.condominio.view.ViewDeAvisos.ViewDeAvisos;
import br.ufsc.condominio.view.ViewDeChamados.ViewDeChamados;
import br.ufsc.condominio.view.ViewDeContas.ViewDeContas;
import br.ufsc.condominio.view.ViewDeEspacosCompartilhados.ViewDeEspacosCompartilhados;
import br.ufsc.condominio.view.ViewDeBoletos.ViewDeBoletos;
import br.ufsc.condominio.view.ViewDePreAutorizacao.ViewDePreAutorizacao;
import br.ufsc.condominio.view.ViewDeUsuários.ViewDeUsuarios;

import java.util.Scanner;

public class Main {

    private static final UsersController usersController = new UsersController();
    private static final AvisoController avisoController = new AvisoController();
    private static final ChamadoController chamadoController = new ChamadoController();
    private static final AcessoController acessoController = new AcessoController();
    private static final Armazenamento armazenamento = Armazenamento.getInstancia();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            Usuario usuarioLogado = null;
            while (usuarioLogado == null) {
                System.out.println("\n=== LOGIN ===");
                System.out.print("E-mail: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                usuarioLogado = usersController.autenticar(email, senha);
                if (usuarioLogado == null) {
                    System.out.println("E-mail ou senha incorretos. Tente novamente.");
                }
            }
            armazenamento.setCurrentUser(usuarioLogado);

            String perfil = (usuarioLogado instanceof Sindico) ? "Síndico"
                    : (usuarioLogado instanceof Porteiro) ? "Porteiro" : "Condômino";
            System.out.println("\nBem-vindo(a), " + usuarioLogado.getNome() + " [" + perfil + "]!");

            int opcao = -1;
            while (opcao != 0) {
                exibirAlertas(usuarioLogado);

                System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE CONDOMÍNIO ===");
                if (usuarioLogado instanceof Porteiro) {
                    System.out.println("1. Controle de Acesso");
                } else {
                    System.out.println("1. Avisos");
                    System.out.println("2. Chamados");
                    System.out.println("3. Espaços Compartilhados");
                    System.out.println("4. Prestação de Contas");
                    if (usuarioLogado instanceof Sindico) {
                        System.out.println("5. Gerenciar Usuários");
                        System.out.println("8. Histórico de Acessos");
                    }
                    if (usuarioLogado instanceof Condomino) {
                        System.out.println("6. Pré-autorizações de Visitantes");
                    }
                    System.out.println("7. Boletos");
                }
                System.out.println("9. Logout");
                System.out.println("0. Sair do Sistema");
                System.out.print("Escolha uma opção: ");

                opcao = scanner.nextInt();
                scanner.nextLine();

                if (usuarioLogado instanceof Porteiro) {
                    switch (opcao) {
                        case 1: ViewDeAcesso.exibirMenu(scanner); break;
                        case 9:
                            System.out.println("Logout realizado. Até logo, " + usuarioLogado.getNome() + "!");
                            opcao = 0;
                            break;
                        case 0:
                            System.out.println("Encerrando o sistema...");
                            executando = false;
                            break;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                } else {
                    switch (opcao) {
                        case 1: ViewDeAvisos.exibirMenu(scanner, usuarioLogado); break;
                        case 2: ViewDeChamados.exibirMenu(scanner, usuarioLogado); break;
                        case 3: ViewDeEspacosCompartilhados.exibirMenu(scanner, usuarioLogado); break;
                        case 4: ViewDeContas.exibirMenu(scanner, usuarioLogado); break;
                        case 5: ViewDeUsuarios.exibirMenu(scanner, usuarioLogado); break;
                        case 6:
                            if (usuarioLogado instanceof Condomino) {
                                ViewDePreAutorizacao.exibirMenu(scanner, (Condomino) usuarioLogado);
                            } else {
                                System.out.println("Opção inválida! Tente novamente.");
                            }
                            break;
                        case 7: ViewDeBoletos.exibirMenu(scanner, usuarioLogado); break;
                        case 8:
                            if (usuarioLogado instanceof Sindico) verHistoricoAcessosSindico();
                            else System.out.println("Opção inválida! Tente novamente.");
                            break;
                        case 9:
                            System.out.println("Logout realizado. Até logo, " + usuarioLogado.getNome() + "!");
                            opcao = 0;
                            break;
                        case 0:
                            System.out.println("Encerrando o sistema...");
                            executando = false;
                            break;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                }
            }
        }
        scanner.close();
    }

    private static void verHistoricoAcessosSindico() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        java.util.List<Acesso> acessos = acessoController.listarAcessos();
        System.out.println("\n-- Histórico de Acessos --");
        if (acessos.isEmpty()) {
            System.out.println("Nenhum acesso registrado.");
        } else {
            for (Acesso a : acessos) {
                String saida = a.getDataHoraSaida() != null
                        ? " | Saída: " + a.getDataHoraSaida().format(fmt)
                        : " | Ainda no condomínio";
                System.out.printf("Entrada: %s | %s | Unidade %s | Porteiro: %s%s%n",
                        a.getDataHora().format(fmt), a.getVisitante().getNome(),
                        a.getVisitante().getUnidadeDestino(), a.getPorteiro().getNome(), saida);
            }
        }

        java.util.List<SolicitacaoAcesso> negadas = new java.util.ArrayList<>();
        for (SolicitacaoAcesso s : acessoController.listarSolicitacoes()) {
            if (s.getStatus() == StatusSolicitacao.NEGADO) negadas.add(s);
        }
        if (!negadas.isEmpty()) {
            System.out.println("\n-- Tentativas de Acesso Negadas --");
            for (SolicitacaoAcesso s : negadas) {
                System.out.printf("%s | %s | Unidade %s | Motivo: %s%n",
                        s.getDataHora().format(fmt), s.getNomeVisitante(),
                        s.getUnidadeDestino(),
                        s.getMotivo() != null ? s.getMotivo() : "não informado");
            }
        }
    }

    private static void exibirAlertas(Usuario usuarioLogado) {
        if (usuarioLogado instanceof Porteiro) return;

        java.util.Date dataCadastro = (usuarioLogado instanceof br.ufsc.condominio.model.PacoteDeUsuarios.Condomino)
                ? ((br.ufsc.condominio.model.PacoteDeUsuarios.Condomino) usuarioLogado).getDataCadastro()
                : null;
        int avisosNaoLidos = avisoController.contarNaoLidos(usuarioLogado.getCPF(), dataCadastro);
        if (avisosNaoLidos > 0) {
            System.out.println(">>> Você tem " + avisosNaoLidos + " aviso(s) não lido(s)! <<<");
        }

        if (usuarioLogado instanceof Sindico) {
            int chamadosNovos = chamadoController.contarNaoVistosPeloSindico();
            if (chamadosNovos > 0) {
                System.out.println(">>> Há " + chamadosNovos + " chamado(s) novo(s) aguardando sua atenção! <<<");
            }
        }
    }
}
