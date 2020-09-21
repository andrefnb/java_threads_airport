package soprojetov3;

/**
 *
 * @author Luís Mestre
 */
public class Grupo {

    private int nPassageiros;
    private final String meioTransporte;
    private final String destino;
    private int horaDeAterragem;
    private int tempoDeEspera;
    private final int numeroVoo;

    /**
     *
     * @param nPassageiros
     * @param meioTransporte
     * @param destino
     * @param numeroVoo
     */
    public Grupo(int nPassageiros, String meioTransporte, String destino, int numeroVoo) {
        this.nPassageiros = nPassageiros;
        this.meioTransporte = meioTransporte.trim();
        this.destino = destino.trim();
        this.tempoDeEspera = this.horaDeAterragem = 0;
        this.numeroVoo = numeroVoo;
    }

    /**
     *
     * @return
     */
    public int getnPassageiros() {
        return nPassageiros;
    }

    /**
     *
     * @param nPassageiros
     */
    public void setnPassageiros(int nPassageiros) {
        this.nPassageiros = nPassageiros;
    }

    /**
     *
     * @return
     */
    public String getMeioTransporte() {
        return meioTransporte;
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
     * @param horaDeAterragem
     */
    public void setHoraDeAterragem(int horaDeAterragem) {
        this.horaDeAterragem = horaDeAterragem;
    }

    /**
     *
     * @return
     */
    public int getTempoDeEspera() {
        return tempoDeEspera;
    }

    /**
     *
     * @param tempoDeEspera
     */
    public void setTempoDeEspera(int tempoDeEspera) {
        this.tempoDeEspera = tempoDeEspera - horaDeAterragem;
    }

    /**
     *
     * @return
     */
    public int getNumeroVoo() {
        return numeroVoo;
    }
    
    public boolean isEmpty(){
        return nPassageiros == 0;
    }

    @Override
    public String toString() {
        return String.format("Nº de Passageiros: %d;\nTransporte Pretendido: %s;\nCom Destino a %s\n", nPassageiros, meioTransporte, destino);
    }
}
