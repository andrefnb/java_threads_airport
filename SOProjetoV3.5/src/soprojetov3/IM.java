package soprojetov3;

/**
 *
 * @author Luís Mestre
 */
public class IM {

    private static int time = 0;
    private static long currentTime;
    private static final int VELOCIDADE = 2;
    private static final int MINUTOSPORSEGUNDO = 1;

    private final Rodoviaria rodoviaria;
    private final Ferroviaria ferroviaria;

    /**
     *
     * @param rodoviaria
     * @param ferroviaria
     */
    public IM(Rodoviaria rodoviaria, Ferroviaria ferroviaria) {
        currentTime = System.currentTimeMillis() / (1000 / VELOCIDADE);
        this.ferroviaria = ferroviaria;
        this.rodoviaria = rodoviaria;
    }

    private int horaDeComeco(){
        String hora;
        if (Util.getTimeSeconds(Autocarro.hora) <= Util.getTimeSeconds(Comboio.hora) ) {
            hora = Autocarro.hora;
        }else{
            hora = Comboio.hora;
        }
        if (Util.getTimeSeconds(hora) > Util.getTimeSeconds(Voo.hora)) {
            hora = Voo.hora;
        }
        return Util.getTimeSeconds(hora)-20;
    }
    
    /**
     *
     * @param grupo
     */
    public synchronized void entregarPassageiros(Grupo grupo) {
        TransporteTerrestre transporte = (TransporteTerrestre) getTerminal(grupo.getMeioTransporte()).getTransporte(grupo.getDestino(), grupo.getnPassageiros());
        if (transporte != null && Util.canWaitMore(transporte.getHoraPartidaMaxima())) {
            int passageirosEntregues = transporte.addPassengers(grupo.getnPassageiros());
            if (passageirosEntregues != 0) {
                grupo.setnPassageiros(0);
                atualizarResultados(transporte, grupo, passageirosEntregues);
            }
        }
    }

    private void atualizarResultados(TransporteTerrestre transporte, Grupo grupo, int passageirosEntregues) {
        if (transporte.onTime()) {
            transporte.setPassageirosDentroPrazo(passageirosEntregues);
        } else {
            transporte.setPassageirosForaPrazo(passageirosEntregues);
        }
        grupo.setTempoDeEspera(getCurrentTime());
        transporte.setGruposTempoEspera(String.format("- Grupo do voo %d com %d passageiros, esperou %s tempo para puder embarcar\n", grupo.getNumeroVoo(), passageirosEntregues, Util.getHora(grupo.getTempoDeEspera())));
    }

    private Terminal getTerminal(String transporte) {
        switch (transporte) {
            case "autocarro":
                return rodoviaria;
            case "comboio":
                return ferroviaria;
            default:
                throw new AssertionError();
        }
    }

    /**
     *
     * @return
     */
    public static int getCurrentTime() {
        long tempoPassado = (System.currentTimeMillis() / (1000 / VELOCIDADE) - currentTime);
        time += tempoPassado * MINUTOSPORSEGUNDO;
        currentTime += tempoPassado;
        return time;
    }

    public void wakeUpSleepyHeads(){
        for (TransporteTerrestre transporte : ferroviaria.getTransportes()) {
            if (transporte != null && Util.isHoraPartida(transporte.getHoraPartidaIM())) {
                transporte.wakeUp();
            }
        }
        for (TransporteTerrestre transporte : rodoviaria.getTransportes()) {
            if (transporte != null && Util.isHoraPartida(transporte.getHoraPartidaIM())) {
                transporte.wakeUp();
            }
        }
    }
    
    /**
     *
     * @return
     */
    public static int getVELOCIDADE() {
        return VELOCIDADE;
    }

    /**
     *
     */
    public void resultados() {
        System.out.println("\nResultados dos Autocarros:\n\n" + Autocarro.getResultados());
        System.out.println("Resultados dos Comboios:\n\n" + Comboio.getResultados());
        System.out.print(String.format("Grupos de passageiros que ficaram retidos no avião:\n%s", Voo.getGruposRetidos()));
    }
    
    public void criarTransportes(Rodoviaria rodoviaria, Ferroviaria ferroviaria, Aeroporto aeroporto){
        rodoviaria.criarTransportes();
        ferroviaria.criarTransportes();
        aeroporto.criarTransportes(this);
        time = horaDeComeco();
    }
}
