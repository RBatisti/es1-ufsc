package br.ufsc.condominio.controller.ControladorDeUsuários;

import br.ufsc.condominio.controller.MainController;
import br.ufsc.condominio.model.Condomino;
import br.ufsc.condominio.model.Genero;
import br.ufsc.condominio.model.Sindico;
import br.ufsc.condominio.model.Usuario;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsersController {
    private MainController mainController;

    private List<Usuario> listaDeUsuarios;

    private void popular() {
        Usuario condomino = new Condomino("Nicolas", "4321", "Nicolau@gmaiu.com",Date.from(Instant.now()), Genero.FEMININO, "1234");
        Usuario sindico = new Sindico("Juliana", "1234", "sindicachata@yahul.com" , Date.from(Instant.now()), Genero.FEMININO, "");

        listaDeUsuarios = new ArrayList<>();
        listaDeUsuarios.add(condomino);
        listaDeUsuarios.add(sindico);
    }

    public UsersController(MainController mainController) {
        popular();

        this.mainController = mainController;
        this.listaDeUsuarios = new ArrayList<>();
    }


    public boolean cadastrarUsuario(Usuario novoUsuario) {
        listaDeUsuarios.add(novoUsuario);
        System.out.println("Usuário cadastrado com sucesso!");
        return true;
    }

    public List<Usuario> listarUsuarios() {
        return listaDeUsuarios;
    }

    public Usuario buscarUsuarioPorCpf(String cpf) {
        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getCPF().equals(cpf)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean atualizarUsuario(String cpf, Usuario dadosAtualizados) {
        Usuario usuarioExistente = buscarUsuarioPorCpf(cpf);

        if (usuarioExistente != null) {
            usuarioExistente.setNome(dadosAtualizados.getNome());
            //usuarioExistente.setEmail(dadosAtualizados.getEmail());
            return true;
        }
        return false;
    }

    public boolean removerUsuario(String cpf) {
        Usuario usuario = buscarUsuarioPorCpf(cpf);
        if (usuario != null) {
            listaDeUsuarios.remove(usuario);
            return true;
        }
        return false;
    }

    public Usuario fazerLogin(String email, String senha) {
        for (Usuario usuario : listaDeUsuarios) {
            if (usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)) {
            }
                return usuario;
        }
        return null;
    }
}