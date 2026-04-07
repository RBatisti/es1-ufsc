package br.ufsc.condominio.controller.ControladorDeEspacosCompartilhados;

import br.ufsc.condominio.controller.MainController;
import br.ufsc.condominio.model.EspacoCompartilhado;
import br.ufsc.condominio.model.Reserva;
import br.ufsc.condominio.model.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EspacoCompartilhadoController {
    private List<EspacoCompartilhado> espacosDisponiveis;
    private List<Reserva> listaDeReservas;
    private MainController mainController;

    public EspacoCompartilhadoController(MainController mainController) {
        this.mainController = mainController;
        this.espacosDisponiveis = new ArrayList<>();
        this.listaDeReservas = new ArrayList<>();
    }

    public void cadastrarEspaco(EspacoCompartilhado espaco) {
        espacosDisponiveis.add(espaco);
        System.out.println("Espaço cadastrado: " + espaco.getNome());
    }

    public List<EspacoCompartilhado> listarEspacos() {
        return espacosDisponiveis;
    }


    public boolean solicitarReserva(EspacoCompartilhado espaco, Usuario morador, Date inicio, Date fim) {


//        // Adaptar implementação para utilizar inicio-fim
//        for (Reserva reservaExistente : listaDeReservas) {
//            if (reservaExistente.getEspacoCompartilhado().equals(espaco) && reservaExistente.getData().equals(data)) {
//                System.out.println("Erro: O espaço '" + espaco.getNome() + "' já está reservado nesta data.");
//                return false;
//            }
//        }
//
//        Reserva novaReserva = new Reserva(espaco, morador, data);
//        listaDeReservas.add(novaReserva);
//        System.out.println("Reserva confirmada para " + morador.getNome() + " no dia " + data);
//
       return true;

    }

    public List<Reserva> listarReservasPorMorador(Usuario morador) {
        List<Reserva> reservasDoMorador = new ArrayList<>();

//        for (Reserva r : listaDeReservas) {
//            if (r.getMorador().equals(morador)) {
//                reservasDoMorador.add(r);
//            }
//        }
        return reservasDoMorador;
    }

    public boolean cancelarReserva(Reserva reserva, Usuario morador) {
//        if (reserva.getMorador().equals(morador)) {
//            listaDeReservas.remove(reserva);
//            System.out.println("Reserva cancelada com sucesso.");
//            return true;
//        }
        System.out.println("Erro: Você não tem permissão para cancelar esta reserva.");
        return false;
    }
}