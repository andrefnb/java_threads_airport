/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís Mestre
 */
public abstract class Terminal {

    private TransporteTerrestre[] transportes;

    /**
     *
     * @param transportes
     */
    public Terminal(TransporteTerrestre[] transportes) {
        this.transportes = transportes;
    }

    /**
     *
     * @param destino
     * @param numeroPassageiros
     * @return
     */
    public TransporteTerrestre getTransporte(String destino, int numeroPassageiros) {
        TransporteTerrestre transporte = null;
        for (int i = 0; i < transportes.length; i++) {
            if (paragemOcupada(i + 1) && transportes[i].getDestino().equals(destino)) {
                if (transportes[i].getnLugaresVagos() == numeroPassageiros) {
                    transporte = transportes[i];
                }
            }
        }
        if (transporte == null) {
            for (int i = 0; i < transportes.length; i++) {
                if (paragemOcupada(i + 1) && transportes[i].getDestino().equals(destino)) {
                    if (transportes[i].getnLugaresVagos() > numeroPassageiros) {
                        transporte = transportes[i];
                    }
                }
            }
        }
        return transporte;
    }

    /**
     *
     * @param transporte
     * @param paragem
     */
    public synchronized void addTransporte(TransporteTerrestre transporte, int paragem) {
        transportes[paragem - 1] = transporte;
    }

    /**
     *
     * @param paragem
     */
    public synchronized void removeTransporte(int paragem) {
        transportes[paragem - 1] = null;
        notifyAll();
    }

    /**
     *
     * @param paragem
     * @return
     */
    public boolean paragemOcupada(int paragem) {
        return transportes[paragem - 1] != null;
    }

    /**
     *
     * @param transporte
     * @return
     */
    public boolean estaEstacionado(TransporteTerrestre transporte) {
        return paragemOcupada(transporte.getnEmbarque()) && transportes[transporte.getnEmbarque() - 1].equals(transporte);
    }

    /**
     *
     * @return
     */
    public TransporteTerrestre[] getTransportes() {
        return transportes;
    }

    /**
     *
     */
    public abstract void criarTransportes();

    /**
     *
     * @param transporte
     */
    public synchronized void transporteEspera(TransporteTerrestre transporte) {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeroporto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param transporte
     * @return
     */
    public synchronized boolean hasMorePassengers(TransporteTerrestre transporte) {
        ArrayList<Voo> voos = Aeroporto.listaDeVoos;
        for (int i = 0; i < voos.size(); i++) {
            if (!voos.get(i).acabou && !voos.get(i).aterrou() && Util.canWaitMore(transporte.getHoraPartidaMaxima())
                    && (Util.getTimeSeconds(voos.get(i).getHoraChegadaIM()) <= Util.getTimeSeconds(transporte.getHoraPartidaMaxima()))) {
                for (Grupo grupo : Aeroporto.listaDeVoos.get(i).getListaDeGrupos()) {
                    if (grupo.getnPassageiros() != 0) {
                        if (grupo.getMeioTransporte().equals(transporte.getClass().getSimpleName().toLowerCase())
                                && grupo.getDestino().equals(transporte.getDestino())
                                && grupo.getnPassageiros() <= transporte.getnLugaresVagos()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
