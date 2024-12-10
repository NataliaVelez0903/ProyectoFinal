/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Archivos.XML;
import clasesPrincipales.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PantallaProductos extends JFrame {
    private List<Producto> productos; 
    private JTable tablaProductos; 
    private DefaultTableModel modeloTabla; 
    private XML xml; 

    public PantallaProductos() {
        xml = new XML(); 
        configurarVentana();
        inicializarComponentes();
        cargarProductosDesdeXML();
    }

    private void configurarVentana() {
        setTitle("Gestión de Productos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        productos = new ArrayList<>(); 
        setLayout(new BorderLayout());

        // Configurar tabla
        modeloTabla = new DefaultTableModel(new String[]{"Código", "Nombre", "Precio", "Categoría", "Imagen"}, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");
        JButton btnActualizar = new JButton("Actualizar Producto");
        JButton btnAbrirCompra = new JButton("Pantalla de Compra");
        JButton btnAbrirFactura = new JButton("Pantalla de Facturas");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAbrirCompra);
        panelBotones.add(btnAbrirFactura);
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnAgregar.addActionListener(e -> mostrarFormularioAgregarProducto());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        btnActualizar.addActionListener(e -> mostrarFormularioActualizarProducto());
        btnAbrirCompra.addActionListener(e -> abrirPantallaCompra());
        btnAbrirFactura.addActionListener(e -> abrirPantallaFacturas());
    }

    private void cargarProductosDesdeXML() {
        try {
            productos = xml.cargarProductos();
            for (Producto producto : productos) {
                modeloTabla.addRow(new Object[]{producto.getCodigo(), producto.getNombre(), producto.getPrecio(), producto.getCategoria(), producto.getImagen()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarFormularioAgregarProducto() {
        JDialog dialogo = new JDialog(this, "Agregar Producto", true);
        dialogo.setSize(400, 300);
        dialogo.setLayout(new GridLayout(6, 2, 10, 10));
        dialogo.setLocationRelativeTo(this);

      
        JTextField txtCodigo = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCategoria = new JTextField();
        JTextField txtImagen = new JTextField();

   
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        dialogo.add(new JLabel("Código:"));
        dialogo.add(txtCodigo);
        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(txtNombre);
        dialogo.add(new JLabel("Precio:"));
        dialogo.add(txtPrecio);
        dialogo.add(new JLabel("Categoría:"));
        dialogo.add(txtCategoria);
        dialogo.add(new JLabel("Imagen:"));
        dialogo.add(txtImagen);
        dialogo.add(btnGuardar);
        dialogo.add(btnCancelar);

        btnGuardar.addActionListener(e -> {
            try {
                String codigo = txtCodigo.getText().trim();
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                String categoria = txtCategoria.getText().trim();
                String imagen = txtImagen.getText().trim();

                if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty() || imagen.isEmpty()) {
                    throw new IllegalArgumentException("Todos los campos son obligatorios.");
                }

                Producto producto = new Producto(codigo, nombre, precio, categoria, imagen);
                productos.add(producto);

                modeloTabla.addRow(new Object[]{codigo, nombre, precio, categoria, imagen});
                guardarProductosEnXML(); // Guardar inmediatamente después de agregar
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
                dialogo.dispose();
            } catch (NumberFormatException ex1) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex2) {
                JOptionPane.showMessageDialog(this, ex2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());
        dialogo.setVisible(true);
    }

    private void mostrarFormularioActualizarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Producto producto = productos.get(filaSeleccionada);

            JDialog dialogo = new JDialog(this, "Actualizar Producto", true);
            dialogo.setSize(400, 300);
            dialogo.setLayout(new GridLayout(6, 2, 10, 10));
            dialogo.setLocationRelativeTo(this);

  
            JTextField txtCodigo = new JTextField(producto.getCodigo());
            JTextField txtNombre = new JTextField(producto.getNombre());
            JTextField txtPrecio = new JTextField(String.valueOf(producto.getPrecio()));
            JTextField txtCategoria = new JTextField(producto.getCategoria());
            JTextField txtImagen = new JTextField(producto.getImagen());


            JButton btnGuardar = new JButton("Guardar Cambios");
            JButton btnCancelar = new JButton("Cancelar");

            dialogo.add(new JLabel("Código:"));
            dialogo.add(txtCodigo);
            dialogo.add(new JLabel("Nombre:"));
            dialogo.add(txtNombre);
            dialogo.add(new JLabel("Precio:"));
            dialogo.add(txtPrecio);
            dialogo.add(new JLabel("Categoría:"));
            dialogo.add(txtCategoria);
            dialogo.add(new JLabel("Imagen:"));
            dialogo.add(txtImagen);
            dialogo.add(btnGuardar);
            dialogo.add(btnCancelar);

            btnGuardar.addActionListener(e -> {
                try {
                    String codigo = txtCodigo.getText().trim();
                    String nombre = txtNombre.getText().trim();
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    String categoria = txtCategoria.getText().trim();
                    String imagen = txtImagen.getText().trim();

                    if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty() || imagen.isEmpty()) {
                        throw new IllegalArgumentException("Todos los campos son obligatorios.");
                    }

 
                    producto.setCodigo(codigo);
                    producto.setNombre(nombre);
                    producto.setPrecio(precio);
                    producto.setCategoria(categoria);
                    producto.setImagen(imagen);

                    modeloTabla.setValueAt(codigo, filaSeleccionada, 0);
                    modeloTabla.setValueAt(nombre, filaSeleccionada, 1);
                    modeloTabla.setValueAt(precio, filaSeleccionada, 2);
                    modeloTabla.setValueAt(categoria, filaSeleccionada, 3);
                    modeloTabla.setValueAt(imagen, filaSeleccionada, 4);

                    guardarProductosEnXML(); // Guardar los cambios en el archivo XML
                    JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                    dialogo.dispose();
                } catch (NumberFormatException ex1) {
                    JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, ex2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnCancelar.addActionListener(e -> dialogo.dispose());
            dialogo.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada >= 0) {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de que desea eliminar este producto?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

  
            if (confirm == JOptionPane.YES_OPTION) {
                productos.remove(filaSeleccionada);
                modeloTabla.removeRow(filaSeleccionada);

                // Guardar los cambios en el archivo XML
                guardarProductosEnXML();

                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarProductosEnXML() {
        try {
            xml.guardarProductos(productos);
            JOptionPane.showMessageDialog(this, "Productos guardados correctamente en el archivo XML.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirPantallaCompra() {
        SwingUtilities.invokeLater(() -> {
            PantallaCompra pantallaCompra = new PantallaCompra(productos); // Pasar productos a la pantalla de compra
            pantallaCompra.setVisible(true);
        });
    }

    private void abrirPantallaFacturas() {
        SwingUtilities.invokeLater(() -> {
            PantallaFactura pantallaFacturas = new PantallaFactura(); // Abre la pantalla de facturas
            pantallaFacturas.setVisible(true);
        });
    }
}

