package com.example.myapplication4;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ingreso extends Transaccion {

    public Ingreso(int id, String desc, BigDecimal monto, LocalDate fecha, Categoria categoria){
        super(id, desc, monto, fecha, categoria);
    }
}
