package br.ufsc.condominio.controller.ControladorDeNotificacões;

import br.ufsc.condominio.model.Armazenamento;
import br.ufsc.condominio.model.Aviso;

import java.util.List;

public class AvisoController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();

    public void enviarAviso(Aviso aviso) {
        armazenamento.adicionarAviso(aviso);
    }

    public List<Aviso> listar() {
        return armazenamento.getAvisos();
    }

    public int contarNaoLidos(String cpf) {
        int count = 0;
        for (Aviso a : armazenamento.getAvisos()) {
            if (!a.foiVistoPor(cpf)) {
                count++;
            }
        }
        return count;
    }
}
