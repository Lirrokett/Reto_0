/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author 2dam
 */
public class ConvocatoriaExamen {

    private String convocatoria;
    private String descripcion;
    private LocalDate fecha;
    private String curso;
    private int idEnunciado;

    public ConvocatoriaExamen() {
    }

    public ConvocatoriaExamen(String convocatoria, String descripcion, LocalDate fecha, String curso, int idEnunciado) {
        this.convocatoria = convocatoria;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.curso = curso;
        this.idEnunciado = idEnunciado;
    }

    public String getConvocatoria() {
        return convocatoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getCurso() {
        return curso;
    }

    public void setConvocatoria(String convocatoria) {
        this.convocatoria = convocatoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getIdEnunciado() {
        return idEnunciado;
    }

    public void setIdEnunciado(int idEnunciado) {
        this.idEnunciado = idEnunciado;
    }
    
    @Override
    public String toString() {
        return "ConvocatoriaExamen{" + "convocatoria=" + convocatoria + ", descripcion=" + descripcion + ", fecha=" + fecha + ", curso=" + curso + '}';
    }

}
