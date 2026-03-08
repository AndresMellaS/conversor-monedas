package com.daniels.conversor.model;

public class Moneda {

    private final String codigo;
    private final String nombre;

    public Moneda(String codigo, String nombre) {
        this.codigo = codigo.toUpperCase();
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return codigo + " — " + nombre;
    }
}
