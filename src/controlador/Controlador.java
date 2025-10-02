/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import excepciones.ConvocatoriaExcepcion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import modelo.ConvocatoriaExamen;
import modelo.Enunciado;
import modelo.Nivel;
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
                    crearConvocatoriaExamen();
                    break;
                case 3:
                    crearEnunciadoYAsociar();
                    break;
                case 4:
                    consultarConvocatoria();
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
                case 9:
                    consultarEnunciadosPorUnidad();
                    break;
                case 10:
                    consultarConvocatoriasPorEnunciado();
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }

        } while (opc != 11);

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
        System.out.println("4. Consultar convocatoria");
        System.out.println("5. Mostrar unidades didacticas");
        System.out.println("6. Visualizar todos los Enunciado");
        System.out.println("7. Mostrar enunciado por ID y abrir archivo");
        System.out.println("8. Asignar Enunciado a Convocatoria");
        System.out.println("9. Consultar enunciados por unidad didáctica");
        System.out.println("10. Consultar convocatorias por enunciado");
        System.out.println("11. Salir");

        opc = Util.leerInt("Seleccione una opción (1-11):", 1, 11);

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

    private static void crearEnunciadoYAsociar() {
        System.out.println("Crear un enunciado y agregando  a las  unidades didacticas  y convocatoria");

        // ===== ENUNCIADO =====
        System.out.println("Introduce el ID del enunciado:");
        int idEnunciado = Util.leerInt();

        System.out.println("Introduce la descripción del enunciado:");
        String descEnunciado = Util.introducirCadena();

        Nivel nivel = null;
        do {
            System.out.println("Introduce el nivel de dificultad (Alta, Media o Baja):");
            String nivelStr = Util.introducirCadena().trim().toLowerCase();
            switch (nivelStr) {
                case "alta":
                    nivel = Nivel.ALTA;
                    break;
                case "media":
                    nivel = Nivel.MEDIA;
                    break;
                case "baja":
                    nivel = Nivel.BAJA;
                    break;
                default:
                    System.out.println("Nivel inválido. Debe ser Alta, Media o Baja.");
            }
        } while (nivel == null);

        System.out.println("¿Está disponible? (S/N, 1/0, true/false):");
        boolean disponible = Util.esBoolean();

        System.out.println("Introduce la ruta del archivo del enunciado:");
        String ruta = Util.introducirCadena();

        // ===== UNIDADES DIDÁCTICAS =====
        System.out.println("Introduce los ID de las unidades didácticas EXISTENTES separados por comas (ej: 1,3,5):");
        String unidadesInput = Util.introducirCadena();
        List<Integer> idsUnidades = new ArrayList<>();
        if (unidadesInput != null) {
            String limpio = unidadesInput.replace("\u00A0", " ").trim();
            String[] partes = limpio.split("[,;]");
            for (String p : partes) {
                String t = p.trim();
                if (t.isEmpty()) {
                    continue;
                }
                if (!t.matches("\\d+")) {
                    System.out.println("⚠️ Se ignora valor no numérico: '" + t + "'");
                    continue;
                }
                try {
                    idsUnidades.add(Integer.parseInt(t));
                } catch (NumberFormatException ex) {
                    System.out.println("⚠️ Número fuera de rango para int: '" + t + "'");
                }
            }
        }

        if (idsUnidades.isEmpty()) {
            System.out.println("❌ No se han introducido unidades didácticas válidas. Operación cancelada.");
            return;
        }

        // ===== Validar que las UDs existen =====
        List<Integer> noExistentes = new ArrayList<>();
        for (int idUD : idsUnidades) {
            if (!dao.existeUnidadDidactica(idUD)) {
                noExistentes.add(idUD);
            }
        }
        if (!noExistentes.isEmpty()) {
            System.out.println("❌ Las siguientes unidades didácticas no existen en la BD: " + noExistentes);
            System.out.println("👉 Crea primero esas UDs o corrige los IDs.");
            return;
        }

        // ===== CONVOCATORIA =====
        System.out.println("Introduce el NOMBRE de la convocatoria existente a la que quieres asociar el enunciado:");
        String nombreConv = Util.introducirCadena();

        if (!dao.existeConvocatoriaPorNombre(nombreConv)) {
            System.out.println("❌ La convocatoria '" + nombreConv + "' no existe en la BD. Operación cancelada.");
            return;
        }

        // ===== Crear Enunciado =====
        Enunciado enunciado = new Enunciado();
        enunciado.setId(idEnunciado);
        enunciado.setDescripcion(descEnunciado);
        enunciado.setNivel(nivel);
        enunciado.setDisponible(disponible);
        enunciado.setRuta(ruta);

        // ===== Resumen =====
        System.out.println("\n=== Resumen de datos introducidos ===");
        System.out.println(enunciado);
        System.out.println("Unidades didácticas: " + idsUnidades);
        System.out.println("Convocatoria: " + nombreConv);

        // ===== Llamar al DAO =====
        try {
            modelo.ConvocatoriaExamen conv = new modelo.ConvocatoriaExamen();
            conv.setConvocatoria(nombreConv);   // solo usamos el nombre
            dao.crearEnunciadoConUnidadesYConvocatoria(enunciado, idsUnidades, conv);
            System.out.println("✅ Enunciado creado y asociado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al crear el enunciado: " + e.getMessage());
        }
    }

    private static void consultarEnunciadosPorUnidad() {
        System.out.println("=== Consultar enunciados por unidad didáctica ===");
        System.out.println("Introduce el ID de la unidad didáctica:");
        int idUnidad = Util.leerInt();

        java.util.List<Enunciado> enunciados = dao.consultarEnunciadosPorUnidad(idUnidad);

        if (enunciados.isEmpty()) {
            System.out.println("No se encontraron enunciados para la unidad " + idUnidad);
        } else {
            System.out.println("Enunciados que tratan la unidad " + idUnidad + ":");
            for (Enunciado e : enunciados) {
                System.out.println(e);
            }
        }
    }

    /**
     * Pide un ID de enunciado y lista por consola todas las convocatorias en las que se utiliza.
     */
    private static void consultarConvocatoriasPorEnunciado() {
        System.out.println("=== Consultar convocatorias por enunciado ===");
        System.out.println("Introduce el ID del enunciado:");
        int idEnunciado = Util.leerInt();

        java.util.List<ConvocatoriaExamen> convocatorias = dao.consultarConvocatoriasPorEnunciado(idEnunciado);

        if (convocatorias.isEmpty()) {
            System.out.println("No se encontraron convocatorias para el enunciado " + idEnunciado);
        } else {
            System.out.println("Convocatorias que utilizan el enunciado " + idEnunciado + ":");
            for (ConvocatoriaExamen c : convocatorias) {
                System.out.println(
                        "Convocatoria=" + c.getConvocatoria()
                        + ", Descripción=" + c.getDescripcion()
                        + ", Fecha=" + c.getFecha()
                        + ", Curso=" + c.getCurso()
                );
            }
        }
    }

    private static void crearConvocatoria(ConvocatoriaExamen con) throws ConvocatoriaExcepcion {
        dao.crearConvocatoria(con);
    }

    private static void crearConvocatoriaExamen() {
        boolean repetir = true;

        while (repetir) {
            try {
                System.out.println("Introduce la convocatoria");
                String convocatoria = Util.introducirCadena();
                System.out.println("Introduce la descripcion");
                String descripcion = Util.introducirCadena();
                System.out.println("Introduce la fecha (dd/MM/yyyy)");
                LocalDate fecha = Util.leerFechaDMA();
                System.out.println("Introduce el curso");
                String curso = Util.introducirCadena();
                int idEnunciado = Util.leerInt("Introduce el enunciado");

                if (!dao.existeEnunciado(idEnunciado)) {
                    System.out.println("Error: el enunciado " + idEnunciado + " no existe.");
                    System.out.println("¿Quieres volver a intentarlo? (s/n)");
                    String respuesta = Util.introducirCadena().trim().toLowerCase();

                    if (respuesta.equals("s") || respuesta.equals("si")) {
                        repetir = true;
                    } else {
                        repetir = false;
                    }
                } else {
                    ConvocatoriaExamen con = new ConvocatoriaExamen(convocatoria, descripcion, fecha, curso, idEnunciado);
                    crearConvocatoria(con);
                    System.out.println("Convocatoria creada en la base de datos.");
                    repetir = false;
                }

            } catch (ConvocatoriaExcepcion e) {
                System.out.println(e.getMessage());
                System.out.println("¿Quieres volver a intentarlo? (s/n)");
                String respuesta = Util.introducirCadena().trim().toLowerCase();
                repetir = (respuesta.equals("s") || respuesta.equals("si"));
            }
        }
    }

    private static void consultarConvocatoria() {

        System.out.println("Introduce la convocatoria que quieres consultar:");
        String nombreConvocatoria = Util.introducirCadena();
        try {
            dao.consultarConvocatoria(nombreConvocatoria);
        } catch (ConvocatoriaExcepcion e) {
            e.printStackTrace();
        }
    }
}
