package br.ufsc.condominio.controller.ControladorDeBoletos;

import br.ufsc.condominio.controller.ControladorDeNotificacões.AvisoController;
import br.ufsc.condominio.controller.ControladorDePrestacãoDeContas.ContasController;
import br.ufsc.condominio.model.PacoteDeBoletos.Boleto;
import br.ufsc.condominio.model.PacoteDeBoletos.StatusBoleto;
import br.ufsc.condominio.model.PacoteDeNotificacoes.Aviso;
import br.ufsc.condominio.model.PacoteDeNotificacoes.CategoriaNotificacao;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeUsuarios.Sindico;
import br.ufsc.condominio.model.PacoteDeUsuarios.Usuario;
import br.ufsc.condominio.model.PrestacaoContas.TransacaoFinanceira;
import br.ufsc.condominio.utils.Armazenamento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BoletoController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();
    private final AvisoController avisoController = new AvisoController();
    private final ContasController contasController = new ContasController();

    // Síndico gera boleto para uma unidade
    public void gerarBoleto(String unidade, String mesReferencia, double valorBase, LocalDate dataVencimento) {
        Boleto boleto = new Boleto(unidade, mesReferencia, valorBase, dataVencimento);
        armazenamento.adicionarBoleto(boleto);

        String cpfCondomino = cpfDoCondominoPorUnidade(unidade);
        String cpfSindico = cpfDoSindico();
        List<String> destinatarios = new ArrayList<>();
        if (cpfCondomino != null) destinatarios.add(cpfCondomino);
        if (cpfSindico != null) destinatarios.add(cpfSindico);

        avisoController.enviarAviso(new Aviso(
                "Boleto de " + mesReferencia + " gerado para unidade " + unidade +
                ". Valor: R$ " + String.format("%.2f", valorBase) +
                ". Vencimento: " + dataVencimento,
                new Date(), CategoriaNotificacao.IMPORTANTE, destinatarios));
    }

    // Retorna o boleto aberto da unidade, atualizando multa/juros se necessário
    public Boleto consultarBoletoAberto(String unidade) {
        for (Boleto b : armazenamento.getBoletos()) {
            if (b.getUnidade().equals(unidade) && b.getStatus() != StatusBoleto.PAGO) {
                atualizarStatus(b);
                return b;
            }
        }
        return null;
    }

    // Registra o pagamento do boleto
    public boolean pagar(Boleto boleto) {
        boleto.setStatus(StatusBoleto.PAGO);
        boleto.setDataPagamento(LocalDate.now());
        boleto.setComprovante(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        contasController.registrarTransacao(new TransacaoFinanceira(
                (float) boleto.getValorTotal(), new Date(),
                "Taxa condominial " + boleto.getMesReferencia() + " - Unidade " + boleto.getUnidade(),
                true, "TAXA_CONDOMINIAL"));

        String cpfCondomino = cpfDoCondominoPorUnidade(boleto.getUnidade());
        List<String> destPagamento = new ArrayList<>();
        if (cpfCondomino != null) destPagamento.add(cpfCondomino);
        avisoController.enviarAviso(new Aviso(
                "Pagamento recebido: unidade " + boleto.getUnidade() +
                " quitou R$ " + String.format("%.2f", boleto.getValorTotal()) +
                " referente a " + boleto.getMesReferencia() +
                ". Comprovante: " + boleto.getComprovante(),
                new Date(), CategoriaNotificacao.IMPORTANTE, destPagamento));

        return true;
    }

    // Síndico simula emissão mensal para todos os condôminos
    public int gerarBoletosDoMes(String mesReferencia, LocalDate dataVencimento) {
        double taxa = armazenamento.getTaxaCondominialPadrao();
        int count = 0;
        for (Usuario u : armazenamento.getUsuarios()) {
            if (u instanceof Condomino) {
                gerarBoleto(((Condomino) u).getUnidade(), mesReferencia, taxa, dataVencimento);
                count++;
            }
        }
        return count;
    }

    public List<Boleto> listarTodos() {
        return armazenamento.getBoletos();
    }

    public List<Boleto> listarDoCondomino(String unidade) {
        List<Boleto> resultado = new ArrayList<>();
        for (Boleto b : armazenamento.getBoletos()) {
            if (b.getUnidade().equals(unidade)) {
                resultado.add(b);
            }
        }
        return resultado;
    }

    private String cpfDoCondominoPorUnidade(String unidade) {
        for (Usuario u : armazenamento.getUsuarios()) {
            if (u instanceof Condomino && ((Condomino) u).getUnidade().equals(unidade)) {
                return u.getCPF();
            }
        }
        return null;
    }

    private String cpfDoSindico() {
        for (Usuario u : armazenamento.getUsuarios()) {
            if (u instanceof Sindico) return u.getCPF();
        }
        return null;
    }

    // Recalcula multa/juros e atualiza status se o boleto estiver vencido
    private void atualizarStatus(Boleto b) {
        if (b.getStatus() == StatusBoleto.PAGO) return;
        LocalDate hoje = LocalDate.now();
        if (!hoje.isAfter(b.getDataVencimento())) return;

        long diasAtraso = ChronoUnit.DAYS.between(b.getDataVencimento(), hoje);
        b.setMulta(b.getValorBase() * 0.02);
        b.setJuros(b.getValorBase() * 0.001 * diasAtraso);

        if (diasAtraso > 30) {
            b.setStatus(StatusBoleto.INADIMPLENTE);
            if (!b.isSindicoNotificadoInadimplencia()) {
                String cpfSindico = cpfDoSindico();
                List<String> destinatarios = new ArrayList<>();
                if (cpfSindico != null) destinatarios.add(cpfSindico);
                avisoController.enviarAviso(new Aviso(
                        "INADIMPLÊNCIA: unidade " + b.getUnidade() +
                        " está " + diasAtraso + " dias em atraso. Valor total: R$ " +
                        String.format("%.2f", b.getValorTotal()),
                        new Date(), CategoriaNotificacao.IMPORTANTE, destinatarios));
                b.setSindicoNotificadoInadimplencia(true);
            }
        } else {
            b.setStatus(StatusBoleto.VENCIDO);
        }
    }
}
