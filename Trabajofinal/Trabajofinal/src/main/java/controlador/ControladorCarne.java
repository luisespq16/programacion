/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import Modelo.Carne;
import vista.CarneVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.ConnectDB;

public class ControladorCarne implements ActionListener {

    private Carne modelo;
    private CarneVista vista;

    public ControladorCarne(Carne modelo, CarneVista vista) {

        this.modelo = modelo;
        this.vista = vista;
        this.vista.agregarActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBotonAddFacultad()) {
            String facultad = JOptionPane.showInputDialog("Ingrese la facultad:");
            if (facultad != null && !facultad.trim().isEmpty()) {
                Connection conn = null;
                try {
                    conn = ConnectDB.obtenerConexion();

                    String sql = "INSERT INTO Facultad (nombreFacultad) VALUES (?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, facultad);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Facultad añadida exitosamente.");
                    vista.getBotonDeleteFacultad().setEnabled(true);
                    vista.getBotonAddCarrera().setEnabled(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al guardar en la BD:\n" + ex.getMessage());
                } finally {
                    ConnectDB.cerrarConexion(conn);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Campo vacío o cancelado.");
            }
        }

        if (e.getSource() == vista.getBotonDeleteFacultad()) {
            String nombreFacultad = JOptionPane.showInputDialog("Ingrese el nombre de la facultad a borrar:");

            if (nombreFacultad != null && !nombreFacultad.trim().isEmpty()) {
                Connection conn = null;
                try {
                    conn = ConnectDB.obtenerConexion();

                    String sql = "DELETE FROM Facultad WHERE nombreFacultad = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nombreFacultad.trim());

                    int filasAfectadas = ps.executeUpdate();

                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(null, "Facultad eliminada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay facultad con ese nombre.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar facultad:\n" + ex.getMessage());
                } finally {
                    ConnectDB.cerrarConexion(conn);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operación cancelada o campo vacío.");
            }
        }

        if (e.getSource() == vista.getBotonAddCarrera()) {
            String carrera = JOptionPane.showInputDialog("Ingrese la carrera:");
            if (carrera != null && !carrera.trim().isEmpty()) {
                String nombreFacultad = JOptionPane.showInputDialog("Ingrese el nombre de la facultad a la que pertenece esta carrera:");
                if (nombreFacultad != null && !nombreFacultad.trim().isEmpty()) {
                    Connection conn = null;
                    try {
                        conn = ConnectDB.obtenerConexion();

                        // Obtener el idFacultad
                        String buscarFacultad = "SELECT idFacultad FROM Facultad WHERE nombreFacultad = ?";
                        PreparedStatement buscarPs = conn.prepareStatement(buscarFacultad);
                        buscarPs.setString(1, nombreFacultad.trim());
                        ResultSet rs = buscarPs.executeQuery();

                        if (rs.next()) {
                            int idFacultad = rs.getInt("idFacultad");

                            // Insertar la carrera con su idFacultad
                            String sql = "INSERT INTO Carrera (nombreCarrera, idFacultad) VALUES (?, ?)";
                            PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setString(1, carrera.trim());
                            ps.setInt(2, idFacultad);
                            ps.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Carrera añadida exitosamente.");
                            vista.getBotonDeleteCarrera().setEnabled(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Facultad no encontrada.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al guardar en la BD:\n" + ex.getMessage());
                    } finally {
                        ConnectDB.cerrarConexion(conn);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe ingresar el nombre de la facultad.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Campo de carrera vacío o cancelado.");
            }
        }
        
        if (e.getSource() == vista.getBotonDeleteCarrera()) {
            String nombreCarrera = JOptionPane.showInputDialog("Ingrese el nombre de la carrera a borrar:");

            if (nombreCarrera != null && !nombreCarrera.trim().isEmpty()) {
                Connection conn = null;
                try {
                    conn = ConnectDB.obtenerConexion();

                    String sql = "DELETE FROM Carrera WHERE nombreCarrera = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nombreCarrera.trim());

                    int filasAfectadas = ps.executeUpdate();

                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(null, "Carrera eliminada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay carrera con ese nombre.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar carrera:\n" + ex.getMessage());
                } finally {
                    ConnectDB.cerrarConexion(conn);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operación cancelada o campo vacío.");
            }
        }

        if (e.getSource() == vista.getBotonRegistro()) {
            if (!existenFacultadYCarrera()) {
                JOptionPane.showMessageDialog(vista, "Debe haber al menos una facultad y una carrera registrada antes de añadir estudiantes.");
                return;
            }
            String codigo = vista.getCodigo();
            String DNI = vista.getDNI();
            String nombres = vista.getNombres();
            String apellidos = vista.getApellidos();

            // Actualizar el modelo con los datos
            if (codigo.length() < 7) {
                JOptionPane.showMessageDialog(vista, "El código debe contener 7 caracteres");
            } else if (DNI.length() < 8) {
                JOptionPane.showMessageDialog(vista, "El DNI debe contener 8 dígitos");
            } else if (nombres.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese sus nombres");
            } else if (apellidos.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese sus apellidos");
            } else {
                modelo.setCodigo(codigo);
                modelo.setDNI(DNI);
                modelo.setNombres(nombres);
                modelo.setApellidos(apellidos);
                // Guardar en base de datos
                Connection conn = null;
                try {
                    conn = ConnectDB.obtenerConexion();

                    String sql = "INSERT INTO Estudiante (codigoAlumno, DNI, nombres, apellidos) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, codigo);
                    ps.setString(2, DNI);
                    ps.setString(3, nombres);
                    ps.setString(4, apellidos);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(vista, "Datos guardados correctamente!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista, "Error al guardar en la base de datos:\n" + ex.getMessage());
                } finally {
                    ConnectDB.cerrarConexion(conn);
                }

            }
        }

    }

    private boolean existenFacultadYCarrera() {
        Connection conn = null;
        boolean existeFacultad = false;
        boolean existeCarrera = false;
        try {
            conn = ConnectDB.obtenerConexion();

            // Verificar facultades
            String sqlFac = "SELECT COUNT(*) FROM Facultad";
            PreparedStatement psFac = conn.prepareStatement(sqlFac);
            ResultSet rsFac = psFac.executeQuery();
            if (rsFac.next()) {
                existeFacultad = rsFac.getInt(1) > 0;
            }

            // Verificar carreras
            String sqlCarr = "SELECT COUNT(*) FROM Carrera";
            PreparedStatement psCarr = conn.prepareStatement(sqlCarr);
            ResultSet rsCarr = psCarr.executeQuery();
            if (rsCarr.next()) {
                existeCarrera = rsCarr.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.cerrarConexion(conn);
        }
        return existeFacultad && existeCarrera;
    }

    public void iniciarVista() {
        vista.setVisible(true);
    }
}
