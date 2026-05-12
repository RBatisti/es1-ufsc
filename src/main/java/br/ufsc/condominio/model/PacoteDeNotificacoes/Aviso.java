package br.ufsc.condominio.model.PacoteDeNotificacoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Aviso extends Notificacao {
    private List<String> cpfsQueViram;
    private List<String> destinatarios; // vazia = broadcast para todos

    public Aviso(String mensagem, Date data, CategoriaNotificacao categoria) {
        super(mensagem, data, categoria);
        this.cpfsQueViram = new ArrayList<>();
        this.destinatarios = new ArrayList<>();
    }

    public Aviso(String mensagem, Date data, CategoriaNotificacao categoria, List<String> destinatarios) {
        super(mensagem, data, categoria);
        this.cpfsQueViram = new ArrayList<>();
        this.destinatarios = new ArrayList<>(destinatarios);
    }

    public boolean isVisibleTo(String cpf) {
        return destinatarios.isEmpty() || destinatarios.contains(cpf);
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
