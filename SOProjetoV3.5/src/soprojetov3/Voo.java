/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís Mestre
 */
public class Voo implements Transporte, Runnable {

    private static StringTokenizer leitor;
    private static boolean first = true;
    private static boolean firstTransport = true;
    public static String hora = "";
    private static final String FICHEIRO = "voos.txt";
    private static final int ESPERA = 30;
    public static boolean hasMoreVoos = true;
    public boolean acabou;
    private boolean aterrou;

    private int nVoo;
    private String origem;
    private String horaChegadaIM;
    private int nPortaEmbarque;
    private final ArrayList<Grupo> listaDeGrupos;

    private Aeroporto aeroporto;
    private IM interfaceModal;

    private String horaAterragem;
    private static String gruposRetidos;

    /**
     *
     * @param aeroporto
     * @param interfaceModal
     */
    public Voo(Aeroporto aeroporto, IM interfaceModal) {
        this.aeroporto = aeroporto;
        this.interfaceModal = interfaceModal;
        this.horaAterragem = "";
        this.aterrou = false;
        this.acabou = false;
        gruposRetidos = "";
        if (first == true) {
            try {
                leitor = new StringTokenizer(new String(Files.readAllBytes(Paths.get(FICHEIRO))), "\n");
            } catch (IOException ex) {
                Logger.getLogger(Autocarro.class.getName()).log(Level.SEVERE, null, ex);
            }
            first = false;
        }
        this.listaDeGrupos = new ArrayList();
        criarTransporte(leitor);
        if (firstTransport) {
            hora = getHoraChegadaIM();
            firstTransport = false;
        }
        hasMoreVoos = leitor.hasMoreTokens();
    }

    /**
     *
     * @param leitor
     */
    @Override
    public void criarTransporte(StringTokenizer leitor) {
        this.nVoo = Util.StringToInt(leitor.nextToken());
        this.origem = leitor.nextToken().trim();
        this.horaChegadaIM = leitor.nextToken().trim();
        this.nPortaEmbarque = Util.StringToInt(leitor.nextToken());
        addGrupos(leitor);
    }

    private void addGrupos(StringTokenizer tk) {
        String line = tk.nextToken("\n");
        boolean hasMoreTokens = true;
        do {
            StringTokenizer temp = new StringTokenizer(line);
            listaDeGrupos.add(new Grupo(Util.StringToInt(temp.nextToken(";").trim()), temp.nextToken(";"), temp.nextToken(), nVoo));
            if (tk.hasMoreTokens()) {
                line = tk.nextToken("\n");
            } else {
                hasMoreTokens = false;
            }
        } while (line.length() > 1 && hasMoreTokens);
    }

    @Override
    public void run() {
        
        do {
            ocuparLugar();
            aterrou = aeroporto.estaEstacionado(this, nPortaEmbarque);
        } while (aterrou == false && !Relogio.hasAutocarrosComboiosEnded());
        
        while (!isEmpty() && !Relogio.hasAutocarrosComboiosEnded()) {
            for (Grupo grupo : listaDeGrupos) {
                if (!grupo.isEmpty()) {
                    interfaceModal.entregarPassageiros(grupo);
                }
            }
            Util.sleepTime(50);
        }
        
        if (isEmpty()) {
            while (!Util.isHoraPartida(getHoraPartida()) && !Relogio.hasAutocarrosComboiosEnded()) {
                Util.sleepTime(50);
            }
            aeroporto.removeTransporte(nPortaEmbarque);
            System.out.println("O Avião " + nVoo + " partiu do Aeroporto. Ele estava aterrado na porta nº " + nPortaEmbarque);
        } else {
            for (Grupo grupo : listaDeGrupos) {
                if (grupo.getnPassageiros() != 0) {
                    gruposRetidos += "O grupo do avião " + nVoo + " com " + grupo.getnPassageiros() + " passageiros, que iria embarcar no " + grupo.getMeioTransporte() + " com destino a " + grupo.getDestino() + " não embarcou a tempo\n";
                }
            }
        }
        acabou = true;
    }

    private synchronized boolean canLand() {
        return !aeroporto.paragemOcupada(nPortaEmbarque) && IM.getCurrentTime() >= Util.getTimeSeconds(horaChegadaIM) && aterrou == false;
    }

    private String getHoraPartida() {
        int tempo = Util.getTimeSeconds(horaAterragem) + ESPERA;
        return Util.getHora(tempo);
    }

    /**
     *
     */
    @Override
    public synchronized void ocuparLugar() {
        if (!canLand()) {
            aeroporto.transporteEspera(this, nPortaEmbarque);
        } else {
            aeroporto.addTransporte(this, nPortaEmbarque);
            horaAterragem = Util.getHora(IM.getCurrentTime());
            setLandTime();
            System.out.println("O avião nº " + nVoo + " chegou à porta de embarque número " + nPortaEmbarque);
        }
    }

    private void setLandTime() {
        for (int i = 0; i < listaDeGrupos.size(); i++) {
            listaDeGrupos.get(i).setHoraDeAterragem(IM.getCurrentTime());
        }
    }

    private synchronized boolean isEmpty() {
        int numeroPassageiros = 0;
        for (Grupo grupo : listaDeGrupos) {
            numeroPassageiros += grupo.getnPassageiros();
        }
        return numeroPassageiros == 0;
    }

    /**
     *
     * @return
     */
    public synchronized static String getGruposRetidos() {
        return gruposRetidos;
    }

    private String listaDosGrupos() {
        String grupos = "";
        for (int i = 0; i < listaDeGrupos.size(); i++) {
            grupos += listaDeGrupos.get(i).toString();
        }
        return grupos;
    }

    public boolean aterrou() {
        return aterrou;
    }

    public String getHoraChegadaIM() {
        return horaChegadaIM;
    }

    public ArrayList<Grupo> getListaDeGrupos() {
        return listaDeGrupos;
    }

    public int getnVoo() {
        return nVoo;
    }

    @Override
    public String toString() {
        return String.format("O voo nº %d que veio de %s chega às %s na porta nº %d com os seguintes grupos de pessoas:\n%s", nVoo, origem, horaChegadaIM, nPortaEmbarque, listaDosGrupos());
    }
}
