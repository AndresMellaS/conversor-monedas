package com.daniels.conversor.util;

import com.daniels.conversor.model.Conversion;
import com.daniels.conversor.model.Moneda;
import com.daniels.conversor.repository.HistorialRepository;
import com.daniels.conversor.service.ConversorService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    private final ConversorService    conversorService;
    private final HistorialRepository historial;
    private final Scanner             scanner;

    private static final Map<String, Moneda> MONEDAS = new LinkedHashMap<>();

    static {
        MONEDAS.put("1",  new Moneda("USD", "Dólar estadounidense"));
        MONEDAS.put("2",  new Moneda("EUR", "Euro"));
        MONEDAS.put("3",  new Moneda("BRL", "Real brasileño"));
        MONEDAS.put("4",  new Moneda("ARS", "Peso argentino"));
        MONEDAS.put("5",  new Moneda("CLP", "Peso chileno"));
        MONEDAS.put("6",  new Moneda("MXN", "Peso mexicano"));
        MONEDAS.put("7",  new Moneda("COP", "Peso colombiano"));
        MONEDAS.put("8",  new Moneda("PEN", "Sol peruano"));
        MONEDAS.put("9",  new Moneda("GBP", "Libra esterlina"));
        MONEDAS.put("10", new Moneda("JPY", "Yen japonés"));
    }

    public Menu(ConversorService conversorService,
                HistorialRepository historial) {
        this.conversorService = conversorService;
        this.historial        = historial;
        this.scanner          = new Scanner(System.in);
    }

    public void iniciar() {
        boolean activo = true;
        while (activo) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> realizarConversion();
                case "2" -> verHistorial();
                case "3" -> limpiarHistorial();
                case "0" -> activo = false;
                default  -> System.out.println("⚠ Opción inválida.\n");
            }
        }
        System.out.println("\n👋 ¡Hasta luego!");
    }

    private void mostrarMenu() {
        System.out.println("""
            ╔══════════════════════════════════╗
            ║       CONVERSOR DE MONEDAS       ║
            ╠══════════════════════════════════╣
            ║  1. Convertir moneda             ║
            ║  2. Ver historial                ║
            ║  3. Limpiar historial            ║
            ║  0. Salir                        ║
            ╚══════════════════════════════════╝
            Elige una opción:""");
    }

    private void realizarConversion() {
        System.out.println("\n── Monedas disponibles ──");
        MONEDAS.forEach((k, v) ->
                System.out.printf("  %2s. %s%n", k, v));

        Moneda origen  = seleccionarMoneda("Moneda origen");
        Moneda destino = seleccionarMoneda("Moneda destino");

        System.out.print("Monto a convertir: ");
        double monto;
        try {
            monto = Double.parseDouble(scanner.nextLine().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("⚠ Monto inválido.\n");
            return;
        }

        System.out.println("⏳ Consultando API...");
        try {
            Conversion conversion = conversorService.convertir(origen, destino, monto);
            historial.guardar(conversion);
            System.out.println("\n✅ " + conversion + "\n");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage() + "\n");
        }
    }

    private Moneda seleccionarMoneda(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + " (número): ");
            String op = scanner.nextLine().trim();
            if (MONEDAS.containsKey(op)) return MONEDAS.get(op);
            System.out.println("⚠ Opción inválida, intenta de nuevo.");
        }
    }

    private void verHistorial() {
        List<Conversion> lista = historial.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("\n📭 Sin conversiones aún.\n");
            return;
        }
        System.out.println("\n── Historial (" + historial.total() + " conversiones) ──");
        lista.forEach(c -> System.out.println("  " + c));
        System.out.println();
    }

    private void limpiarHistorial() {
        historial.limpiar();
        System.out.println("🗑 Historial limpiado.\n");
    }
}