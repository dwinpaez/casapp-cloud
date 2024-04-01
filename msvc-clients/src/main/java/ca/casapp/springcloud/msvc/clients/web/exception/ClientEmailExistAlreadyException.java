package ca.casapp.springcloud.msvc.clients.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public final class ClientEmailExistAlreadyException extends RuntimeException {

    public ClientEmailExistAlreadyException() {
        super();
    }

    public ClientEmailExistAlreadyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClientEmailExistAlreadyException(final String message) {
        super(message);
    }

    public ClientEmailExistAlreadyException(final Throwable cause) {
        super(cause);
    }

}
