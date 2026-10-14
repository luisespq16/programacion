/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Carne;
import Vista.CarneVista;
import Vista.VistaResumen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class Controlador {

    private Carne modelo;
    private CarneVista vista;

    public Controlador(Carne modelo, CarneVista vista) {

        this.modelo = modelo;
        this.vista = vista;

        vista.agregarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = vista.getCodigo();
                String DNI = vista.getDNI();
                String nombres = vista.getNombres();
                String apellidos = vista.getApellidos();
                String facultad = vista.getFacultad();
                String carrera = vista.getCarrera();

                // Actualizar el modelo con los datos
                if (codigo.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Ingrese su codigo");
                } else if (DNI.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Ingrese su DNI");
                } else if (nombres.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Ingrese sus nombres");
                } else if (apellidos.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Ingrese sus apellidos");
                } else if (facultad.equals("Seleccione...")) {
                    JOptionPane.showMessageDialog(vista, "Ingrese su facultad");
                } else if (carrera.equals("Seleccione...")) {
                    JOptionPane.showMessageDialog(vista, "Ingrese su carrera");
                } else {
                    modelo.setCodigo(codigo);
                    modelo.setDNI(DNI);
                    modelo.setNombres(nombres);
                    modelo.setApellidos(apellidos);
                    modelo.setFacultad(facultad);
                    modelo.setCarrera(carrera);
                    JOptionPane.showMessageDialog(vista, "Datos guardados correctamente!");
                    VistaResumen resultados = new VistaResumen();
                    resultados.mostrarResultados(codigo, DNI, nombres, apellidos, facultad, carrera);
                    resultados.setVisible(true);
                    vista.setVisible(false);
                }  
            }
        });
    }

    public void iniciarVista() {
        vista.setVisible(true);
    }
}
