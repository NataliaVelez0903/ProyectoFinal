/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;
import clasesPrincipales.Producto;
import clasesPrincipales.Factura;
import Archivos.Json;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PantallaCompra extends JFrame {
    private JComboBox<String> comboProductos;
    private JTextField txtNombreCliente, txtIdentificacion, txtDireccion;
    private JLabel lblImpuesto, lblTotal;
    private JButton btnGenerarFactura;
    private Json json;
    private List<Producto> productos;

    public PantallaCompra(List<Producto> productos) {
        setTitle("Compra de Productos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.productos = productos;
        json = new Json();

        // Panel Principal
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Producto:"));
        comboProductos = new JComboBox<>();
        for (Producto p : productos) {
            comboProductos.addItem(p.getNombre() + " - $" + p.getPrecio());
        }
        panel.add(comboProductos);

        panel.add(new JLabel("Nombre del Cliente:"));
        txtNombreCliente = new JTextField();
        panel.add(txtNombreCliente);

        panel.add(new JLabel("Identificación:"));
        txtIdentificacion = new JTextField();
        panel.add(txtIdentificacion);

        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        lblImpuesto = new JLabel("Impuesto: 0.0");
        panel.add(lblImpuesto);

        lblTotal = new JLabel("Total: 0.0");
        panel.add(lblTotal);

        btnGenerarFactura = new JButton("Generar Factura");
        panel.add(btnGenerarFactura);

        add(panel);

        // Listeners
        comboProductos.addActionListener(e -> calcularTotal());
        btnGenerarFactura.addActionListener(e -> generarFactura());
    }

    private void calcularTotal() {
        int index = comboProductos.getSelectedIndex();
        if (index >= 0) {
            Producto producto = productos.get(index);
            double impuesto = producto.getPrecio() * 0.19;
            double total = producto.getPrecio() + impuesto;

            lblImpuesto.setText("Impuesto: " + impuesto);
            lblTotal.setText("Total: " + total);
        }
    }

    private void generarFactura() {
        try {
            String nombre = txtNombreCliente.getText();
            String identificacion = txtIdentificacion.getText();
            String direccion = txtDireccion.getText();
            Producto producto = productos.get(comboProductos.getSelectedIndex());

            Factura factura = new Factura();
            factura.setNombreCliente(nombre);
            factura.setIdentificacion(identificacion);
            factura.setDireccion(direccion);
            factura.setProducto(List.of(producto));
            factura.setImpuesto(producto.getPrecio() * 0.19);
            factura.setTotal(producto.getPrecio() + factura.getImpuesto());

            List<Factura> facturas = json.cargarFacturas();
            facturas.add(factura);
            json.guardarFacturas(facturas);

            JOptionPane.showMessageDialog(this, "Factura generada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar factura: " + ex.getMessage());
        }
    }

}
