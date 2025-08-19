package ProjetoTioPatinhas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // Ajuste conforme sua conexão do SQL Developer:
    // - Se usar Service Name (com PDB, ex.: XEPDB1):
    //   jdbc:oracle:thin:@//localhost:1521/XEPDB1
    // - Se usar SID (ex.: XE antigo):
    //   jdbc:oracle:thin:@localhost:1521:XE
    private static final String URL  = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String USER = "pbl_cripto";
    private static final String PASS = "senhacripto";

    public static Connection getConnection() throws SQLException {
        // Opcional com drivers modernos: DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        return DriverManager.getConnection(URL, USER, PASS);
    }
}