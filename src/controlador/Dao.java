package controlador;

public interface Dao {

    // Crea una nueva convocatoria
    void crearConvocatoria(int id, String nombre, String fecha, String descripcion);

    // Consulta una convocatoria por ID
    void consultarConvocatoria(int id);

}
