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
                    // Crear Convocatoria
                    break;
                case 3:
                    // Crear Enunciado
                    break;
                case 4:
                    // Consultar Enunciado
                    break;
                case 5:
                    mostrarUnidades();
                    break;
                case 6:
                    mostrarTodosEnunciados();
                    break;
                case 7:
                    mostrarEnunciadoPorId();
                    break;
                case 8:
                    asignarEnunciadoConvocatoria();
                    break;

                default:
                    System.out.println("Opción no válida");
                    break;
            }

        } while (opc != 9);

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
        System.out.println("7. Mostrar enunciado por ID y abrir archivo");
        System.out.println("8. Asignar Enunciado a Convocatoria");
        System.out.println("9. Salir");

        opc = Util.leerInt("Seleccione una opción (1-9):", 1, 7);

        return opc;
    }

    private static void crearUnidad() throws LoginException {
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

    private static void mostrarEnunciadoPorId() {
        int idBuscado = Util.leerInt("Ingrese el ID del enunciado que desea visualizar:");
        Enunciado enSeleccionado = dao.obtenerEnunciadoPorId(idBuscado);

        if (enSeleccionado == null) {
            System.out.println("No se encontró un enunciado con ID " + idBuscado);
            return;
        }

        // Mostrar información del enunciado
        System.out.println("\n=== Información del Enunciado ===");
        System.out.println("ID: " + enSeleccionado.getId());
        System.out.println("Descripción: " + enSeleccionado.getDescripcion());
        System.out.println("Nivel: " + enSeleccionado.getNivel());
        System.out.println("Disponible: " + enSeleccionado.isDisponible());
        System.out.println("Ruta archivo: " + (enSeleccionado.getRuta() != null ? enSeleccionado.getRuta() : "No asignado"));

        if (enSeleccionado.getRuta() != null) {
            String respuesta = Util.leerString("¿Desea abrir el archivo asociado? (S/N):").trim().toUpperCase();
            if (respuesta.equals("S")) {
                java.io.File archivo = new java.io.File(enSeleccionado.getRuta());

                System.out.println("Ruta que Java intenta abrir: " + archivo.getAbsolutePath());
                System.out.println("¿Existe archivo? " + archivo.exists());

                if (archivo.exists() && java.awt.Desktop.isDesktopSupported()) {
                    try {
                        java.awt.Desktop.getDesktop().open(archivo);
                        System.out.println("Abriendo archivo...");
                    } catch (Exception e) {
                        System.out.println("Error al abrir el archivo: " + e.getMessage());
                    }
                } else {
                    System.out.println("El archivo no existe o Desktop no soportado.");
                }
            }
        } else {
            System.out.println("ℹ Este enunciado no tiene un archivo asociado.");
        }
    }

     private static void asignarEnunciadoConvocatoria() {
        int idEnunciado = Util.leerInt("Ingrese el ID del enunciado a asignar:");
        String convocatoria = Util.leerString("Ingrese el nombre de la convocatoria:");

        dao.asignarEnunciadoAConvocatoria(idEnunciado, convocatoria);
    }
}
