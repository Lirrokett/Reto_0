/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import excepciones.ConvocatoriaExcepcion;
import java.util.List;
import javax.security.auth.login.LoginException;
import modelo.ConvocatoriaExamen;
import modelo.Enunciado;
import modelo.UnidadDidactica;

/**
 *
 * @author 2dam
 */
public interface Dao {
    
    public void altaUD(UnidadDidactica unid) throws LoginException;
    
    public List<UnidadDidactica> obtenerTodasUD();
    
    List<Enunciado> obtenerTodosEnunciados();
    
    Enunciado obtenerEnunciadoPorId(int id);
    
    public void asignarEnunciadoAConvocatoria(int idEnunciado, String nombreConvocatoria);
    
     public void crearConvocatoria(ConvocatoriaExamen con) throws ConvocatoriaExcepcion;
    
    public void consultarConvocatoria(String nombreConvocatoria) throws ConvocatoriaExcepcion;
    
    public void crearEnunciadoConUnidadesYConvocatoria(
        modelo.Enunciado enunciado,
        java.util.List<Integer> idsUnidades,
        modelo.ConvocatoriaExamen convocatoria
    );
    
    public boolean existeUnidadDidactica(int idUnidad);
    
    public boolean existeConvocatoriaPorNombre(String nombreConvocatoria);
   
    public boolean existeEnunciado(int idEnunciado);
    
    public java.util.List<modelo.Enunciado> consultarEnunciadosPorUnidad(int idUnidad);
    
    public java.util.List<modelo.ConvocatoriaExamen> consultarConvocatoriasPorEnunciado(int idEnunciado);
}
