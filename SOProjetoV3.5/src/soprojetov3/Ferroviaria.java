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
public class Ferroviaria extends Terminal {

    private static final int PLATAFORMAS = 2;
    public static ArrayList<Comboio> listaDeComboios;

    /**
     *
     */
    public Ferroviaria() {
        super(new Comboio[PLATAFORMAS]);
        listaDeComboios = new ArrayList();
    }

    @Override
    public void criarTransportes() {
        while (Comboio.hasMoreComboios) {
            Comboio c = new Comboio(this);
            listaDeComboios.add(c);
        }
    }
}
