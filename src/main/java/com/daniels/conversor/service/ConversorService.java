package com.daniels.conversor.service;

import com.daniels.conversor.model.Conversion;
import com.daniels.conversor.model.Moneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversorService {
    private final ApiService apiService;

    @Autowired
    public ConversorService(ApiService apiService){
        this.apiService = apiService;
    }

    public Conversion convertir (Moneda origen, Moneda destino, double monto) throws Exception{

        double tasa = apiService.obtenerTasa(origen.getCodigo(), destino.getCodigo());
        double resultado = monto * tasa;
        return new Conversion(origen, destino, monto, resultado, tasa);

    }







}