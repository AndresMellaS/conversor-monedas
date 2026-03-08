package com.daniels.conversor.controller;

import com.daniels.conversor.model.Conversion;
import com.daniels.conversor.model.Moneda;
import com.daniels.conversor.repository.HistorialRepository;
import com.daniels.conversor.service.ConversorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ConversorController {

    private final ConversorService    conversorService;
    private final HistorialRepository historial;

    @Autowired
    public ConversorController(ConversorService conversorService,
                               HistorialRepository historial) {
        this.conversorService = conversorService;
        this.historial        = historial;
    }

    // POST /api/convertir
    @PostMapping("/convertir")
    public ResponseEntity<?> convertir(@RequestBody Map<String, Object> body) {
        try {
            String desde = (String) body.get("desde");
            String hasta = (String) body.get("hasta");
            double monto = Double.parseDouble(body.get("monto").toString());

            Moneda origen  = new Moneda(desde, desde);
            Moneda destino = new Moneda(hasta, hasta);

            Conversion conversion = conversorService.convertir(origen, destino, monto);
            historial.guardar(conversion);

            return ResponseEntity.ok(Map.of(
                    "desde",     conversion.getOrigen().getCodigo(),
                    "hasta",     conversion.getDestino().getCodigo(),
                    "monto",     conversion.getMonto(),
                    "resultado", conversion.getResultado(),
                    "tasa",      conversion.getTasa(),
                    "timestamp", conversion.getTimestamp().toString()
            ));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/historial
    @GetMapping("/historial")
    public ResponseEntity<?> historial() {
        List<Map<String, Object>> response = historial.obtenerTodos()
                .stream()
                .map(c -> Map.<String, Object>of(
                        "desde",     c.getOrigen().getCodigo(),
                        "hasta",     c.getDestino().getCodigo(),
                        "monto",     c.getMonto(),
                        "resultado", c.getResultado(),
                        "tasa",      c.getTasa(),
                        "timestamp", c.getTimestamp().toString()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    // DELETE /api/historial
    @DeleteMapping("/historial")
    public ResponseEntity<?> limpiarHistorial() {
        historial.limpiar();
        return ResponseEntity.ok(Map.of("mensaje", "Historial limpiado"));
    }
}