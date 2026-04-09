package br.ufsc.condominio.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Aviso extends Notificacao {
    private List<String> cpfsQueViram;

    public Aviso(String mensagem, Date data, CategoriaNotificacao categoria) {
        super(mensagem, data, categoria);
        this.cpfsQueViram = new ArrayList<>();
    }

    public boolean foiVistoPor(String cpf) {
        for (String c : cpfsQueViram) {
            if (c.equals(cpf)) return true;
        }
        return false;
    }

    public void marcarComoVisto(String cpf) {
        if (!foiVistoPor(cpf)) {
            cpfsQueViram.add(cpf);
        }
    }
}
