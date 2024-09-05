package com.example.myapplication4.Clases;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaccion {
    private int id;
    private String desc;
    private BigDecimal monto;
    private LocalDate fecha;
    private Categoria categoria;

    public Transaccion(int id, String desc, BigDecimal monto, LocalDate fecha, int cantCuotas, BigDecimal interes, Categoria categoria){
        this.id = id;
        this.desc = desc;
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public Transaccion(int id, String desc, BigDecimal monto, LocalDate fecha, Categoria categoria){
        this.id = id;
        this.desc = desc;
        this.monto = monto;
        this.fecha = fecha;
    }
}
