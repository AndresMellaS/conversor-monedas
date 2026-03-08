package com.daniels.conversor.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Conversion {

    private final Moneda origen;
    private final Moneda destino;
    private final double monto;
    private final double resultado;
    private final double tasa;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Conversion(Moneda origen, Moneda destino,
                      double monto, double resultado, double tasa) {
        this.origen    = origen;
        this.destino   = destino;
        this.monto     = monto;
        this.resultado = resultado;
        this.tasa      = tasa;
        this.timestamp = LocalDateTime.now();
    }

    public Moneda getOrigen()    { return origen; }
    public Moneda getDestino()   { return destino; }
    public double getMonto()     { return monto; }
    public double getResultado() { return resultado; }
    public double getTasa()      { return tasa; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format(
                "[%s]  %.2f %s → %.2f %s  (tasa: %.6f)",
                timestamp.format(FORMATTER),
                monto,    origen.getCodigo(),
                resultado, destino.getCodigo(),
                tasa
        );
    }
    public Conversion(Moneda origen, Moneda destino,
                      double monto, double resultado,
                      double tasa, LocalDateTime timestamp) {
        this.origen    = origen;
        this.destino   = destino;
        this.monto     = monto;
        this.resultado = resultado;
        this.tasa      = tasa;
        this.timestamp = timestamp;
    }

}