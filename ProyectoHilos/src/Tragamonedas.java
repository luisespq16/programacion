
import java.awt.Color;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
/**
 * @author Estudiante Luis Espejo
 * Ing. de Sistemas y Computación
 */
public class Tragamonedas extends javax.swing.JFrame {

    private volatile boolean running = false; 
    private final ImageIcon[] images = new ImageIcon[3];
    private Thread hilo1,hilo2,hilo3;
    private Timer ledBorderTimer;
    private final Color[] ledColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}; 
    private int currentColorIndex = 0;
    
    public Tragamonedas() {
        initComponents();
        cargarImagenes();
        setLocationRelativeTo(this);
        setTitle("Tragamonedas");
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("logo.png")).getImage());
        btniniciar.setEnabled(false);
        btndetener.setEnabled(false);
    }
    
    private void iniciarLucesLEDBordes() {
        JLabel[] labels = {lblimagen1, lblimagen2, lblimagen3};

        ledBorderTimer = new Timer(300, e -> {
            for (JLabel label : labels) {
                label.setBorder(BorderFactory.createLineBorder(ledColors[currentColorIndex], 5)); // Borde de 5px
            }

            currentColorIndex = (currentColorIndex + 1) % ledColors.length;

            for (JLabel label : labels) {
                label.repaint();
            }
        });

        ledBorderTimer.start();
    }

    private void detenerLucesLEDBordes() {
        if (ledBorderTimer != null) {
            ledBorderTimer.stop(); 
            resetBordes(); 
        }
    }

    private void resetBordes() {
        JLabel[] labels = {lblimagen1, lblimagen2, lblimagen3};
        for (JLabel label : labels) {
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            label.repaint();
        }
    }

    private ImageIcon escalarImagen(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }

    private void cargarImagenes() {
        images[0] = escalarImagen(new ImageIcon(getClass().getResource("campana.png")), 100, 100);
        images[1] = escalarImagen(new ImageIcon(getClass().getResource("diamante.png")), 100, 100);
        images[2] = escalarImagen(new ImageIcon(getClass().getResource("numero.png")), 100, 100);
    }
     
    private void iniciar() {
        if (running) return; // Evitar múltiples inicios
        running = true;
        
        iniciarLucesLEDBordes();

        hilo1 = new Thread(() -> ejecutarTragamonedas(lblimagen1));
        hilo2 = new Thread(() -> ejecutarTragamonedas(lblimagen2));
        hilo3 = new Thread(() -> ejecutarTragamonedas(lblimagen3));

        hilo1.start();
        hilo2.start();
        hilo3.start();
    }

    private void detener() {
        running = false; // Detiene los hilos de manera segura
        detenerLucesLEDBordes();
        try {
            if (hilo1 != null) hilo1.join();
            if (hilo2 != null) hilo2.join();
            if (hilo3 != null) hilo3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verificarResultado();
    }

    private void ejecutarTragamonedas(javax.swing.JLabel label) {
        Random random = new Random();
        while (running) {
            int index = random.nextInt(images.length); 
            label.setIcon(images[index]); 
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    int cantidadIngresada=0;
    int cantidadApostada=0;

    private void jugar(){       
        String input = JOptionPane.showInputDialog(null, "Ingrese la cantidad para apostar:", "Apuesta", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.isEmpty()) {
            try {
                int nuevaCantidad = Integer.parseInt(input);
                    if (nuevaCantidad > 0) {
                        cantidadIngresada+=nuevaCantidad;
                        JOptionPane.showMessageDialog(null, "Has apostado " + cantidadIngresada + " monedas.", "Apuesta Realizada", JOptionPane.INFORMATION_MESSAGE);
                        txtcred.setText(""+cantidadIngresada);                      
                    } else {
                        JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se ingresó ninguna cantidad.", "Error", JOptionPane.WARNING_MESSAGE);
        }              
    }
    
    private void apostar() {
        try {
            cantidadApostada = Integer.parseInt(txtapuesta.getText());

            if (cantidadApostada > cantidadIngresada) {
                JOptionPane.showMessageDialog(null, "Monto apostado insuficiente", "Error", JOptionPane.WARNING_MESSAGE);
            }else if (cantidadApostada <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0", "Error", JOptionPane.WARNING_MESSAGE);
            }else {
                btniniciar.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verificarResultado() {
        if (lblimagen1.getIcon().equals(lblimagen2.getIcon()) && lblimagen2.getIcon().equals(lblimagen3.getIcon())) {
            ganar();
        } else {
            perder();
        }
    }
    private void ganar() {
        JOptionPane.showMessageDialog(this, "¡Felicidades :), ganaste "+cantidadApostada*2+" !", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        cantidadIngresada=cantidadIngresada+cantidadApostada*2;
        cantidadApostada=0;
        txtcred.setText(""+cantidadIngresada);
        txtapuesta.setText("");
    }

    private void perder() {
        JOptionPane.showMessageDialog(this, "Lo siento, perdiste :(. ¡Inténtalo de nuevo!", "Resultado", JOptionPane.CANCEL_OPTION);
        cantidadIngresada-=cantidadApostada;
        cantidadApostada=0;
        txtcred.setText(""+cantidadIngresada);
        txtapuesta.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        btnjugar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtcred = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtapuesta = new javax.swing.JTextField();
        lbltitulo = new javax.swing.JLabel();
        lblimagen1 = new javax.swing.JLabel();
        lblimagen2 = new javax.swing.JLabel();
        lblimagen3 = new javax.swing.JLabel();
        btndetener = new javax.swing.JButton();
        btniniciar = new javax.swing.JButton();

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        btnjugar.setBackground(new java.awt.Color(255, 255, 0));
        btnjugar.setFont(new java.awt.Font("Asap", 1, 24)); // NOI18N
        btnjugar.setText("Apostar");
        btnjugar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 255, 0)));
        btnjugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnjugarActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Gabarito", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Crédito:");

        txtcred.setEditable(false);
        txtcred.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcredActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Gabarito", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Apuesta:");

        txtapuesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapuestaActionPerformed(evt);
            }
        });

        lbltitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icono_principal.png"))); // NOI18N

        lblimagen1.setBackground(new java.awt.Color(255, 255, 255));
        lblimagen1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));
        lblimagen1.setOpaque(true);

        lblimagen2.setBackground(new java.awt.Color(255, 255, 255));
        lblimagen2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblimagen2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));
        lblimagen2.setOpaque(true);

        lblimagen3.setBackground(new java.awt.Color(255, 255, 255));
        lblimagen3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));
        lblimagen3.setOpaque(true);

        btndetener.setBackground(new java.awt.Color(255, 51, 0));
        btndetener.setFont(new java.awt.Font("Asap", 1, 24)); // NOI18N
        btndetener.setText("Detener");
        btndetener.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 51, 0), new java.awt.Color(255, 51, 0), new java.awt.Color(255, 51, 0), new java.awt.Color(255, 51, 0)));
        btndetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndetenerActionPerformed(evt);
            }
        });

        btniniciar.setBackground(new java.awt.Color(153, 255, 0));
        btniniciar.setFont(new java.awt.Font("Asap", 1, 24)); // NOI18N
        btniniciar.setText("Iniciar");
        btniniciar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 255, 0), new java.awt.Color(153, 255, 0), new java.awt.Color(153, 255, 0), new java.awt.Color(153, 255, 0)));
        btniniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btniniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(btnjugar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcred, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btniniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btndetener, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtapuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)))
                .addGap(0, 58, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(lblimagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(lblimagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(lblimagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(lbltitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbltitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblimagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblimagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblimagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(txtcred, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtapuesta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btndetener, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnjugar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btniniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnjugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnjugarActionPerformed
        jugar();
    }//GEN-LAST:event_btnjugarActionPerformed

    private void btndetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndetenerActionPerformed
        detener();
        btndetener.setEnabled(false);
    }//GEN-LAST:event_btndetenerActionPerformed

    private void txtcredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcredActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcredActionPerformed

    private void txtapuestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapuestaActionPerformed
        apostar();
    }//GEN-LAST:event_txtapuestaActionPerformed

    private void btniniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btniniciarActionPerformed
        iniciar();
        btndetener.setEnabled(true);
        btniniciar.setEnabled(false);
    }//GEN-LAST:event_btniniciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tragamonedas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tragamonedas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tragamonedas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tragamonedas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tragamonedas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndetener;
    private javax.swing.JButton btniniciar;
    private javax.swing.JButton btnjugar;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblimagen1;
    private javax.swing.JLabel lblimagen2;
    private javax.swing.JLabel lblimagen3;
    private javax.swing.JLabel lbltitulo;
    private javax.swing.JTextField txtapuesta;
    private javax.swing.JTextField txtcred;
    // End of variables declaration//GEN-END:variables
}
