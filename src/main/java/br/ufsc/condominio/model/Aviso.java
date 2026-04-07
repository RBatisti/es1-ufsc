package br.ufsc.condominio.model;

public class Aviso extends Notificacao {
    public Aviso(String mensagem, java.util.Date data, CategoriaNotificacao categoria) {
        super(mensagem, data, categoria);
    }
}
