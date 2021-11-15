package org.iesinfantaelena.modelo;

public class Cafe {

    private String nombre;
    private int provid;
    private float precio;
    private int ventas;
    private int total;

    public Cafe() {

    }

    public Cafe(String nombre, int provid, float precio, int ventas, int total) {
        this.nombre = nombre;
        this.provid = provid;
        this.precio = precio;
        this.ventas = ventas;
        this.total = total;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getProvid() {
        return provid;
    }
    public void setProvid(int provid) {
        this.provid = provid;
    }
    public float getPrecio() {
        return precio;
    }
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    public int getVentas() {
        return ventas;
    }
    public void setVentas(int ventas) {
        this.ventas = ventas;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
