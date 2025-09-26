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
import utilidades.Util;

public class Controlador {

    private static Dao dao = new DaoImplementacion();

    public static void iniciar() throws LoginException {
        int opc;

        do {

            opc = menu();

            switch (opc) {
                case 1:
                    crearUnidad();
                    break;
                case 2:
                   
                    break;
                case 3:

                    break;
                case 4:
                    
                    break;
                case 5:
                    mostrarUnidades();
                    break;
                case 6:
                    mostrarTodosEnunciados();
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
     *
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

    private static void crearUnidad() throws LoginException{ 
        int id = Util.leerInt("Ingrese ID de la Unidad Didáctica:");
        String acronimo = Util.leerString("Ingrese acrónimo:");
        String titulo = Util.leerString("Ingrese título:");
        String evaluacion = Util.leerString("Ingrese tipo de evaluación:");
        String descripcion = Util.leerString("Ingrese descripción:");

        UnidadDidactica ud = new UnidadDidactica();
        ud.setId(id);
        ud.setAcronimo(acronimo);
        ud.setTitulo(titulo);
        ud.setEvaluacion(evaluacion);
        ud.setDescripcion(descripcion);

        try {
            dao.altaUD(ud);
            System.out.println("Unidad Didáctica guardada en la base de datos.");
        } catch (Exception e) {
            System.out.println("Error al guardar la Unidad Didáctica: " + e.getMessage());
        }
    }

    private static void mostrarUnidades() {
         List<UnidadDidactica> unidades = dao.obtenerTodasUD();

        if (unidades.isEmpty()) {
            System.out.println("No hay Unidades Didácticas registradas.");
        } else {
            System.out.println("\n=== Lista de Unidades Didácticas ===");
            for (UnidadDidactica ud : unidades) {
                System.out.println(ud);
            }
        }
    }
    
    private static void mostrarTodosEnunciados() {
    // Llamamos al DAO para obtener la lista de enunciados
    List<Enunciado> enunciados = dao.obtenerTodosEnunciados();

    // Verificamos si la lista está vacía
    if (enunciados.isEmpty()) {
        System.out.println("No hay Enunciados registrados.");
    } else {
        System.out.println("\n=== Lista de Enunciados ===");
        for (Enunciado en : enunciados) {
            System.out.println(en); // Llama automáticamente al toString() del modelo
        }
    }
}

}

