/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.List;
import javax.security.auth.login.LoginException;
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
}
