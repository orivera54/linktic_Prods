package com.linktic.productoservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("nombre")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @JsonProperty("precio")
    private BigDecimal precio;

    @JsonProperty("descripcion")
    private String descripcion;

    // Constructors
    public ProductoRequest() {}

    public ProductoRequest(String nombre, BigDecimal precio, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ProductoRequest{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}