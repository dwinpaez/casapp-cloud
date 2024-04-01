package ca.casapp.springcloud.msvc.clients.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class ClientEmailNotFoundException extends RuntimeException {

    public ClientEmailNotFoundException() {
        super();
    }

    public ClientEmailNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClientEmailNotFoundException(final String message) {
        super(message);
    }

    public ClientEmailNotFoundException(final Throwable cause) {
        super(cause);
    }

}
