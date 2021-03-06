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
    private static final String INSERT_LIBROS_QUERY = "insert into libros values (?,?,?,?,?,?,?)";
    private static final String DELETE_LIBROS_QUERY = "delete from libros WHERE isbn = ?";
    private static final String CREATE_TABLE_LIBROS ="create table if not exists libros (   isbn integer not null,titulo varchar(50) not null,autor varchar(50) not null,editorial varchar(25) not null,paginas integer not null,copias integer not null,constraint isbn_pk primary key (isbn));";
    private static final String SELECT_CAMPOS_QUERY = "SELECT * FROM LIBROS LIMIT 1";
    private static final String CREATE_LIBROS=" create table libros (isbn integer not null, titulo varchar(50) not null, autor varchar(50) not null, " +
            "editorial varchar(25) not null, paginas integer not null, copias integer not null, precio double, constraint isbn_pk primary key (isbn));";
    private static final String INSERT_LIBRO_QUERY="INSERT INTO libros VALUES (?,?,?,?,?,?,?)";
    private static final String SEARCH_LIBROS_EDITORIAL = "SELECT * FROM libros WHERE libros.editorial= ?";
    private static final String VER_CATALOGO = "SELECT * FROM libros";
    private static final String ACTUALIZAR_COPIAS = "UPDATE libros SET copias=100 WHERE libros.isbn = ?";
    private static final String BORRAR_LIBRO = "DELETE from libros WHERE libros.titulo = ?";
    private static final String INSERT_COLUMNA_PRECIO = "ALTER TABLE libros ADD precio double";


    /**
     * Constructor: inicializa conexi??n
     */

    public Libros() throws AccesoDatosException {
        try {
            // Obtenemos la conexi??n
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;

            stmt = con.createStatement();
            stmt.executeUpdate(CREATE_LIBROS);

        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicaci??n real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        }
    }


    /**
     * M??todo para cerrar la conexi??n
     *
     * @throws AccesoDatosException
     */
    public void cerrar() {

        if (con != null) {
            Utilidades.closeConnection(con);
        }

    }


    /**
     * M??todo para liberar recursos
     *
     * @throws AccesoDatosException
     */
    private void liberar() {
        try {
            // Liberamos todos los recursos pase lo que pase
            //Al cerrar un stmt se cierran los resultset asociados. Pod??amos omitir el primer if. Lo dejamos por claridad.
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
            // En una aplicaci??n real, escribo en el log, no delego porque
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
            // Creaci??n de la sentencia
            stmt = con.createStatement();
            // Ejecuci??n de la consulta y obtenci??n de resultados en un
            // ResultSet
            rs = stmt.executeQuery(VER_CATALOGO);

            // Recuperaci??n de los datos del ResultSet
            while (rs.next()) {
                int isbn= rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                double precio = rs.getDouble("precio");

                listalibros.add(new Libro(isbn,titulo,autor,editorial,paginas,copias, precio));
            }
            return listalibros;

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
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
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }


    /**
     * A??ade un nuevo libro a la BD
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
            pstmt.setDouble(7, libro.getPrecio());
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
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
            // Creaci??n de la sentencia
            pstmt = con.prepareStatement(BORRAR_LIBRO);
            pstmt.setString(1, libro.getTitulo());
            // Ejecuci??n del borrado
            pstmt.executeUpdate();
            System.out.println("libro "+ libro.getTitulo()+ " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
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
        /*Sentencia sql con par??metros de entrada*/
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
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");

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
            // Creaci??n de la sentencia
            pstmt = con.prepareStatement(SEARCH_LIBROS_EDITORIAL);
            pstmt.setInt(1, ISBN);
            // Ejecuci??n de la consulta y obtenci??n de resultados en un
            // ResultSet
            rs = pstmt.executeQuery();

            // Recuperaci??n de los datos del ResultSet
            if (rs.next()) {
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
            }

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
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
            //Ejecuci??n de la consulta y obtencion de resultados en un ResultSet
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
            // En una aplicaci??n real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
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
                // En una aplicaci??n real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

    /**
     * Debes utilizar la misma consulta que ya tienes para mostrar el cat??logo en orden alfab??tico.
     * Resu??lvelo utilizando un tipo adecuado de Resultset y los m??todos vistos en clase de esta interfaz.
     * (Con insensitive y concur updatable)
     * @throws AccesoDatosException
     * @return
     */
    public void verCatalogoInverso() throws AccesoDatosException{

        try {

            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // Creaci??n de la sentencia
           // stmt = con.createStatement();
            // Ejecuci??n de la consulta y obtenci??n de resultados en un
            // ResultSet
            rs = stmt.executeQuery(VER_CATALOGO);
            rs.afterLast();

            // Recuperaci??n de los datos del ResultSet
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
            // En una aplicaci??n real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

    /**
     * A??ade un nuevo m??todo actualizarCopias de la clase Libros. Esta nueva versi??n recibe un Hashmap que contiene
     * el nuevo n??mero de copias para cada isbn.
     *
     *  public void actualizarCopias(HashMap<Integer, Integer> copias);
     *
     * Este nuevo n??mero de copias se lo tienes que sumar al actual. Resu??lvelo con la consulta SELECT_LIBROS_QUERY utilizando un tipo de ResultSet adecuado.
     * Prueba que funciona el nuevo m??todo en la clase con la funci??n main.
     */

    public void actualizarCopias(HashMap<Integer, Integer> copias) throws AccesoDatosException {
        stmt = null;
        pstmt=null;

        Libro lb = new Libro();
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
              rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

             while (rs.next()){
                  if(copias.containsKey(rs.getInt("isbn"))){
                      int total = rs.getInt("copias") + copias.get(rs.getInt("isbn"));
                     rs.updateInt("copias", total);
                     rs.updateRow();
                     lb.setCopias(total);
                  }
              }
            System.out.println("Las copias han sido actualizadas. Total actual: " + lb.getCopias());
        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }

    /**
     * A??ade un m??todo a la clase Libros que reciba un listado con n??meros de filas a mostrar utilizando cursores.
     *
     *  public void verCatalogo(int[] filas) throws AccesoDatosException;
     *
     * Los n??meros no tienen por qu?? ser consecutivos ni estar ordenados. Resu??lvelo con la consulta SELECT_LIBROS_QUERY utilizando un ResultSet adecuado y sus m??todos.
     * Prueba que funciona el nuevo m??todo en la clase con la funci??n main.
     */

    public void verCatalogo(int[] filas) throws AccesoDatosException{

        stmt = null;
        pstmt = null;

        try{

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            System.out.println("Listado de libros: ");

            for(int i = 0; i < filas.length; i++){
                if(rs.absolute(filas[i])){
                    int isbn = rs.getInt("isbn");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String editorial = rs.getString("editorial");
                    int paginas = rs.getInt("paginas");
                    int copias = rs.getInt("copias");

                    System.out.println("ISBN: " + isbn + " T??tulo: " + titulo + " Autor: " + autor + " Editorial: " + editorial + " P??ginas: " + paginas + " Copias: " + copias);
                }
            }

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
}

/**
 * A??ade un nuevo campo precio a la tabla Libros. A??ade un nuevo m??todo a la clase Libros
 * llamado rellenaPrecio(float precio) throws AccesoDatosException. Este m??todo debe consultar las p??ginas de cada libro,
 * multiplicar por el precio por p??gina y rellenar la columna precio de cada libro. Resu??lvelo con la consulta
 * SELECT_LIBROS_QUERY utilizando un ResultSet adecuado, cursores y sus m??todos.
 * Prueba que funciona el nuevo m??todo en la clase con la funci??n main.
 */

    public void rellenaPrecio(float precio) throws AccesoDatosException{

        stmt = null;
        pstmt = null;

        Libro lb = new Libro();
      try{
              stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
              //stmt.executeUpdate(INSERT_COLUMNA_PRECIO);
              rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

              while (rs.next()){
                  double precioTotal = (rs.getInt("paginas") * precio);
                  rs.updateDouble("precio", precioTotal);
                  rs.updateRow();
                  lb.setPrecio(precioTotal);
                  System.out.println(lb.getPrecio());
              }

          System.out.println();
      } catch (SQLException sqle) {
        // En una aplicaci??n real, escribo en el log y delego
        Utilidades.printSQLException(sqle);
        throw new AccesoDatosException(
                "Ocurri?? un error al acceder a los datos");
    } finally {
        try {
            // Liberamos todos los recursos pase lo que pase
            if (stmt != null) {
                stmt.close();
            }

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }
    }

/**
 * A??ade un nuevo m??todo a la clase libros que reciba dos isbn y un float que indica el precio por p??gina:
 *
 * public void actualizaPrecio(int isbn1, int isbn2, float precio)
 *      throws AccesoDatosException
 *
 * Este m??todo realizar?? lo siguiente utilizando cursores:
 * a.       Consultar?? las p??ginas de los dos libros y calcular?? el precio para los dos.
 * b.      Actualizar?? para ambos libros el precio con el precio m??ximo obtenido del c??lculo anterior.
 * c.       Las 2 operaciones de actualizaci??n  de precio de los libros tienen que ser una transacci??n
 * Prueba que funciona el nuevo m??todo en la clase con la funci??n main.
 */


    public void actualizaPrecio(int isbn1, int isbn2, float precio) throws AccesoDatosException{

        stmt = null;
        pstmt = null;
        Libro lb = new Libro();

        try{
            con.setAutoCommit(false);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            double precioMaximo = 0;

            while (rs.next()){
                if(rs.getInt("isbn") == isbn1 || rs.getInt("isbn") == isbn2){
                    double precioTotal = (rs.getInt("paginas") * precio);
                    if(precioMaximo < precioTotal){
                        precioMaximo = precioTotal;
                    }
                }
            }

            rs.beforeFirst();

            while(rs.next()){
                if(rs.getInt("isbn") == isbn1 || rs.getInt("isbn") == isbn2){
                    rs.updateDouble("precio", precioMaximo);
                    rs.updateRow();
                }
            }

            con.commit();

    } catch (SQLException sqle) {
        // En una aplicaci??n real, escribo en el log y delego
        Utilidades.printSQLException(sqle);
        throw new AccesoDatosException(
                "Ocurri?? un error al acceder a los datos");
    } finally {
        try {
            // Liberamos todos los recursos pase lo que pase
            if (stmt != null) {
                stmt.close();
            }

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }

}

/**
 * A??ade un nuevo m??todo a la clase libros que reciba un isbn, un float que indica el precio por p??gina y un n??mero de p??ginas.
 *
 * public void actualizaPrecio(int isbn, float precio,  int paginas) throws AccesoDatosException;
 *
 *  este m??todo realizar?? lo siguiente utilizando cursores:
 *
 *
 * a.       Sumar?? el n??mero de p??ginas a las p??ginas actuales que ya tiene el libro
 * b.      Calcula el precio multiplicando el total de p??ginas por el precio por p??gina
 * c.       Actualiza el precio del libro
 * d.      Las 2 operaciones de actualizaci??n ser una transacci??n
 * e.       Resu??lvelo utilizando ResultSet.CONCUR_UPDATABLE
 * Prueba que funciona el nuevo m??todo en la clase con la funci??n main
 * F??jate bien lo sencillo que resulta realizar una transacci??n con .CONCUR_UPDATABLE cuando los cambios afectan al mismo ResultSet.
 */


    public void actualizaPrecio2(int isbn, float precio,  int paginas) throws AccesoDatosException{

    stmt = null;
    pstmt = null;
        try{

            con.setAutoCommit(false);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while (rs.next()){

                if(rs.getInt("isbn") == isbn){
                    int totalPaginas = rs.getInt("paginas") + paginas;
                    rs.updateInt("paginas", totalPaginas);
                    rs.updateRow();

                    double nuevoPrecio = rs.getInt("paginas") * precio;
                    rs.updateDouble("precio", nuevoPrecio);
                    rs.updateRow();
                }
            }

            con.commit();

    } catch (SQLException sqle) {
        // En una aplicaci??n real, escribo en el log y delego
        Utilidades.printSQLException(sqle);
        throw new AccesoDatosException(
                "Ocurri?? un error al acceder a los datos");
    } finally {
        try {
            // Liberamos todos los recursos pase lo que pase
            if (stmt != null) {
                stmt.close();
            }

        } catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }
    }

    /**
     * A??ade un nuevo m??todo a la clase libros que reciba dos isbn y realice lo siguiente utilizando cursores:
     *   public void copiaLibro(int isbn1, int isbn2) throws AccesoDatosException {
     *
     * a.       Consultar?? los datos del primer isbn
     * b.      Insertar?? una nueva fila con el segundo isbn copiando el resto de datos de los obtenidos en la consulta anterior.
     * c.       Debes resolverlo utilizando s??lo la consulta del primer isbn y un resultset de tipo adecuado.
     */

    public void copiaLibro(int isbn1, int isbn2) throws AccesoDatosException{

        stmt = null;
        pstmt = null;

        try{
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while(rs.next()){

                if(rs.getInt("isbn") == isbn1){
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String editorial = rs.getString("editorial");
                    int paginas = rs.getInt("paginas");
                    int copias = rs.getInt("copias");
                    double precio = rs.getDouble("precio");

                    rs.moveToInsertRow();
                    rs.updateInt("isbn", isbn2);
                    rs.updateString("titulo", titulo);
                    rs.updateString("autor", autor);
                    rs.updateString("editorial", editorial);
                    rs.updateInt("paginas", paginas);
                    rs.updateInt("copias", copias);
                    rs.updateDouble("precio", precio);

                    rs.insertRow();
                }
            }
        }catch (SQLException sqle) {
            // En una aplicaci??n real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurri?? un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicaci??n real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
        }

}

