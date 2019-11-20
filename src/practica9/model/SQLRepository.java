package practica9.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SQLRepository {
    
    private Connection connection;
    private int connected = 0;
        
    public int logIn(String user, String password){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://mozart.dis.ulpgc.es/DIU_BD",
                    user, password);
            connected = 1;
            return connected;
        }catch(SQLException ex){
            return ex.getErrorCode();
        } catch (ClassNotFoundException ex) {
            return 0;
        }
    }
    
    public List<String> getTableList() throws SQLException {
        List<String> list = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        String[] types = {"TABLE"};
        ResultSet result = metaData.getTables(null, null, "%", types);
        while (result.next()) {
            list.add(result.getString("TABLE_NAME"));
        }
        return list;
    }

    public Map<String, List<String>> getTablesFields() throws SQLException {
        Map<String,List<String>> map= new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        String[] types = {"TABLE"};
        ResultSet rows = metaData.getTables(null, null, "%", types);
        while (rows.next()) {
            String table = rows.getString("TABLE_NAME");
            List <String> fields = new ArrayList<>();
            ResultSet columns = metaData.getColumns(null, null, table, null);
            while (columns.next()) {
                fields.add(table + "." + columns.getString("COLUMN_NAME"));
            }
            map.put(table,fields);
        }
        return map;
    } 
    
    public void connectionClose() throws SQLException{
        connection.close();
    }
}
