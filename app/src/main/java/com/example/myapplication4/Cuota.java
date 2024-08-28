package com.example.myapplication4;
import java.time.LocalDate;
public class Cuota {
    private int nroCuota;
    private LocalDate fechaPago;

    public Cuota(int nroCuota, LocalDate fechaPago){
        this.nroCuota = nroCuota;
        this.fechaPago = fechaPago;
    }
}
