/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

	final String ALTA_TREN = "INSERT INTO TREN (ID, nombre, plazas, plazas_ocupadas, precio, origen, destino) VALUES (?, ?, ?, ?, ?, ?, ?)";

	public DaoImplementacion() {
		this.configFile = ResourceBundle.getBundle("modelo.configClase");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	private void openConnection() throws ClassNotFoundException {
		try {

			/*
			 * con = DriverManager.getConnection(
			 * "jdbc:mysql://localhost:3306/UNALBA_SL?serverTimezone=Europe/Madrid&useSSL=false",
			 * "root", "abcd*1234");
			 */
			con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
		} catch (SQLException e) {
			System.out.println("Error al intentar abrir la BD");
		}
	}

	private void closeConnection() throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (con != null)
			con.close();
	}
}
