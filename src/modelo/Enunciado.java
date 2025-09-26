/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author 2dam
 */
public class Enunciado {
    private Integer id;
    private String descripcion;
    private Nivel nivel;
    private boolean disponible;
    private String ruta;

    public Enunciado() {
    }

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public String getRuta() {
        return ruta;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
               "ID: " + id + "\n" +
               "Descripción: " + descripcion + "\n" +
               "Nivel: " + nivel + "\n" +
               "Disponible: " + (disponible ? "Sí" : "No") + "\n" +
               "Ruta: " + ruta + "\n" +
               "----------------------------------------";
    }
    
    
}
