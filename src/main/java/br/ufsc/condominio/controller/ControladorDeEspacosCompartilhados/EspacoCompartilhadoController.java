package br.ufsc.condominio.controller.ControladorDeEspacosCompartilhados;

import br.ufsc.condominio.utils.Armazenamento;
import br.ufsc.condominio.model.PacoteDeUsuarios.Condomino;
import br.ufsc.condominio.model.PacoteDeEspaçosCompartilhados.EspacoCompartilhado;
import br.ufsc.condominio.model.PacoteDeEspaçosCompartilhados.Reserva;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EspacoCompartilhadoController {

    private final Armazenamento armazenamento = Armazenamento.getInstancia();

    public void criarEspaco(EspacoCompartilhado espaco) {
        armazenamento.adicionarEspaco(espaco);
    }

    public List<EspacoCompartilhado> listarEspacos() {
        return armazenamento.getEspacos();
    }

    public EspacoCompartilhado buscarPorNome(String nome) {
        for (EspacoCompartilhado e : armazenamento.getEspacos()) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                return e;
            }
        }
        return null;
    }

    // Retorna a reserva ativa agora para um espaço, ou null se livre
    public Reserva getReservaAtiva(EspacoCompartilhado espaco) {
        Date agora = new Date();
        for (Reserva r : armazenamento.getReservas()) {
            if (r.getEspacoCompartilhado() == espaco) {
                if (!agora.before(r.getInicio()) && !agora.after(r.getFim())) {
                    return r;
                }
            }
        }
        return null;
    }

    // Verifica se há conflito de horário para um espaço no intervalo dado
    public boolean temConflito(EspacoCompartilhado espaco, Date inicio, Date fim) {
        for (Reserva r : armazenamento.getReservas()) {
            if (r.getEspacoCompartilhado() == espaco) {
                if (r.getInicio().before(fim) && inicio.before(r.getFim())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Conta quantas reservas ainda não encerradas (futuras ou em andamento) o condômino possui
    public int contarReservasFuturas(String cpf) {
        Date agora = new Date();
        int count = 0;
        for (Reserva r : armazenamento.getReservas()) {
            if (r.getCondomino().getCPF().equals(cpf) && r.getFim().after(agora)) {
                count++;
            }
        }
        return count;
    }

    public boolean atingiuLimiteReservasFuturas(String cpf) {
        return contarReservasFuturas(cpf) >= armazenamento.getLimiteReservasFuturas();
    }

    public int getLimiteReservasFuturas() {
        return armazenamento.getLimiteReservasFuturas();
    }

    public boolean definirLimiteReservasFuturas(int limite) {
        if (limite < 1) {
            return false;
        }
        armazenamento.setLimiteReservasFuturas(limite);
        return true;
    }

    public boolean agendarReserva(EspacoCompartilhado espaco, Condomino condomino, Date inicio, Date fim) {
        if (inicio.after(fim) || inicio.equals(fim)) {
            return false;
        }
        if (temConflito(espaco, inicio, fim)) {
            return false;
        }
        if (atingiuLimiteReservasFuturas(condomino.getCPF())) {
            return false;
        }
        armazenamento.adicionarReserva(new Reserva(espaco, inicio, fim, condomino));
        return true;
    }

    public List<Reserva> listarReservasDoCondomino(String cpf) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : armazenamento.getReservas()) {
            if (r.getCondomino().getCPF().equals(cpf)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<Reserva> listarTodasReservas() {
        return armazenamento.getReservas();
    }

    public boolean cancelarReservaDoCondomino(int indice, String cpf) {
        List<Reserva> minhas = listarReservasDoCondomino(cpf);
        if (indice < 0 || indice >= minhas.size()) {
            return false;
        }
        armazenamento.removerReserva(minhas.get(indice));
        return true;
    }

    public boolean cancelarQualquerReserva(int indice) {
        List<Reserva> todas = armazenamento.getReservas();
        if (indice < 0 || indice >= todas.size()) {
            return false;
        }
        armazenamento.removerReserva(todas.get(indice));
        return true;
    }
}
