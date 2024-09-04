package com.example.myapplication4;

import java.math.BigDecimal;

public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String pwd;
    private BigDecimal saldo;

    public Usuario(int id, String nombre, String username, String pwd, BigDecimal saldo){
        this.id = this.id;
        this.nombre = nombre;
        this.username = username;
        this.pwd = pwd;
        this.saldo = saldo;
    }

    public void actulizarSaldo(BigDecimal s){
        this.saldo = s;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
