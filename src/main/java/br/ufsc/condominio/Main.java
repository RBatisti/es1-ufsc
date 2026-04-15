package br.ufsc.condominio;

import br.ufsc.condominio.controller.ControladorDeNotificacões.AvisoController;
import br.ufsc.condominio.controller.ControladorDeNotificacões.ChamadoController;
import br.ufsc.condominio.controller.ControladorDeUsuários.UsersController;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.view.ViewDeAvisos.ViewDeAvisos;
import br.ufsc.condominio.view.ViewDeChamados.ViewDeChamados;
import br.ufsc.condominio.view.ViewDeContas.ViewDeContas;
import br.ufsc.condominio.view.ViewDeEspacosCompartilhados.ViewDeEspacosCompartilhados;
import br.ufsc.condominio.view.ViewDeUsuários.ViewDeUsuarios;

import java.util.Scanner;

public class Main {

    private static final UsersController usersController = new UsersController();
    private static final AvisoController avisoController = new AvisoController();
    private static final ChamadoController chamadoController = new ChamadoController();

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

            String perfil = (usuarioLogado instanceof Sindico) ? "Síndico" : "Condômino";
            System.out.println("\nBem-vindo(a), " + usuarioLogado.getNome() + " [" + perfil + "]!");

            int opcao = -1;
            while (opcao != 0) {
                exibirAlertas(usuarioLogado);

                System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE CONDOMÍNIO ===");
                System.out.println("1. Avisos");
                System.out.println("2. Chamados");
                System.out.println("3. Espaços Compartilhados");
                System.out.println("4. Prestação de Contas");
                if (usuarioLogado instanceof Sindico) {
                    System.out.println("5. Gerenciar Usuários");
                }
                System.out.println("9. Logout");
                System.out.println("0. Sair do Sistema");
                System.out.print("Escolha uma opção: ");

                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1: ViewDeAvisos.exibirMenu(scanner, usuarioLogado); break;
                    case 2: ViewDeChamados.exibirMenu(scanner, usuarioLogado); break;
                    case 3: ViewDeEspacosCompartilhados.exibirMenu(scanner, usuarioLogado); break;
                    case 4: ViewDeContas.exibirMenu(scanner, usuarioLogado); break;
                    case 5: ViewDeUsuarios.exibirMenu(scanner, usuarioLogado); break;
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
        scanner.close();
    }

    private static void exibirAlertas(Usuario usuarioLogado) {
        int avisosNaoLidos = avisoController.contarNaoLidos(usuarioLogado.getCPF());
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
