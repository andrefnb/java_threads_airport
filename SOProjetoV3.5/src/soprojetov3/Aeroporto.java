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
public class Aeroporto {

    public static ArrayList<Voo> listaDeVoos;
    private static final int PORTAS = 3;
    private final Voo[] voos;

    public Aeroporto() {
        this.voos = new Voo[PORTAS];
        listaDeVoos = new ArrayList();
    }

    public synchronized void addTransporte(Voo transporte, int paragem) {
        voos[paragem - 1] = transporte;
    }

    public synchronized void removeTransporte(int paragem) {
        voos[paragem - 1] = null;
        notifyAll();
        
    }

    public synchronized boolean paragemOcupada(int paragem) {
        return voos[paragem - 1] != null;
    }

    public synchronized boolean estaEstacionado(Voo voo, int paragem) {
        return paragemOcupada(paragem) && voos[paragem - 1].equals(voo);
    }

    public synchronized void criarTransportes(IM interfaceModal) {
        while (Voo.hasMoreVoos) {
            Voo newVoo = new Voo(this, interfaceModal);
            listaDeVoos.add(newVoo);
        }
    }

    public synchronized void transporteEspera(Voo voo, int paragem){
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeroporto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void wakeUp(){
        notifyAll();
    }
}
