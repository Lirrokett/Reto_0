package controlador;

import utilidades.Util;

public class Controlador {

    // 🔑 Trabajamos con la INTERFAZ, no con la implementación concreta
    private Dao dao;
    
    public Controlador() {
        // Asignamos el Singleton de la implementación
        this.dao = DaoImplementacion.getInstance();
    }
    
    public void iniciar() {
        int opc;
        
        do {
            mostrarMenu();
            opc = Util.leerInt("Elige una opción", 1, 7);
            switch (opc) {
                case 1:
                   
                    break;
                case 2:
                    crearConvocatoria();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    consultarConvocatoria();
                    break;
                case 6:
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (opc != 7);
    }

    private void mostrarMenu() {
        System.out.println("=== MENÚ PRINCIPAL ===");
        System.out.println("1. Opción 1");
        System.out.println("2. Crear convocatoria");
        System.out.println("3. Opción 3");
        System.out.println("4. Opción 4");
        System.out.println("5. Consultar convocatoria");
        System.out.println("6. Opción 6");
        System.out.println("7. Salir");
    }

    private void crearConvocatoria() {
        // Aquí llamas a tu DAO Singleton a través de la interfaz
        System.out.println("Creando convocatoria...");
        // Ejemplo: dao.crearConvocatoria("Convocatoria prueba");
    }

    private void consultarConvocatoria() {
        System.out.println("Consultando convocatoria...");
        // Ejemplo: dao.consultarConvocatoria(1);
    }
}
