/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.security.auth.login.LoginException;
import modelo.Enunciado;
import modelo.Nivel;
import modelo.UnidadDidactica;

/**
 *
 * @author 2dam
 */
public class DaoImplementacion implements Dao{
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
        final String CONSULTAR_ENUNCIADO ="SELECT * FROM enunciado";
        
        
        
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
    
}
