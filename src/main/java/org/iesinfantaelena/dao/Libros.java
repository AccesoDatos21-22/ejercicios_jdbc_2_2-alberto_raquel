package org.iesinfantaelena.dao;

import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.utils.Utilidades;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Libros {

    // Consultas a realizar en BD


    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pstmt;
    private ArrayList<Libro> listalibros= new ArrayList<Libro>();


    // Consultas a realizar en BD
    private static final String SELECT_LIBROS_QUERY = "select * from libros";
    private static final String SEARCH_LIBROS_QUERY = "select * from libros WHERE isbn= ?";
    private static final String INSERT_LIBROS_QUERY = "insert into libros values (?,?,?,?,?,?)";
    private static final String DELETE_LIBROS_QUERY = "delete from libros WHERE isbn = ?";
    private static final String CREATE_TABLE_LIBROS ="create table if not exists libros (   isbn integer not null,titulo varchar(50) not null,autor varchar(50) not null,editorial varchar(25) not null,paginas integer not null,copias integer not null,constraint isbn_pk primary key (isbn));";
    private static final String SELECT_CAMPOS_QUERY = "SELECT * FROM LIBROS LIMIT 1";
    private static final String CREATE_LIBROS=" create table libros (isbn integer not null, titulo varchar(50) not null, autor varchar(50) not null, " +
            "editorial varchar(25) not null, paginas integer not null, copias integer not null, constraint isbn_pk primary key (isbn));";

    private static final String INSERT_LIBRO_QUERY="INSERT INTO libros VALUES (?,?,?,?,?,?)";
    private static final String SEARCH_LIBROS_EDITORIAL = "SELECT * FROM libros WHERE libros.editorial= ?";
    private static final String VER_CATALOGO = "SELECT * FROM libros";
    private static final String ACTUALIZAR_COPIAS = "UPDATE libros SET copias=100 WHERE libros.isbn = ?";
    private static final String BORRAR_LIBRO = "DELETE from libros WHERE libros.titulo = ?";

    /**
     * Constructor: inicializa conexión
     */

    public Libros() throws AccesoDatosException {
        try {
            // Obtenemos la conexión
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;

            stmt = con.createStatement();
            stmt.executeUpdate(CREATE_LIBROS);

        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        }
    }


    /**
     * Método para cerrar la conexión
     *
     * @throws AccesoDatosException
     */
    public void cerrar() {

        if (con != null) {
            Utilidades.closeConnection(con);
        }

    }


    /**
     * Método para liberar recursos
     *
     * @throws AccesoDatosException
     */
    private void liberar() {
        try {
            // Liberamos todos los recursos pase lo que pase
            //Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el primer if. Lo dejamos por claridad.
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        } catch (SQLException sqle) {
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

    public List<Libro> verCatalogo() throws AccesoDatosException {

        /* Sentencia sql */
        stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        rs = null;

        try {
            // Creación de la sentencia
            stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(VER_CATALOGO);

            // Recuperación de los datos del ResultSet
            while (rs.next()) {
                int isbn= rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");

                listalibros.add(new Libro(isbn,titulo,autor,editorial,paginas,copias));
            }
            return listalibros;

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
                liberar();

        }

    }

    /**
     * Actualiza el numero de copias para un libro
     * @param
     * @param
     * @throws AccesoDatosException
     */

    public void actualizarCopias(Libro libro) throws AccesoDatosException {
        pstmt=null;
        try {
            pstmt=con.prepareStatement(ACTUALIZAR_COPIAS);
            pstmt.setInt(1,libro.getISBN());
            pstmt.executeUpdate();
            System.out.println("Las copias del libro "+ libro.getTitulo()+ " han sido actualizadas.");

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }


    /**
     * Añade un nuevo libro a la BD
     * @throws AccesoDatosException
     */
    public void anadirLibro(Libro libro) throws AccesoDatosException {

        pstmt=null;
        try {
            pstmt=con.prepareStatement(CREATE_LIBROS);
            pstmt= con.prepareStatement(INSERT_LIBRO_QUERY);

            pstmt.setInt(1,libro.getISBN());
            pstmt.setString(2,libro.getTitulo());
            pstmt.setString(3,libro.getAutor());
            pstmt.setString(4,libro.getEditorial());
            pstmt.setInt(5,libro.getPaginas());
            pstmt.setInt(6,libro.getCopias());
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }

    /**
     * Borra un libro por ISBN
     * @throws AccesoDatosException
     */

    public void borrar(Libro libro) throws AccesoDatosException {
        // Sentencia sql
        pstmt = null;

        try {
            // Creación de la sentencia
            pstmt = con.prepareStatement(BORRAR_LIBRO);
            pstmt.setString(1, libro.getTitulo());
            // Ejecución del borrado
            pstmt.executeUpdate();
            System.out.println("libro "+ libro.getTitulo()+ " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }

        }

    }

    /**
     * Devulve los nombres de los campos de BD
     * @return
     * @throws AccesoDatosException
     */

    /**
     * Devulve los nombres de los campos de BD
     *
     * @return
     * @throws AccesoDatosException
     */

    public String[] getCamposLibro() throws AccesoDatosException {
        /*Sentencia sql con parámetros de entrada*/
        pstmt = null;

        /*Conjunto de Resultados a obtener de la sentencia sql*/
        rs= null;
        ResultSetMetaData rsmd;
        String[] campos;
        try {
            //Solicitamos a la conexion un objeto stmt para nuestra consulta
            pstmt = con.prepareStatement(SELECT_CAMPOS_QUERY);

            //Le solicitamos al objeto stmt que ejecute nuestra consulta
            //y nos devuelve los resultados en un objeto ResultSet
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            campos = new String[columns];
            for (int i = 0; i < columns; i++) {
                //Los indices de las columnas comienzan en 1
                campos[i] = rsmd.getColumnLabel(i + 1);
            }

            return campos;


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally{
            liberar();
        }
    }


    public void obtenerLibro(int ISBN) throws AccesoDatosException {
// Sentencia sql
        pstmt = null;
        // Conjunto de Resultados a obtener de la sentencia sql
        rs = null;
        try {
            // Creación de la sentencia
            pstmt = con.prepareStatement(SEARCH_LIBROS_EDITORIAL);
            pstmt.setInt(1, ISBN);
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = pstmt.executeQuery();

            // Recuperación de los datos del ResultSet
            if (rs.next()) {
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
            }

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }

    public boolean crearTablaLibros() throws SQLException {

        if (stmt == null)
            stmt = con.createStatement();
        stmt.execute(CREATE_LIBROS);
        liberar();
        return true;
    }

    public void librosporEditorial(String editorial) throws AccesoDatosException{
        //Sentencia SQL
        pstmt=null;
        //Resultados a obtener de la sentencia SQL
        rs=null;
        try {
            con=new Utilidades().getConnection();
            //Creacion de la sentencia
            pstmt=con.prepareStatement(SEARCH_LIBROS_EDITORIAL);
            pstmt.setString(1,editorial);
            //Ejecución de la consulta y obtencion de resultados en un ResultSet
            rs=pstmt.executeQuery();
            while (rs.next()){
                int isbn=rs.getInt("isbn");
                String titulo=rs.getString("titulo");
                String autor=rs.getString("autor");
                String editor=rs.getString("editorial");
                int paginas=rs.getInt("paginas");
                int copias=rs.getInt("copias");
                System.out.println(isbn+", "+titulo+", "+autor+", "+editor+", "+paginas+", "+copias);
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
                // Liberamos todos los recursos pase lo que pase
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

    /**
     * Debes utilizar la misma consulta que ya tienes para mostrar el catálogo en orden alfabético.
     * Resuélvelo utilizando un tipo adecuado de Resultset y los métodos vistos en clase de esta interfaz.
     * (Con insensitive y concur updatable)
     * @throws AccesoDatosException
     * @return
     */
    public void verCatalogoInverso() throws AccesoDatosException{

        try {

            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // Creación de la sentencia
           // stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(VER_CATALOGO);
            rs.afterLast();

            // Recuperación de los datos del ResultSet
            while (rs.previous()) {
                int isbn= rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                Libro l1 = new Libro(isbn, titulo, autor, editorial, paginas, copias);
                System.out.println(l1);
            }

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

}
