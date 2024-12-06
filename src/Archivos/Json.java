/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Archivos;

import clasesPrincipales.Factura;
import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Json {
    private static final String FILE_NAME = "facturas.json";

    // Método para cargar facturas desde el archivo JSON
    public List<Factura> cargarFacturas() throws Exception {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>(); // Si el archivo no existe, devuelve una lista vacía
        }

        Gson gson = new Gson();
        try (Reader reader = new FileReader(file)) {
            Factura[] facturasArray = gson.fromJson(reader, Factura[].class);
            return new ArrayList<>(Arrays.asList(facturasArray));
        }
    }

    // Método para guardar facturas en el archivo JSON
    public void guardarFacturas(List<Factura> facturas) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(facturas, writer); // Escribe las facturas en el archivo JSON
        }
    }
}
