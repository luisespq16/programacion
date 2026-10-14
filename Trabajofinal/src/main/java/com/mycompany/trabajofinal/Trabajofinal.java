/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.trabajofinal;

import Modelo.Carne;
import controlador.ControladorCarne;
import vista.CarneVista;

/**
 *
 * @author USUARIO
 */
public class Trabajofinal {

    public static void main(String[] args) {
        CarneVista vista = new CarneVista();
        Carne modelo = new Carne();
        ControladorCarne controlador = new ControladorCarne(modelo, vista);
        controlador.iniciarVista();
    }
}
