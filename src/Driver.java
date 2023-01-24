
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sqlite.SQLiteDataSource;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author ahmed
 */
public final class Driver implements AutoCloseable {

    private Connection connection;
    private static String url;

    // Queries
    public static final String SQL_ALL_ROWS = "SELECT * FROM %s;";
    private static final String SQL_FIND_ALL_TABLES
            = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";

    public Driver(String fileName) throws SQLException {
        setUrl();
        connect(fileName);
    }

    // Connect to a selected database
    public void connect(String fileName) throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(String.format(url, fileName));
        connection = dataSource.getConnection();
    }

    // Retrieve all tables of a specific database
    public List<String> getAllTables() throws SQLException {
        var tableNames = new ArrayList<String>();
        try (var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_TABLES);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                tableNames.add(name);
            }
            return tableNames;
        }
    }

    // Execute specified query
    public DataTableModel runQuery(String query, String table) throws SQLException {
        try ( Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            
            // Retrieve columns
            var columnCount = metaData.getColumnCount();
            var columns = new String[columnCount];
            
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columns[i] = metaData.getColumnName(i + 1);
            }
            
            // Retrieve row
            var data = new HashMap<Integer, Object[]>();
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = resultSet.getObject(j + 1);
                }
                data.put(i++, row);
            }
            return new DataTableModel(columns, data);
        }
    }

    private void setUrl() {
        // Currently, only sqlite is supported
        url = "jdbc:sqlite:%s";
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
