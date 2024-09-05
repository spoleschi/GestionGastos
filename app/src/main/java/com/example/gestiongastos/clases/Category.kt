package com.example.gestiongastos.clases

//La data class en Kotlin genera automáticamente:
//Constructor primario
//Getters y setters: Para acceder y modificar los valores de las propiedades.
//equals(): Para comparar dos objetos de la clase en función de sus propiedades.
//hashCode(): Para calcular un valor hash único para el objeto.
//toString(): Para generar una representación de cadena del objeto, incluyendo los valores de sus propiedades.
//copy(): Para crear una copia del objeto con la posibilidad de modificar algunos de sus valores.

data class Category(
    val nombre: String,
    val desc: String,
    val color: String,
    val tipoCategoria: String
)

