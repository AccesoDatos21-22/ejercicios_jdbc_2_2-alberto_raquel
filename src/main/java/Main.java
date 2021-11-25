import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.util.*;

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
            Libro libro2 = new Libro(2222,"Reina Roja","Juan Gómez Jurado","Patatin",300,500000);
            Libro libro3 = new Libro(333,"La villa de las telas","Anne Jacobs","Libritos",250,20000);


            libroprueba.anadirLibro(libro1);
            libroprueba.anadirLibro(libro2);
            libroprueba.anadirLibro(libro3);

            //libroprueba.borrar(libro1);
            //libroprueba.obtenerLibro(1111);
            //libroprueba.librosporEditorial("Éditions Gallimard");
            //libroprueba.actualizarCopias(libro1);

            //Probando el ejercicio 1
            System.out.println("Ejercicio 1 -->");
            System.out.println(Arrays.toString(libroprueba.getCamposLibro()));
            libroprueba.verCatalogoInverso();

            //Probando el ejercicio 2
            System.out.println("Ejercicio 2 -->");
            HashMap<Integer, Integer> actCopias = new HashMap<>();
            actCopias.put(1111, 5000);
            libroprueba.actualizarCopias(actCopias);

        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }


}