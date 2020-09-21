/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís Mestre
 */
public abstract class TransporteTerrestre implements Transporte {

    private final Terminal terminal;

    private int nTransporte;
    private String destino;
    private String horaChegadaIM;
    private String horaPartidaIM;
    private int nEmbarque;
    private int nLugaresVagos;

    private final int TOLERANCIA;

    private String horaChegada;
    private String horaPartida;
    private String horaPartidaMaxima;
    private int passageirosDentroPrazo;
    private int passageirosForaPrazo;
    private String gruposTempoEspera;

    /**
     *
     * @param terminal
     * @param TOLERANCIA
     */
    public TransporteTerrestre(Terminal terminal, int TOLERANCIA) {
        this.TOLERANCIA = TOLERANCIA;
        this.terminal = terminal;
        this.passageirosDentroPrazo = this.passageirosForaPrazo = 0;
        this.horaChegada = this.horaPartidaMaxima = this.gruposTempoEspera = this.horaPartida = "";
    }

    /**
     *
     * @param leitor
     */
    public void lerConfig(StringTokenizer leitor) {
        criarTransporte(leitor);
        if (leitor.hasMoreTokens()) {
            leitor.nextToken();
        }
        this.horaPartidaMaxima = Util.getHora(Util.getTimeSeconds(horaPartidaIM) + TOLERANCIA);
    }

    /**
     *
     * @param leitor
     */
    @Override
    public void criarTransporte(StringTokenizer leitor) {
        this.nTransporte = Util.StringToInt(leitor.nextToken());
        this.destino = leitor.nextToken().trim();
        this.horaChegadaIM = leitor.nextToken().trim();
        this.horaPartidaIM = leitor.nextToken().trim();
        this.nEmbarque = Util.StringToInt(leitor.nextToken());
        this.nLugaresVagos = Util.StringToInt(leitor.nextToken());
    }

    /**
     *
     */
    @Override
    public synchronized void ocuparLugar() {
        if (!canPark()) {
            terminal.transporteEspera(this);
        } else {
            terminal.addTransporte(this, nEmbarque);
            horaChegada = Util.getHora(IM.getCurrentTime());
            String transporte = this.getClass().getSimpleName().toLowerCase();
            String paragem = (transporte.equals("comboio")) ? "plataforma" : "linha";
            System.out.println("O " + transporte + " nº " + nTransporte + " chegou à " + paragem + " nº " + nEmbarque);
            try {
                if (!Util.isHoraPartida(getHoraPartidaIM())) {
                    wait();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Comboio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     *
     * @param passageiros
     * @return
     */
    public synchronized int addPassengers(int passageiros) {
        int passageirosEntraram = 0;
        if (nLugaresVagos > 0 && nLugaresVagos >= passageiros) {
            setnLugaresVagos(nLugaresVagos - passageiros);
            System.out.println("Entraram " + passageiros + " passageiros para o " + this.getClass().getSimpleName() + " nº " + nTransporte + " com destino a " + destino);
            passageirosEntraram = passageiros;
        }
        return passageirosEntraram;
    }

    /**
     *
     * @return
     */
    public synchronized boolean canPark() {
        return !terminal.paragemOcupada(nEmbarque) && IM.getCurrentTime() >= Util.getTimeSeconds(horaChegadaIM);
    }

    /**
     *
     * @return
     */
    public boolean onTime() {
        return Util.onTime(horaChegadaIM, horaPartidaIM);
    }

    /**
     *
     * @return
     */
    public int getnLugaresVagos() {
        return nLugaresVagos;
    }

    /**
     *
     * @param nLugaresVagos
     */
    public void setnLugaresVagos(int nLugaresVagos) {
        this.nLugaresVagos = nLugaresVagos;
    }

    /**
     *
     * @return
     */
    public int getnTransporte() {
        return nTransporte;
    }

    /**
     *
     * @return
     */
    public String getDestino() {
        return destino;
    }

    /**
     *
     * @return
     */
    public int getnEmbarque() {
        return nEmbarque;
    }

    /**
     *
     * @return
     */
    public String getHoraChegadaIM() {
        return horaChegadaIM;
    }

    /**
     *
     * @return
     */
    public String getHoraPartidaIM() {
        return horaPartidaIM;
    }

    /**
     *
     * @return
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     *
     * @return
     */
    public String getHoraChegada() {
        return horaChegada;
    }

    /**
     *
     * @param passageirosDentroPrazo
     */
    public void setPassageirosDentroPrazo(int passageirosDentroPrazo) {
        this.passageirosDentroPrazo += passageirosDentroPrazo;
    }

    /**
     *
     * @param passageirosForaPrazo
     */
    public void setPassageirosForaPrazo(int passageirosForaPrazo) {
        this.passageirosForaPrazo += passageirosForaPrazo;
    }

    /**
     *
     * @param gruposTempoEspera
     */
    public void setGruposTempoEspera(String gruposTempoEspera) {
        this.gruposTempoEspera += gruposTempoEspera;
    }

    /**
     *
     * @return
     */
    public String getResultadoParticular() {
        String numero = "Número: " + nTransporte + "\n";
        String destinoFinal = "Destino: " + destino + "\n";
        String tempoAtraso = "Tempo de Atraso: " + Util.getHora(Util.getTimeSeconds(horaPartida) - Util.getTimeSeconds(horaPartidaIM)) + "\n";
        String passageirosEmbarcados = String.format("Passageiros Embarcados:\n- Dentro do Prazo: %d\n- Fora do Prazo: %d\n", passageirosDentroPrazo, passageirosForaPrazo);
        String lugaresPreencher = "Lugares por Preencher: " + nLugaresVagos + "\n";
        String tempoEspera = "Tempo de espera por grupo embarcado:\n" + gruposTempoEspera;
        return numero + destinoFinal + tempoAtraso + passageirosEmbarcados + lugaresPreencher + tempoEspera + "\n";
    }

    public void setHoraPartida(String horaPartida) {
        this.horaPartida = horaPartida;
    }

    public String getHoraPartidaMaxima() {
        return horaPartidaMaxima;
    }

    public synchronized boolean hasMorePassengers() {
        return terminal.hasMorePassengers(this);
    }

    public synchronized void wakeUp() {
        notifyAll();
    }
    
    public synchronized void removeTransporte(){
        terminal.removeTransporte(nEmbarque);
        setHoraPartida(Util.getHora(IM.getCurrentTime()));
    }
}
