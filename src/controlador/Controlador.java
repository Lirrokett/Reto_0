/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import utilidades.Util;

public class Controlador {
    
    private DaoImplementacion dao;
        
        
    public static void iniciar() {
        int opc;
        
        do {
            
            opc = menu();
            
            
            switch (opc) {
            case 1:
             
                break;
            case 2:
                
                break;
            case 3:
                
                break;
            case 4:
               
                break;
            case 5:
                
                break;
            case 6:
              
                break;
            case 7:
                
                break;
            default:
                System.out.println("Opción no válida");
                break;
        }
            
            
        } while (opc != 7);
        
        System.out.println("¡Hasta pronto!");
    }
    
    /**
     * Método que procesa la opción seleccionada por el usuario
     * @param opcion La opción seleccionada del menú
     */

    private static int menu() {
            int opc;
        
            System.out.println("1. Crear Unidad Didacta");
            System.out.println("2. Crear Convocatoria");
            System.out.println("3. Crear Enunciado");
            System.out.println("4. Consultar Enunciado");
            System.out.println("5. Consultar Convocatorias");
            System.out.println("6. Visualizar Enunciado");
            System.out.println("7. Salir");
            
            opc = Util.leerInt("Seleccione una opción (1-7):", 1, 7);
            
            return opc;
    }
    
}
