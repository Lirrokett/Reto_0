package controlador;

public interface Dao {

    // Crea una nueva convocatoria
    public void crearConvocatoria(int id, String nombre, String fecha, String descripcion);

    // Consulta una convocatoria por ID
    public void consultarConvocatoria(int id);

}
