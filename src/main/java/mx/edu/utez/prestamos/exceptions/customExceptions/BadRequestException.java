package mx.edu.utez.prestamos.exceptions.customExceptions;
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
