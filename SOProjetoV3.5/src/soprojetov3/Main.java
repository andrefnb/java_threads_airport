/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

/**
 *
 * @author Luís Mestre
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Rodoviaria rodoviaria = new Rodoviaria();
        Ferroviaria ferroviaria = new Ferroviaria();
        Aeroporto aeroporto = new Aeroporto();
        IM interfaceModal = new IM(rodoviaria, ferroviaria);
        
        System.out.println("Vai começar a simulação");
        
        interfaceModal.criarTransportes(rodoviaria, ferroviaria, aeroporto);
        
        new Thread (new Relogio(interfaceModal, aeroporto)).start();
    }
    
}
