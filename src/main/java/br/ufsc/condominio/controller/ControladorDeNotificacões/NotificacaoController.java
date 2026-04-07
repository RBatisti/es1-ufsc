package br.ufsc.condominio.controller.ControladorDeNotificacões;

import br.ufsc.condominio.controller.MainController;
import br.ufsc.condominio.model.Notificacao; // Você precisará criar este Model
import br.ufsc.condominio.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoController {

    // Lista que simula o banco de dados armazenando todos os avisos e notificações
    private List<Notificacao> muralENotificacoes;
    private MainController mainController;

    // Construtor
    public NotificacaoController(MainController mainController) {
        this.mainController = mainController;
        this.muralENotificacoes = new ArrayList<>();
    }

    // ==========================================
    // CRIAÇÃO E ENVIO DE NOTIFICAÇÕES
    // ==========================================

    // Cria um aviso para TODOS os moradores (ex: "Manutenção na piscina amanhã")
    public void enviarAvisoGeral(String titulo, String mensagem) {
        // Passamos null no destinatário para indicar que é para todos
        Notificacao aviso = new Notificacao(mensagem, null, null);
        muralENotificacoes.add(aviso);
        System.out.println("Aviso geral publicado: " + titulo);
    }

    // Cria uma notificação direta para um morador (ex: "Sua encomenda chegou")
    public void enviarNotificacaoIndividual(String titulo, String mensagem, Usuario destinatario) {
        Notificacao notificacao = new Notificacao(titulo, null, null);
        muralENotificacoes.add(notificacao);
        System.out.println("Notificacao enviada para: " + destinatario.getNome());
    }

    // ==========================================
    // CONSULTA E LEITURA (READ)
    // ==========================================

    // Retorna apenas os avisos que são para o condomínio inteiro (destinatario == null)
    public List<Notificacao> listarAvisosGerais() {
        List<Notificacao> avisosGlobais = new ArrayList<>();

//        for (Notificacao n : muralENotificacoes) {
//            if (n.getDestinatario() == null) {
//                avisosGlobais.add(n);
//            }
//        }
        return avisosGlobais;
    }

    // Retorna as mensagens de um morador específico + os avisos gerais
    public List<Notificacao> listarCaixaDeEntrada(Usuario morador) {
        List<Notificacao> caixaDeEntrada = new ArrayList<>();

        for (Notificacao n : muralENotificacoes) {
            // Pega avisos gerais OU notificações direcionadas exatamente para este morador
//            if (n.getDestinatario() == null || n.getDestinatario().equals(morador)) {
//                caixaDeEntrada.add(n);
//            }
        }
        return caixaDeEntrada;
    }

    // ==========================================
    // ATUALIZAÇÃO E REMOÇÃO
    // ==========================================

    // Marca uma notificação como lida pelo usuário
    public void marcarComoLida(Notificacao notificacao) {
//        if (muralENotificacoes.contains(notificacao)) {
//            notificacao.setLida(true);
//        }
    }

    // Apaga um aviso (útil para o síndico limpar o mural)
    public boolean removerNotificacao(Notificacao notificacao) {
        return muralENotificacoes.remove(notificacao);
    }
}