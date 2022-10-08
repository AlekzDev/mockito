package net.alekz.examples.exceptions;

/**
 * @author alekz
 * @Date 08/10/22
 */
public class NoDataException extends RuntimeException{
    public NoDataException(String message) {
        super(message);
    }
}
