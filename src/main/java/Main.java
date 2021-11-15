import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {


        try {
            Cafes cafes = new Cafes();
            cafes.insertar("Cafetito", 150, 1.0f, 100,1000);
            cafes.insertar("Cafe tacilla", 150, 2.0f, 100,1000);
            cafes.verTabla();
            cafes.buscar("tacilla");
            cafes.cafesPorProveedor(150);
            cafes.borrar("Cafe tacilla");
            cafes.verTabla();

            Libros libroprueba = new Libros();
            Libro libro1 = new Libro(1111,"El principito","Antoine de Saint-Exupéry","Éditions Gallimard",100,6000000);

            ArrayList listalibros = (ArrayList) libroprueba.verCatalogo();
            listalibros.add(libro1);

            libroprueba.anadirLibro(libro1);
            libroprueba.borrar(libro1);
            libroprueba.obtenerLibro(1111);
            libroprueba.librosporEditorial("Éditions Gallimard");
            libroprueba.actualizarCopias(libro1);

            System.out.println(Arrays.toString(libroprueba.getCamposLibro()));


        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }

}