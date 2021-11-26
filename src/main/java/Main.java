import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Cafe;
import org.iesinfantaelena.modelo.Libro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.*;

public class Main {

    public static void main(String[] args) {


        try {
            Cafes cafes = new Cafes();
            cafes.insertar("Cafetito", 150, 1.0f, 100,1000);
            cafes.insertar("Tacilla", 150, 2.0f, 100,1000);
            cafes.verTabla();
            //cafes.buscar("tacilla");
            //cafes.cafesPorProveedor(150);
            //cafes.borrar("Cafe tacilla");
            //cafes.verTabla();

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


            //Probando el ejercicio 3
            System.out.println("Ejercicio 3 -->");
            int[]filas = {1,2};
            libroprueba.verCatalogo(filas);


            //Probando el ejercicio 4
            System.out.println("Ejercicio 4 -->");
            libroprueba.rellenaPrecio(0.10f);


            //Probando el ejercicio 5
            System.out.println("Ejercicio 5 -->");
            libroprueba.actualizaPrecio(1111, 2222, 0.15f);
            System.out.println(libroprueba.verCatalogo());

            //Probando el ejercicio 6
            System.out.println("Ejercicio 6 -->");
            libroprueba.actualizaPrecio2(1111, 0.20f,  350);
            System.out.println(libroprueba.verCatalogo());

            //Probando el ejercicio 7
            System.out.println("Ejercicio 7 -->");
            libroprueba.copiaLibro(1111,3333);
            System.out.println(libroprueba.verCatalogo());

            //Probando el ejercicio 8
            System.out.println("Ejercicio 8 -->");
            cafes.transferencia("Cafetito", "Tacilla");
            cafes.verTabla();

        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }



}