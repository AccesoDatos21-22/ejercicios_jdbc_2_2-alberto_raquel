package org.iesinfantaelena.dao;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.utils.Utilidades;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Cafes {

    //Define como atributos de clase: un objeto Connection, ResulSet, PreparedStatement y Statement
    private static Connection connection;
    private static ResultSet rs;
    private static PreparedStatement pstmt;
    private static Statement stmt;


    // Consultas a realizar en BD
    private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
    private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
    private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
    private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
    private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";

    private static final String CREATE_TABLE_PROVEEDORES ="create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";

    private static final String CREATE_TABLE_CAFES ="create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

    /**
     * Constructor: inicializa conexión
     *
     * @throws AccesoDatosException
     */

    /**
    Desarrolla un constructor de clase en el que se inicialice la conexión y el resto de atributos se inicialice a null
     */
    public Cafes() {

        stmt = null;

        try {
            connection = new Utilidades().getConnection();
            stmt = connection.createStatement();

            stmt.executeUpdate(CREATE_TABLE_PROVEEDORES);

            stmt.executeUpdate(CREATE_TABLE_CAFES);

            stmt.executeUpdate("insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');");
            stmt.executeUpdate("insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');");
            stmt.executeUpdate("insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');");

        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);


        } finally {
            try {
             liberarRecursos();
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }

    /**
     * Desarrolla un método cerrar en el que se cierre la conexión
     */

    public void cerrarConection() throws SQLException {

        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Desarrolla un método liberar en el que se cierren el resto de recursos
     */

    public void liberarRecursos() throws SQLException {

        try{
            if (stmt!= null){
                stmt.close();
            }
            if (rs!= null){
                rs.close();
            }
            if (pstmt!=null){
                pstmt.close();
            }
        }catch (SQLException sqle) {
            // En una aplicación real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }

    }
    /**
     * Metodo que muestra por pantalla los datos de la tabla cafes
     *
     * @param
     * @throws SQLException
     */
    public void verTabla() throws AccesoDatosException {

        /* Sentencia sql */
        stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        rs = null;
        try {
            // Creación de la sentencia
            stmt = connection.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(SELECT_CAFES_QUERY);

            // Recuperación de los datos del ResultSet
            while (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                liberarRecursos();
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

    /**
     * Mótodo que busca un cafe por nombre y muestra sus datos
     *
     * @param nombre
     */
    public void buscar(String nombre) throws AccesoDatosException {

        /* Sentencia sql */
        pstmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            pstmt = connection.prepareStatement(SEARCH_CAFE_QUERY);
            pstmt.setString(1, nombre);
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = pstmt.executeQuery();

            // Recuperación de los datos del ResultSet
            if (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                liberarRecursos();
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

    /**
     * Mótodo para insertar una fila
     *
     * @param nombre
     * @param provid
     * @param precio
     * @param ventas
     * @param total
     * @return
     */
    public void insertar(String nombre, int provid, float precio, int ventas,
                         int total) throws AccesoDatosException {

        /* Sentencia sql */
        pstmt = null;

        try {

            pstmt = connection.prepareStatement(INSERT_CAFE_QUERY);
            pstmt.setString(1, nombre);
            pstmt.setInt(2, provid);
            pstmt.setFloat(3, precio);
            pstmt.setInt(4, ventas);
            pstmt.setInt(5, total);
            // Ejecución de la inserción
            pstmt.executeUpdate();


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase

                liberarRecursos();

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

    /**
     * Mótodo para borrar una fila dado un nombre de cafó
     *
     * @param
     * @return
     */
    public void borrar(String nombre) throws AccesoDatosException {

        /* Sentencia sql */
        pstmt = null;

        try {
            // Creación de la sentencia
            pstmt = connection.prepareStatement(DELETE_CAFE_QUERY);
            pstmt.setString(1, nombre);
            // Ejecución del borrado
            pstmt.executeUpdate();
            System.out.println("café "+nombre+ " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            try {

                liberarRecursos();

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }

        }

    }

    /**
     * Mótodo que busca un cafe por nombre y muestra sus datos
     *
     * @param
     */
    public void cafesPorProveedor(int provid) throws AccesoDatosException {

        //Sentencia sql
        pstmt = null;
        //Conjunto de Resultados a obtener de la sentencia sql
        ResultSet rs = null;
        try {
            connection = new Utilidades().getConnection();
            // Creación de la sentencia
            pstmt = connection.prepareStatement(SEARCH_CAFES_PROVEEDOR);
            pstmt.setInt(1, provid);
            // Ejecución de la consulta y obtención de resultados en un ResultSet
            rs = pstmt.executeQuery();
            // Recuperación de los datos del ResultSet

            while (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                String provName = rs.getString("PROV_NOMBRE");
                String calle = rs.getString("CALLE");
                String ciudad = rs.getString("CIUDAD");
                String pais = rs.getString("PAIS");
                int cp = rs.getInt("CP");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total
                        + ",Y el proveedor es:" + provName + "," + calle + ","
                        + ciudad + "," + pais + "," + cp);
            }

        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {

                liberarRecursos();

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

}
