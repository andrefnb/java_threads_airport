/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Luís Mestre
 */
public class Relogio implements Runnable {

    private final IM interfaceModal;
    private final Aeroporto aeroporto;
    private static boolean autocarrosComboiosEnded = false;
    private static boolean simulationEnded = false;
    private final ArrayList<Comboio> comboios;
    private final ArrayList<Autocarro> autocarros;
    private final ArrayList<Voo> voos;

    /**
     *
     * @param interfaceModal
     * @param aeroporto
     */
    public Relogio(IM interfaceModal, Aeroporto aeroporto) {
        this.interfaceModal = interfaceModal;
        this.aeroporto = aeroporto;
        this.comboios = new ArrayList();
        this.autocarros = new ArrayList();
        this.voos = new ArrayList();
    }

    @Override
    public void run() {
        iniciarListas();
        do {
            System.out.println("A Hora atual é: " + Util.getHora(IM.getCurrentTime()));
            iniciarTransporte();
            interfaceModal.wakeUpSleepyHeads();
            if (hasAllTransTerrestreEnded()) {
                autocarrosComboiosEnded = true;
                aeroporto.wakeUp();
            }
            if (autocarrosComboiosEnded && hasAllVoosEnded()) {
                simulationEnded = true;
            }
            Util.sleepTime(1000 / IM.getVELOCIDADE());
        } while (!simulationEnded);
        interfaceModal.resultados();
    }

    /**
     *
     * @return
     */
    public static boolean hasAutocarrosComboiosEnded() {
        return autocarrosComboiosEnded;
    }

    private boolean hasAllVoosEnded(){
        for (int i = 0; i < Aeroporto.listaDeVoos.size(); i++) {
            if (Aeroporto.listaDeVoos.get(i).acabou == false) {
                return false;
            }
        }
        return true;
    }
    
    private boolean hasAllTransTerrestreEnded() {
        for (int i = 0; i < Rodoviaria.listaDeAutocarros.size(); i++) {
            if (Rodoviaria.listaDeAutocarros.get(i).acabou == false) {
                return false;
            }
        }
        for (int i = 0; i < Ferroviaria.listaDeComboios.size(); i++) {
            if (Ferroviaria.listaDeComboios.get(i).acabou == false) {
                return false;
            }
        }
        return true;
    }

    private void iniciarListas() {
        iniciarLista(this.comboios, Ferroviaria.listaDeComboios);
        iniciarLista(this.autocarros, Rodoviaria.listaDeAutocarros);
        iniciarLista(this.voos, Aeroporto.listaDeVoos);
    }

    private void iniciarLista(ArrayList lista, ArrayList listaOriginal) {
        Iterator it = listaOriginal.iterator();
        while (it.hasNext()) {
            lista.add(it.next());
        }
    }

    private void iniciarTransporte() {
        for (int i = 0; i < voos.size(); i++) {
            if (voos.get(i) != null && (Util.getTimeSeconds(voos.get(i).getHoraChegadaIM()) <= Util.getTimeSeconds(Util.getHora(IM.getCurrentTime())))) {
                new Thread(voos.get(i)).start();
                voos.set(i, null);
            }
        }
        for (int i = 0; i < comboios.size(); i++) {
            if (comboios.get(i) != null && Util.getTimeSeconds(comboios.get(i).getHoraChegadaIM()) <= Util.getTimeSeconds(Util.getHora(IM.getCurrentTime()))) {
                new Thread(comboios.get(i)).start();
                comboios.set(i, null);
            }
        }
        for (int i = 0; i < autocarros.size(); i++) {
            if (autocarros.get(i) != null && Util.getTimeSeconds(autocarros.get(i).getHoraChegadaIM()) <= Util.getTimeSeconds(Util.getHora(IM.getCurrentTime()))) {
                new Thread(autocarros.get(i)).start();
                autocarros.set(i, null);
            }
        }
    }
}
