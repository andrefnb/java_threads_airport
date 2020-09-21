/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soprojetov3;

import java.util.ArrayList;

/**
 *
 * @author Luís Mestre
 */
public class Rodoviaria extends Terminal{
    
    private static final int LINHAS = 3;
    public static ArrayList<Autocarro> listaDeAutocarros;
    
    /**
     *
     */
    public Rodoviaria() {
        super(new Autocarro[LINHAS]);
        listaDeAutocarros = new ArrayList();
    }

    @Override
    public void criarTransportes() {
        while (Autocarro.hasMoreAutocarros) {
            Autocarro a = new Autocarro(this);
            listaDeAutocarros.add(a);
        }
    }
}
