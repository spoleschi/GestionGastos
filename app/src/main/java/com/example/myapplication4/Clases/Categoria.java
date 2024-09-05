package com.example.myapplication4.Clases;

public class Categoria {
    private String nombre;
    private String desc;
    private String color;
    private String tipoCategoria;

    public Categoria(String nombre, String desc, String color, String tipoCategoria){
        this.nombre = nombre;
        this.desc = desc;
        this.color = color;
        this.tipoCategoria = tipoCategoria;
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

    public String getTipoCategoria() {
        return tipoCategoria;
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

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }
}
