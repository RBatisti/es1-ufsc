package br.ufsc.condominio.view.ViewDeUsuários;

import br.ufsc.condominio.controller.ControladorDeUsuários.UsersController;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeUsuarios.Genero;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ViewDeUsuarios {

    private static final UsersController usersController = new UsersController();
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void exibirMenu(Scanner scanner, Usuario usuarioLogado) {
        if (!(usuarioLogado instanceof Sindico)) {
            System.out.println("Acesso negado. Apenas o síndico pode gerenciar usuários.");
            return;
        }

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- GERENCIAMENTO DE USUÁRIOS ---");
            System.out.println("1. Cadastrar condômino");
            System.out.println("2. Editar condômino");
            System.out.println("3. Excluir condômino");
            System.out.println("4. Listar todos os usuários");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: cadastrarCondomino(scanner); break;
                case 2: editarCondomino(scanner); break;
                case 3: excluirCondomino(scanner); break;
                case 4: listarUsuarios(); break;
                case 0: break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarCondomino(Scanner scanner) {
        System.out.println("\n-- Cadastrar Condômino --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Unidade (ex: 101): ");
        String unidade = scanner.nextLine();
        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        System.out.print("Gênero (M/F): ");
        String generoStr = scanner.nextLine();

        Date dataNascimento = parsarData(dataStr);
        if (dataNascimento == null) {
            System.out.println("Data inválida. Condômino não cadastrado.");
            return;
        }

        if (usersController.buscarPorCpf(cpf) != null) {
            System.out.println("Já existe um usuário com esse CPF.");
            return;
        }

        Genero genero = generoStr.equalsIgnoreCase("M") ? Genero.MASCULINO : Genero.FEMININO;
        usersController.cadastrarUsuario(new Condomino(nome, cpf, email, dataNascimento, genero, unidade, senha));
        System.out.println("Condômino cadastrado com sucesso!");
    }

    private static void editarCondomino(Scanner scanner) {
        System.out.println("\n-- Editar Condômino --");
        System.out.print("CPF do condômino a editar: ");
        String cpf = scanner.nextLine();

        Usuario usuario = usersController.buscarPorCpf(cpf);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        if (usuario instanceof Sindico) {
            System.out.println("Não é permitido editar o síndico por aqui.");
            return;
        }

        Condomino condomino = (Condomino) usuario;
        System.out.println("Editando: " + condomino.getNome() + " | Deixe em branco para manter o valor atual.");

        System.out.print("Novo nome [" + condomino.getNome() + "]: ");
        String nome = scanner.nextLine();
        System.out.print("Novo e-mail [" + condomino.getEmail() + "]: ");
        String email = scanner.nextLine();
        System.out.print("Nova senha (deixe em branco para não alterar): ");
        String senha = scanner.nextLine();
        System.out.print("Nova unidade [" + condomino.getUnidade() + "]: ");
        String unidade = scanner.nextLine();

        if (!nome.isBlank()) condomino.setNome(nome);
        if (!email.isBlank()) condomino.setEmail(email);
        if (!senha.isBlank()) condomino.setSenha(senha);
        if (!unidade.isBlank()) condomino.setUnidade(unidade);

        System.out.println("Condômino atualizado com sucesso!");
    }

    public static void editarMeuCadastro(Scanner scanner, Condomino condomino) {
        System.out.println("\n--- MEU CADASTRO ---");
        exibirDadosCadastro(condomino);

        System.out.println("\nAtualize seus dados. Deixe em branco para manter o valor atual.");

        System.out.print("Nome [" + condomino.getNome() + "]: ");
        String nome = scanner.nextLine();
        System.out.print("E-mail [" + condomino.getEmail() + "]: ");
        String email = scanner.nextLine();
        System.out.print("Unidade [" + condomino.getUnidade() + "]: ");
        String unidade = scanner.nextLine();

        String dataAtual = condomino.getDataNascimento() != null
                ? FORMATO_DATA.format(condomino.getDataNascimento().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate())
                : "não informada";
        System.out.print("Data de nascimento (dd/MM/yyyy) [" + dataAtual + "]: ");
        String dataStr = scanner.nextLine();

        System.out.print("Gênero (M/F) [" + condomino.getGenero() + "]: ");
        String generoStr = scanner.nextLine();

        System.out.print("Nova senha (deixe em branco para não alterar): ");
        String senha = scanner.nextLine();

        if (!dataStr.isBlank()) {
            Date novaData = parsarData(dataStr);
            if (novaData == null) {
                System.out.println("Data inválida. Data de nascimento não foi alterada.");
            } else {
                condomino.setDataNascimento(novaData);
            }
        }

        if (!generoStr.isBlank()) {
            if (generoStr.equalsIgnoreCase("M")) {
                condomino.setGenero(Genero.MASCULINO);
            } else if (generoStr.equalsIgnoreCase("F")) {
                condomino.setGenero(Genero.FEMININO);
            } else {
                System.out.println("Gênero inválido. Gênero não foi alterado.");
            }
        }

        if (!nome.isBlank()) condomino.setNome(nome);
        if (!email.isBlank()) condomino.setEmail(email);
        if (!unidade.isBlank()) condomino.setUnidade(unidade);
        if (!senha.isBlank()) condomino.setSenha(senha);

        System.out.println("\nDados atualizados com sucesso!");
        exibirDadosCadastro(condomino);
    }

    private static void exibirDadosCadastro(Condomino condomino) {
        String dataNascimento = condomino.getDataNascimento() != null
                ? FORMATO_DATA.format(condomino.getDataNascimento().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate())
                : "não informada";
        System.out.println("\nDados atuais:");
        System.out.println("  Nome: " + condomino.getNome());
        System.out.println("  CPF: " + condomino.getCPF());
        System.out.println("  E-mail: " + condomino.getEmail());
        System.out.println("  Unidade: " + condomino.getUnidade());
        System.out.println("  Data de nascimento: " + dataNascimento);
        System.out.println("  Gênero: " + condomino.getGenero());
    }

    private static void excluirCondomino(Scanner scanner) {
        System.out.println("\n-- Excluir Condômino --");
        System.out.print("CPF do condômino a excluir: ");
        String cpf = scanner.nextLine();

        Usuario usuario = usersController.buscarPorCpf(cpf);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        if (usuario instanceof Sindico) {
            System.out.println("Não é permitido excluir o síndico.");
            return;
        }

        System.out.print("Confirma exclusão de " + usuario.getNome() + "? (s/n): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("s")) {
            usersController.removerUsuario(cpf);
            System.out.println("Condômino removido com sucesso.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = usersController.listarUsuarios();
        System.out.println("\n-- Lista de Usuários --");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        for (Usuario u : usuarios) {
            String tipo = (u instanceof Sindico) ? "Síndico" : "Condômino";
            String unidade = (u instanceof Condomino) ? " | Unidade: " + ((Condomino) u).getUnidade() : "";
            System.out.println("[" + tipo + "] " + u.getNome() + " | CPF: " + u.getCPF() + " | E-mail: " + u.getEmail() + unidade);
        }
    }

    private static Date parsarData(String dataStr) {
        try {
            LocalDate data = LocalDate.parse(dataStr, FORMATO_DATA);
            return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
