import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestDBConnection {

    private Connection connection1, connection2;


    @Before
    public void setUp() {
        connection1 = DBConnection.getConnection();
        connection2 = DBConnection.getConnection();
    }

    @After
    public void apres() {
        connection1 = null;
        connection2 = null;
    }

    @Test
    public void testSingleton() {
        assertEquals("Le singleton ne fonctionne pas correctement.", connection1, connection2);
    }

    @Test
    public void testChangeDB() throws SQLException {
        DBConnection.setNomDB("deefy");
        Connection newConnection = DBConnection.getConnection();
        assertNotSame("Le changement de base de données n'a pas été effectué correctement.", connection1, newConnection);
    }

    @Test
    public void testConnectionType() {
        assertTrue("La connection n'est pas du bon type", connection1 instanceof java.sql.Connection );
    }
}
