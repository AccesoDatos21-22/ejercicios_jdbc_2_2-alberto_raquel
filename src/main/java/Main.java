import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {


        try {

            Libros libroprueba = new Libros();
            Libro libro1 = new Libro(1111,"El principito","Antoine de Saint-Exupéry","Éditions Gallimard",100,6000000);
            Libro libro2 = new Libro(2222,"Harry Potter y la piedra filosofal","JK Rowling","Nose",300,99000000);

            libroprueba.anadirLibro(libro1);
            libroprueba.anadirLibro(libro2);

            libroprueba.verCatalogoInverso();


        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }

}