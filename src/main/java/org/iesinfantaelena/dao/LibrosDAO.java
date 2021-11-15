package org.iesinfantaelena.dao;

import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.sql.SQLException;
import java.util.List;

public interface LibrosDAO {

    public void cerrar() throws AccesoDatosException;

    public void liberar() throws AccesoDatosException;

    public List<Libro> verCatalogo() throws AccesoDatosException;

    public void actualizarCopias(Libro libro) throws AccesoDatosException;

    public void anadirLibro(Libro libro) throws AccesoDatosException;

    public void borrar(Libro libro) throws AccesoDatosException;

    public String[] getCamposLibro() throws AccesoDatosException;

    public void obtenerLibro(int ISBN) throws AccesoDatosException;

    public boolean crearTablaLibros() throws SQLException;

    public void librosporEditorial(String editorial) throws AccesoDatosException;
}
