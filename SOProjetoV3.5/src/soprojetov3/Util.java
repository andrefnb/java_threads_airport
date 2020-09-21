/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís Mestre
 */
public class Util {

    /**
     *
     * @param numero
     * @return
     */
    public synchronized static int StringToInt(String numero) {
        return Integer.parseInt(numero.trim());
    }

    /**
     *
     * @param hora
     * @return
     */
    public synchronized static boolean isHoraPartida(String hora) {
        return IM.getCurrentTime() >= getTimeSeconds(hora);
    }

    /**
     *
     * @param hora
     * @return
     */
    public synchronized static boolean canWaitMore(String hora) {
        return IM.getCurrentTime() <= getTimeSeconds(hora);
    }

    /**
     *
     * @param horaChegada
     * @param horaPartida
     * @return
     */
    public synchronized static boolean onTime(String horaChegada, String horaPartida) {
        return IM.getCurrentTime() >= getTimeSeconds(horaChegada) && IM.getCurrentTime() <= getTimeSeconds(horaPartida);
    }

    /**
     *
     * @param time
     * @return
     */
    public synchronized static int getTimeSeconds(String time) {
        int minutos = Integer.parseInt(time.substring(0, 2));
        int segundos = Integer.parseInt(time.substring(3, time.length()));
        return minutos * 60 + segundos;
    }

    /**
     *
     * @param time
     * @return
     */
    public synchronized static String getHora(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    /**
     *
     * @param tempo
     */
    public static void sleepTime(int tempo) {
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
