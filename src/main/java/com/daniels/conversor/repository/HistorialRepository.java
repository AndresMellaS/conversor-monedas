package com.daniels.conversor.repository;

import com.daniels.conversor.model.Conversion;
import com.daniels.conversor.model.Moneda;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class HistorialRepository {

    private static final String ARCHIVO = "historial.json";
    private final List<Conversion> historial;
    private final Gson gson;

    public HistorialRepository() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        this.historial = new ArrayList<>(cargarDesdeArchivo());
    }

    // ── Operaciones principales ──────────────────────────────

    public void guardar(Conversion conversion) {
        historial.add(conversion);
        guardarEnArchivo();
    }

    public List<Conversion> obtenerTodos() {
        return Collections.unmodifiableList(historial);
    }

    public List<Conversion> obtenerUltimos(int n) {
        int size  = historial.size();
        int desde = Math.max(0, size - n);
        return Collections.unmodifiableList(historial.subList(desde, size));
    }

    public void limpiar() {
        historial.clear();
        guardarEnArchivo();
    }

    public int total() {
        return historial.size();
    }

    // ── Persistencia ─────────────────────────────────────────

    private void guardarEnArchivo() {
        try {
            List<ConversionDTO> dtos = historial.stream()
                    .map(ConversionDTO::new)
                    .toList();
            String json = gson.toJson(dtos);
            Files.writeString(Path.of(ARCHIVO), json);
        } catch (IOException e) {
            System.err.println("⚠ Error al guardar historial: " + e.getMessage());
        }
    }

    private List<Conversion> cargarDesdeArchivo() {
        Path path = Path.of(ARCHIVO);
        if (!Files.exists(path)) return new ArrayList<>();

        try {
            String json = Files.readString(path);
            Type listType = new TypeToken<List<ConversionDTO>>(){}.getType();
            List<ConversionDTO> dtos = gson.fromJson(json, listType);
            if (dtos == null) return new ArrayList<>();
            return dtos.stream()
                    .map(ConversionDTO::toConversion)
                    .toList();
        } catch (IOException e) {
            System.err.println("⚠ Error al cargar historial: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ── DTO interno para serialización ───────────────────────

    private static class ConversionDTO {
        String desde;
        String hasta;
        double monto;
        double resultado;
        double tasa;
        LocalDateTime timestamp;

        ConversionDTO(Conversion c) {
            this.desde     = c.getOrigen().getCodigo();
            this.hasta     = c.getDestino().getCodigo();
            this.monto     = c.getMonto();
            this.resultado = c.getResultado();
            this.tasa      = c.getTasa();
            this.timestamp = c.getTimestamp();
        }

        Conversion toConversion() {
            Moneda origen  = new Moneda(desde, desde);
            Moneda destino = new Moneda(hasta, hasta);
            return new Conversion(origen, destino, monto, resultado, tasa, timestamp);
        }
    }

    // ── Adapter para LocalDateTime ────────────────────────────

    private static class LocalDateTimeAdapter
            implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        @Override
        public JsonElement serialize(LocalDateTime src, Type type,
                                     JsonSerializationContext ctx) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type,
                                         JsonDeserializationContext ctx) {
            return LocalDateTime.parse(json.getAsString());
        }
    }
}
