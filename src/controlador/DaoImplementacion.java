/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import excepciones.ConvocatoriaExcepcion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import modelo.ConvocatoriaExamen;
import modelo.Enunciado;
import modelo.Nivel;
import modelo.UnidadDidactica;

/**
 *
 * @author 2dam
 */
public class DaoImplementacion implements Dao {

    /* Tiene que haber 3 o 4 excepciones */
    private ResourceBundle configFile;
    private String urlBD;
    private String userBD;
    private String passwordBD;

    private Connection con;
    private PreparedStatement stmt;
    private CallableStatement cs;

    final String ALTA_UD = "INSERT INTO unidad_didacta (ID_UNIDAD_DIDACTA, ACRONIMO, TITULO, EVALUACION, DESCRIPCION) VALUES (?, ?, ?, ?, ?)";
    final String CONSULTAR_UD = "SELECT * FROM unidad_didacta";
    final String CONSULTAR_ENUNCIADO = "SELECT * FROM enunciado";
    final String CONSULTAR_ENUNCIADO_ID = "SELECT * FROM enunciado WHERE ID_ENUNCIADO = ?";
    final String ASIGNAR_ENUNCOADO_CONVO = "UPDATE convocatoria_examen SET ID_ENUNCIADO = ? WHERE CONVOCATORIA = ?";
    
    final String INSERTAR_CONVOCATORIA = "INSERT INTO CONVOCATORIA_EXAMEN (CONVOCATORIA, DESCRIPCION, FECHA, CURSO, ID_ENUNCIADO) VALUES (?, ?, ?, ?, ?)";

    final String CONSULTAR_CONVOCATORIA = "SELECT * FROM CONVOCATORIA_EXAMEN WHERE CONVOCATORIA = ?";


    private static final String SQL_EXISTE_ENUNCIADO =
        "SELECT 1 FROM ENUNCIADO WHERE ID_ENUNCIADO = ?";

    /** Comprueba existencia de unidad didáctica por ID. */
    private static final String SQL_EXISTE_UNIDAD =
        "SELECT 1 FROM UNIDAD_DIDACTA WHERE ID_UNIDAD_DIDACTA = ?";

    /** Comprueba existencia de convocatoria por nombre. */
    private static final String SQL_EXISTE_CONVOCATORIA =
        "SELECT 1 FROM CONVOCATORIA_EXAMEN WHERE CONVOCATORIA = ?";

    /** Inserta un enunciado. */
    private static final String SQL_INSERT_ENUNCIADO =
        "INSERT INTO ENUNCIADO (ID_ENUNCIADO, DESCRIPCION, NIVEL, DISPONIBLE, RUTA) VALUES (?, ?, ?, ?, ?)";

    /** Inserta relación enunciado–unidad. */
    private static final String SQL_INSERT_CONTIENE =
        "INSERT INTO CONTIENE (ID_ENUNCIADO, ID_UNIDAD_DIDACTA) VALUES (?, ?)";

    /** Asocia un enunciado a una convocatoria existente (por nombre). */
    private static final String SQL_UPDATE_CONVOCATORIA_SET_ENUNCIADO =
        "UPDATE CONVOCATORIA_EXAMEN SET ID_ENUNCIADO = ? WHERE CONVOCATORIA = ?";

    /** Consulta enunciados por unidad didáctica. */
    private static final String SQL_CONSULTAR_ENUNCIADOS_POR_UNIDAD =
        "SELECT e.ID_ENUNCIADO, e.DESCRIPCION, e.NIVEL, e.DISPONIBLE, e.RUTA " +
        "FROM ENUNCIADO e " +
        "JOIN CONTIENE c ON e.ID_ENUNCIADO = c.ID_ENUNCIADO " +
        "WHERE c.ID_UNIDAD_DIDACTA = ?";

    /** Consulta convocatorias por enunciado. */
    private static final String SQL_CONSULTAR_CONVOCATORIAS_POR_ENUNCIADO =
        "SELECT CONVOCATORIA, DESCRIPCION, FECHA, CURSO " +
        "FROM CONVOCATORIA_EXAMEN " +
        "WHERE ID_ENUNCIADO = ?";
    
     private String dificultadToDbValue(Nivel n) {
        String name = n.name().toLowerCase(); // "alta"
        return Character.toUpperCase(name.charAt(0)) + name.substring(1); // "Alta"
    }
    
    public DaoImplementacion() {
        this.configFile = ResourceBundle.getBundle("modelo.configClass");
        this.urlBD = this.configFile.getString("Conn");
        this.userBD = this.configFile.getString("DBUser");
        this.passwordBD = this.configFile.getString("DBPass");
    }

    private void openConnection() {

        try {
            con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
//			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/futbol_americano?serverTimezone=Europe/Madrid&useSSL=false", "root",
//				"abcd*1234");
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD");
        }
    }

    private void closeConnection() throws SQLException {

        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
        if (cs != null) {
            cs.close();
        }
        if (con != null) {
            con.close();
        }

    }

    @Override
    public void crearConvocatoria(ConvocatoriaExamen con) throws ConvocatoriaExcepcion {
        try {
            openConnection();

            stmt = this.con.prepareStatement(INSERTAR_CONVOCATORIA);

            stmt.setString(1, con.getConvocatoria());
            stmt.setString(2, con.getDescripcion());
            stmt.setDate(3, java.sql.Date.valueOf(con.getFecha()));
            stmt.setString(4, con.getCurso());
            stmt.setInt(5, con.getIdEnunciado());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }

    }
    
    @Override
    public void consultarConvocatoria(String nombreConvocatoria) throws ConvocatoriaExcepcion {
        try {
            openConnection();

            stmt = this.con.prepareStatement(CONSULTAR_CONVOCATORIA);
            stmt.setString(1, nombreConvocatoria);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ConvocatoriaExamen c = new ConvocatoriaExamen(
                        rs.getString("CONVOCATORIA"),
                        rs.getString("DESCRIPCION"),
                        rs.getDate("FECHA").toLocalDate(),
                        rs.getString("CURSO"),
                        rs.getInt("ID_ENUNCIADO")
                );
                System.out.println(c);
            } else {
                System.out.println("No se encontró la convocatoria: " + nombreConvocatoria);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void altaUD(UnidadDidactica unid) throws LoginException {
        openConnection();
        try {
            stmt = con.prepareStatement(ALTA_UD);
            stmt.setInt(1, unid.getId());
            stmt.setString(2, unid.getAcronimo());
            stmt.setString(3, unid.getTitulo());
            stmt.setString(4, unid.getEvaluacion());
            stmt.setString(5, unid.getDescripcion());
            if (stmt.executeUpdate() != 1) {
                throw new LoginException("Problemas con el alta de Unidades");
            }
        } catch (SQLException e) {
            throw new LoginException("Problemas en la BDs");
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<UnidadDidactica> obtenerTodasUD() {
        List<UnidadDidactica> lista = new ArrayList<>();
        openConnection();

        try {
            stmt = con.prepareStatement(CONSULTAR_UD);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UnidadDidactica ud = new UnidadDidactica();
                ud.setId(rs.getInt("ID_UNIDAD_DIDACTA"));
                ud.setAcronimo(rs.getString("ACRONIMO"));
                ud.setTitulo(rs.getString("TITULO"));
                ud.setEvaluacion(rs.getString("EVALUACION"));
                ud.setDescripcion(rs.getString("DESCRIPCION"));
                lista.add(ud);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener las Unidades Didácticas: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    @Override
    public List<Enunciado> obtenerTodosEnunciados() {
        List<Enunciado> lista = new ArrayList<>();
        openConnection();

        try {
            stmt = con.prepareStatement(CONSULTAR_ENUNCIADO);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Enunciado en = new Enunciado();
                en.setId(rs.getInt("ID_ENUNCIADO"));
                en.setDescripcion(rs.getString("DESCRIPCION"));
                String nivelBD = rs.getString("NIVEL");
                if (nivelBD != null) {
                    en.setNivel(Nivel.valueOf(nivelBD.toUpperCase()));
                }
                en.setDisponible(rs.getBoolean("DISPONIBLE"));
                en.setRuta(rs.getString("RUTA"));

                lista.add(en);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los Enunciados: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    @Override
    public Enunciado obtenerEnunciadoPorId(int id) {
        Enunciado en = null;
        openConnection();

        try {
            stmt = con.prepareStatement(CONSULTAR_ENUNCIADO_ID);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                en = new Enunciado();
                en.setId(rs.getInt("ID_ENUNCIADO"));
                en.setDescripcion(rs.getString("DESCRIPCION"));
                String nivelBD = rs.getString("NIVEL");
                if (nivelBD != null) {
                    en.setNivel(Nivel.valueOf(nivelBD.toUpperCase()));
                }
                en.setDisponible(rs.getBoolean("DISPONIBLE"));
                en.setRuta(rs.getString("RUTA"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener el Enunciado por ID: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();

            }

            return en;
        }
    }

    @Override
    public void asignarEnunciadoAConvocatoria(int idEnunciado, String nombreConvocatoria
    ) {
        openConnection();
        try {
            stmt = con.prepareStatement(ASIGNAR_ENUNCOADO_CONVO);
            stmt.setInt(1, idEnunciado);
            stmt.setString(2, nombreConvocatoria);
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Enunciado asignado a la convocatoria correctamente.");
            } else {
                System.out.println("No se encontró la convocatoria especificada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al asignar enunciado a convocatoria ");
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();;
            }
        }
    }

    @Override
    public void crearEnunciadoConUnidadesYConvocatoria(
            Enunciado enunciado,
            List<Integer> idsUnidades,
            ConvocatoriaExamen convocatoria) {

        try {
            openConnection();
            con.setAutoCommit(false);  // Iniciar transacción

            // 1) Validar que el enunciado NO exista
            stmt = con.prepareStatement(SQL_EXISTE_ENUNCIADO);
            stmt.setInt(1, enunciado.getId());
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("❌ El enunciado con ID " + enunciado.getId() + " ya existe.");
                    con.rollback();
                    return;
                }
            }
            stmt.close();

            // 2) Validar que TODAS las UDs existan
            for (Integer idUnidad : idsUnidades) {
                stmt = con.prepareStatement(SQL_EXISTE_UNIDAD);
                stmt.setInt(1, idUnidad);
                try (java.sql.ResultSet rsU = stmt.executeQuery()) {
                    if (!rsU.next()) {
                        System.out.println("❌ La UD " + idUnidad + " no existe.");
                        con.rollback();
                        return;
                    }
                }
                stmt.close();
            }

            // 3) Validar que la CONVOCATORIA exista (por NOMBRE)
            stmt = con.prepareStatement(SQL_EXISTE_CONVOCATORIA);
            stmt.setString(1, convocatoria.getConvocatoria());
            try (java.sql.ResultSet rsC = stmt.executeQuery()) {
                if (!rsC.next()) {
                    System.out.println("❌ La convocatoria '" + convocatoria.getConvocatoria() + "' no existe.");
                    con.rollback();
                    return;
                }
            }
            stmt.close();

            // 4) INSERT ENUNCIADO
            stmt = con.prepareStatement(SQL_INSERT_ENUNCIADO);
            stmt.setInt(1, enunciado.getId());
            stmt.setString(2, enunciado.getDescripcion());
            stmt.setString(3, dificultadToDbValue(enunciado.getNivel())); // "Alta|Media|Baja"
            stmt.setBoolean(4, enunciado.isDisponible());
            stmt.setString(5, enunciado.getRuta());
            stmt.executeUpdate();
            stmt.close();

            // 5) INSERT CONTIENE (una fila por cada UD)
            if (idsUnidades != null && !idsUnidades.isEmpty()) {
                stmt = con.prepareStatement(SQL_INSERT_CONTIENE);
                for (Integer idUnidad : idsUnidades) {
                    stmt.setInt(1, enunciado.getId());
                    stmt.setInt(2, idUnidad);
                    stmt.executeUpdate();
                }
                stmt.close();
            } else {
                System.out.println("Aviso: no se han asociado unidades didácticas a este enunciado.");
            }

            // 6) UPDATE CONVOCATORIA: asociar el enunciado al nombre dado
            stmt = con.prepareStatement(SQL_UPDATE_CONVOCATORIA_SET_ENUNCIADO);
            stmt.setInt(1, enunciado.getId());
            stmt.setString(2, convocatoria.getConvocatoria());
            int filas = stmt.executeUpdate();
            stmt.close();

            if (filas == 0) {
                System.out.println("❌ No se pudo asociar la convocatoria (0 filas actualizadas).");
                con.rollback();
                return;
            }

            con.commit();
            System.out.println("✅ Enunciado, UDs y asociación a convocatoria realizados.");

        } catch (Exception e) {
            System.out.println("❌ Error al crear enunciado y asociar: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (Exception ex) {
                System.out.println("❌ Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
                closeConnection();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }  
    
     // ========================== VALIDACIONES ==========================

    /** {@inheritDoc} */
    @Override
    public boolean existeUnidadDidactica(int idUnidad) {
        try {
            openConnection();
            try (PreparedStatement ps = con.prepareStatement(SQL_EXISTE_UNIDAD)) {
                ps.setInt(1, idUnidad);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error existeUnidadDidactica: " + e.getMessage());
            return false;
        } finally {
            try { closeConnection(); } catch (Exception ignore) {}
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean existeConvocatoriaPorNombre(String nombreConvocatoria) {
        try {
            openConnection();
            try (PreparedStatement ps = con.prepareStatement(SQL_EXISTE_CONVOCATORIA)) {
                ps.setString(1, nombreConvocatoria);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error existeConvocatoriaPorNombre: " + e.getMessage());
            return false;
        } finally {
            try { closeConnection(); } catch (Exception ignore) {}
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean existeEnunciado(int idEnunciado) {
        try {
            openConnection();
            try (PreparedStatement ps = con.prepareStatement(SQL_EXISTE_ENUNCIADO)) {
                ps.setInt(1, idEnunciado);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error existeEnunciado: " + e.getMessage());
            return false;
        } finally {
            try { closeConnection(); } catch (Exception ignore) {}
        }
    }

    // ========================== CONSULTAS ==========================

    /** {@inheritDoc} */
    @Override
    public java.util.List<modelo.Enunciado> consultarEnunciadosPorUnidad(int idUnidad) {
        java.util.List<modelo.Enunciado> lista = new java.util.ArrayList<>();
        try {
            openConnection();
            stmt = con.prepareStatement(SQL_CONSULTAR_ENUNCIADOS_POR_UNIDAD);
            stmt.setInt(1, idUnidad);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    modelo.Enunciado e = new modelo.Enunciado();
                    e.setId(rs.getInt("ID_ENUNCIADO"));
                    e.setDescripcion(rs.getString("DESCRIPCION"));

                    String nivelStr = rs.getString("NIVEL");
                    if (nivelStr != null) {
                        switch (nivelStr.toLowerCase()) {
                            case "alta":  e.setNivel(modelo.Nivel.ALTA);  break;
                            case "media": e.setNivel(modelo.Nivel.MEDIA); break;
                            case "baja":  e.setNivel(modelo.Nivel.BAJA);  break;
                        }
                    }

                    e.setDisponible(rs.getBoolean("DISPONIBLE"));
                    e.setRuta(rs.getString("RUTA"));
                    lista.add(e);
                }
            }
        } catch (Exception ex) {
            System.out.println("❌ Error al consultar enunciados por unidad: " + ex.getMessage());
        } finally {
            try { closeConnection(); } catch (Exception ignore) {}
        }
        return lista;
    }

    /** {@inheritDoc} */
    @Override
    public java.util.List<modelo.ConvocatoriaExamen> consultarConvocatoriasPorEnunciado(int idEnunciado) {
        java.util.List<modelo.ConvocatoriaExamen> lista = new java.util.ArrayList<>();
        try {
            openConnection();
            stmt = con.prepareStatement(SQL_CONSULTAR_CONVOCATORIAS_POR_ENUNCIADO);
            stmt.setInt(1, idEnunciado);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    modelo.ConvocatoriaExamen c = new modelo.ConvocatoriaExamen();
                    c.setConvocatoria(rs.getString("CONVOCATORIA"));
                    c.setDescripcion(rs.getString("DESCRIPCION"));
                    // Convertir SQL Date → LocalDate
                    java.sql.Date fechaSql = rs.getDate("FECHA");
                    if (fechaSql != null) {
                        c.setFecha(fechaSql.toLocalDate());
                    }
                    c.setCurso(rs.getString("CURSO"));
                    lista.add(c);
                }
            }
        } catch (Exception ex) {
            System.out.println("❌ Error al consultar convocatorias por enunciado: " + ex.getMessage());
        } finally {
            try { closeConnection(); } catch (Exception ignore) {}
        }
        return lista;
    }
}
