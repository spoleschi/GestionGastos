package com.example.myapplication4.Clases;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Gasto extends Transaccion{
    private int cantCuotas;
    private BigDecimal interes;
    private ArrayList<Cuota> cuotas;

    public Gasto(int id, String desc, BigDecimal monto, LocalDate fecha, int cantCuotas, BigDecimal interes, Categoria categoria){
        super(id, desc, monto, fecha, cantCuotas, interes, categoria);

        cuotas = new ArrayList<Cuota>();
        LocalDate fechaPagar = LocalDate.now();
        for(int i=0;i<cantCuotas;i++){
            Cuota cuota = new Cuota(i+1,fechaPagar.plusMonths(i));
        }
    }
}
