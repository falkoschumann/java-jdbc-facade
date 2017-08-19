package de.muspellheim.jdbc.stream;

import org.junit.*;

import java.sql.*;

public class DDLTest {

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        //Class.forName("org.hsqldb.jdbcDriver");
        //connection = DriverManager.getConnection("jdbc:hsqldb:mem:oshop");

        //Class.forName("org.postgresql.Driver");
        //connection = DriverManager.getConnection("jdbc:postgresql:oshop");

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:8889", "root", "root");
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testCreateSchema() throws Exception {
        Statement statement = connection.createStatement();
        statement.execute("DROP SCHEMA IF EXISTS oshop");
        statement.execute("CREATE SCHEMA oshop CHARACTER SET utf8 COLLATE utf8_unicode_ci");
        //statement.execute("CREATE SCHEMA oshop");
    }

}
