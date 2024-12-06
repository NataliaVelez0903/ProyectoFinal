/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import clasesPrincipales.Factura;
import clasesPrincipales.Producto;
import Archivos.Json;

public class PantallaFactura extends JFrame {
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private Json json;

    public PantallaFactura() {
        json = new Json();

        setTitle("Gestión de Facturas");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());

        // Modelo de tabla
        modeloTabla = new DefaultTableModel(new String[]{"Cliente", "ID", "Dirección", "Productos", "Impuesto", "Total"}, 0);
        tablaFacturas = new JTable(modeloTabla);

        cargarFacturasEnTabla(); // Cargar las facturas en la tabla

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaFacturas);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnEliminar = new JButton("Eliminar Factura");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnEliminar.addActionListener(e -> eliminarFacturaSeleccionada());
        btnCerrar.addActionListener(e -> dispose());

        // Añadir el panel principal a la ventana
        add(panel);
    }

    private void cargarFacturasEnTabla() {
        try {
            List<Factura> facturas = json.cargarFacturas();
            modeloTabla.setRowCount(0); // Limpiar la tabla
            for (Factura factura : facturas) {
                String productos = factura.getProductosComprados().stream()
                        .map(Producto::getNombre)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Sin productos");

                modeloTabla.addRow(new Object[]{
                        factura.getNombreCliente(),
                        factura.getIdentificacion(),
                        factura.getDireccion(),
                        productos,
                        factura.getImpuesto(),
                        factura.getTotal()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar facturas: " + ex.getMessage());
        }
    }

    private void eliminarFacturaSeleccionada() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Confirmar eliminación
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de que desea eliminar esta factura?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Cargar todas las facturas
                    List<Factura> facturas = json.cargarFacturas();

                    // Eliminar la factura seleccionada
                    facturas.remove(filaSeleccionada);

                    // Guardar las facturas actualizadas
                    json.guardarFacturas(facturas);

                    // Actualizar la tabla
                    cargarFacturasEnTabla();

                    JOptionPane.showMessageDialog(this, "Factura eliminada correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar factura: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una factura para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
