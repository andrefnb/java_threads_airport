/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.util.StringTokenizer;

/**
 *
 * @author Luís Mestre
 */
public interface Transporte {
    
    /**
     *
     * @param leitor
     */
    public void criarTransporte(StringTokenizer leitor);
    
    /**
     *
     */
    public void ocuparLugar();
}
