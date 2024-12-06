/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clasesPrincipales;

import GUI.PantallaProductos;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                PantallaProductos pantallaProductos = new PantallaProductos();
                pantallaProductos.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

