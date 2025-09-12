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
    private Dificultad nivel;
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

    public Dificultad getNivel() {
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

    public void setNivel(Dificultad nivel) {
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
        return "Enunciado{" + "id=" + id + ", descripcion=" + descripcion + ", nivel=" + nivel + ", disponible=" + disponible + ", ruta=" + ruta + '}';
    }
    
    
}
