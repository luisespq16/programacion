/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aplicacioncarneuni;

import Controlador.Controlador;
import Modelo.Carne;
import Vista.CarneVista;

/**
 *
 * @author USUARIO
 */
public class AplicacionCarneUni {

    public static void main(String[] args) {
        CarneVista vista = new CarneVista();
        Carne modelo = new Carne();
        Controlador control = new Controlador(modelo, vista);
        control.iniciarVista();
    }
}
