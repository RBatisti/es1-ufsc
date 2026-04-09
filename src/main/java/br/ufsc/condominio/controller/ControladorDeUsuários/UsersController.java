package br.ufsc.condominio.controller.ControladorDeUsuários;

import br.ufsc.condominio.model.Armazenamento;
import br.ufsc.condominio.model.Usuario;

import java.util.List;

public class UsersController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();

    public Usuario autenticar(String email, String senha) {
        for (Usuario u : armazenamento.getUsuarios()) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    public void cadastrarUsuario(Usuario usuario) {
        armazenamento.adicionarUsuario(usuario);
    }

    public boolean removerUsuario(String cpf) {
        return armazenamento.removerUsuario(cpf);
    }

    public Usuario buscarPorCpf(String cpf) {
        return armazenamento.buscarPorCpf(cpf);
    }

    public List<Usuario> listarUsuarios() {
        return armazenamento.getUsuarios();
    }
}
