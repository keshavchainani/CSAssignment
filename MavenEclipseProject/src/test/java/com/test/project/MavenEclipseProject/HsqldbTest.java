package com.test.project.MavenEclipseProject;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HsqldbTest {

    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        initDatabase();
    }

    @AfterClass
    public static void destroy() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE logs");
            connection.commit();
            statement.executeUpdate("SHUTDOWN");
            connection.commit();
        }
    }

    /**
     * Database initialization for testing i.e.
     * <ul>
     * <li>Creating Table</li>
     * <li>Inserting record</li>
     * </ul>
     *
     * @throws SQLException
     */
    private static void initDatabase() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("CREATE TABLE logs(id VARCHAR(20) NOT NULL,state VARCHAR(10) NOT NULL,type VARCHAR(20), host VARCHAR(10), timestamp VARCHAR(50) NOT NULL, alarm BIT DEFAULT 0)");
            connection.commit();
            statement.executeUpdate("INSERT INTO logs VALUES ('scsmbstgra','STARTED', 'APPLICATION_LOG','12345','1491377495242',0)");
            statement.executeUpdate("INSERT INTO logs VALUES ('scsmbstgrb','STARTED','','', '1491377495213','')");
            statement.executeUpdate("INSERT INTO logs VALUES ('scsmbstgrc','FINISHED','','', '1491377495218','')");
            statement.executeUpdate("UPDATE logs SET alarm = 1 WHERE timestamp = '1491377495218'");
            connection.commit();
        }
    }

    /**
     * Create a connection
     *
     * @return connection object
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:src/res/logs", "sa", "");
    }

    /**
     * Get total records in table
     *
     * @return total number of records. In case of exception 0 is returned
     */
    private int getTotalRecords() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            ResultSet result = statement.executeQuery("SELECT count(*) as total FROM logs");
            if (result.next()) {
                return result.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Test
    public void getTotalRecordsTest() {
        assertEquals("the total records is 3",3, getTotalRecords());
    }

    @Test
    public void checkIdExistsTest() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);) {

            ResultSet result = statement.executeQuery("SELECT id FROM logs");

            if (result.first()) {
                assertEquals("first raw id is scsmbstgra","scsmbstgra", (result.getString("id")));
            }

            if (result.last()) {
                assertEquals("last id is scsmbstgrc","scsmbstgrc", result.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkUpdatedColumnTest(){
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);) {
            ResultSet result = statement.executeQuery("SELECT alarm FROM logs where timestamp = '1491377495218'");
            if (result.first()){
                assertEquals("The alarm column updated!", 1 , result.getInt("alarm"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}

