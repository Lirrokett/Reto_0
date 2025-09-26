package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DaoImplementacion implements Dao {

    // 1. Instancia única (estática y privada)
    private static DaoImplementacion instancia;

    // 2. Atributos de configuración
    private ResourceBundle configFile;
    private String urlBD;
    private String userBD;
    private String passwordBD;

    private Connection con;
    private PreparedStatement stmt;

    // Consulta de ejemplo
    final String ALTA_TREN = "INSERT INTO TREN (ID, nombre, plazas, plazas_ocupadas, precio, origen, destino) VALUES (?, ?, ?, ?, ?, ?, ?)";

    // 3. Constructor privado
    private DaoImplementacion() {
        this.configFile = ResourceBundle.getBundle("modelo.configClase");
        this.urlBD = this.configFile.getString("Conn");
        this.userBD = this.configFile.getString("DBUser");
        this.passwordBD = this.configFile.getString("DBPass");
    }

    // 4. Método estático público para obtener la única instancia
    public static DaoImplementacion getInstance() {
        if (instancia == null) {
            instancia = new DaoImplementacion();
        }
        return instancia;
    }

    // 5. Abrir conexión
    private void openConnection() throws ClassNotFoundException {
        try {
            con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD: " + e.getMessage());
        }
    }

    // 6. Cerrar conexión
    private void closeConnection() throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
    }

    // Aquí irán tus métodos CRUD (insertar, eliminar, listar, etc.)
    public void altaTren(int id, String nombre, int plazas, int ocupadas, double precio, String origen, String destino) {
        try {
            openConnection();
            stmt = con.prepareStatement(ALTA_TREN);
            stmt.setInt(1, id);
            stmt.setString(2, nombre);
            stmt.setInt(3, plazas);
            stmt.setInt(4, ocupadas);
            stmt.setDouble(5, precio);
            stmt.setString(6, origen);
            stmt.setString(7, destino);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en altaTren: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    @Override
    public void crearConvocatoria(int id, String nombre, String fecha, String descripcion) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void consultarConvocatoria(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
