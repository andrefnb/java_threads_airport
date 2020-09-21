/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís Mestre
 */
public class Comboio extends TransporteTerrestre implements Runnable {

    private static StringTokenizer leitor;
    private static boolean first = true;
    private static boolean firstTransport = true;
    public static String hora = "";
    private static final String FICHEIRO = "comboios.txt";
    private static final int TOLERANCIA = 10;
    private static String resultados;
    public static boolean hasMoreComboios = true;
    public boolean acabou;

    /**
     *
     * @param ferroviaria
     */
    public Comboio(Ferroviaria ferroviaria) {
        super(ferroviaria, TOLERANCIA);
        resultados = "";
        this.acabou = false;
        if (first) {
            try {
                leitor = new StringTokenizer(new String(Files.readAllBytes(Paths.get(FICHEIRO))), "\n");
            } catch (IOException ex) {
                Logger.getLogger(Autocarro.class.getName()).log(Level.SEVERE, null, ex);
            }
            first = false;
        }
        super.lerConfig(leitor);
        if (firstTransport) {
            hora = super.getHoraChegadaIM();
            firstTransport = false;
        }
        hasMoreComboios = leitor.hasMoreTokens();
    }

    @Override
    public void run() {
        boolean partiu = false;

        do {
            ocuparLugar();
        } while (!getTerminal().estaEstacionado(this));

        while (!Util.isHoraPartida(getHoraPartidaIM())) {
            Util.sleepTime(100/ IM.getVELOCIDADE());
        }

        while (!partiu) {
            if (!hasMorePassengers()) {
                partiu = true;
            } else {
                Util.sleepTime(500 / IM.getVELOCIDADE());
            }
        }

        removeTransporte();
        System.out.println("O comboio " + getnTransporte() + " partiu da linha nº " + getnEmbarque());

        setResultados(getResultadoParticular());
        acabou = true;
    }

    /**
     *
     * @return
     */
    public static String getResultados() {
        return resultados;
    }

    /**
     *
     * @param resultados
     */
    public static void setResultados(String resultados) {
        Comboio.resultados += resultados;
    }
}
