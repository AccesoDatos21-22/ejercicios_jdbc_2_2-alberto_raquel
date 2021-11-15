package org.iesinfantaelena.dao;

import org.iesinfantaelena.modelo.AccesoDatosException;

public interface CafesDAO {

    public void verTabla() throws AccesoDatosException;

    public void buscar(String nombre) throws AccesoDatosException;

    public void insertar(String nombre, int provid, float precio, int ventas,
                         int total) throws AccesoDatosException;

    public void borrar(String nombre)  throws AccesoDatosException;

    public void cafesPorProveedor(int provid) throws AccesoDatosException;

}
