package com.example.myapplication4.Clases;
//Agregar id a la categoría
public class Categoria {
    private String nombre;
    private String desc;
    private String color;
    private String tipo;

    public Categoria(String nombre, String desc, String color, String tipo){
        this.nombre = nombre;
        this.desc = desc;
        this.color = color;
        this.tipo = tipo;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDesc() {
        return desc;
    }

    public String getColor() {
        return color;
    }

    public String getTipo() {
        return tipo;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
