/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Archivos;


import clasesPrincipales.Producto;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XML {
    private static final String FILE_NAME = "productos.xml";

    public List<Producto> cargarProductos() throws Exception {
        List<Producto> productos = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return productos;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("Producto");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String codigo = element.getElementsByTagName("Codigo").item(0).getTextContent();
                String nombre = element.getElementsByTagName("Nombre").item(0).getTextContent();
                double precio = Double.parseDouble(element.getElementsByTagName("Precio").item(0).getTextContent());
                String categoria = element.getElementsByTagName("Categoria").item(0).getTextContent();
                String imagen = element.getElementsByTagName("Imagen").item(0).getTextContent();

                Producto producto = new Producto(codigo, nombre, precio, categoria, imagen);
                productos.add(producto);
            }
        }

        return productos;
    }

    public void guardarProductos(List<Producto> productos) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("Productos");
        document.appendChild(root);

        for (Producto producto : productos) {
            Element productoElement = document.createElement("Producto");

            Element codigo = document.createElement("Codigo");
            codigo.appendChild(document.createTextNode(producto.getCodigo()));
            productoElement.appendChild(codigo);

            Element nombre = document.createElement("Nombre");
            nombre.appendChild(document.createTextNode(producto.getNombre()));
            productoElement.appendChild(nombre);

            Element precio = document.createElement("Precio");
            precio.appendChild(document.createTextNode(String.valueOf(producto.getPrecio())));
            productoElement.appendChild(precio);

            Element categoria = document.createElement("Categoria");
            categoria.appendChild(document.createTextNode(producto.getCategoria()));
            productoElement.appendChild(categoria);

            Element imagen = document.createElement("Imagen");
            imagen.appendChild(document.createTextNode(producto.getImagen()));
            productoElement.appendChild(imagen);

            root.appendChild(productoElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(FILE_NAME));
        transformer.transform(source, result);
    }
}
