package org.iesinfantaelena.modelo;
/**
 *
 *  @descrition Clase Raiz para la jerarquia de Excepciones de mi aplicación
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class MercadoException extends Exception {

    /**
     * Necesario por impmentar Serializable
     */
    private static final long serialVersionUID = 6308847858962342271L;

    public MercadoException(String message) {
        super(message);
    }

}
