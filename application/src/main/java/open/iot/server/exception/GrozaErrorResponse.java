package open.iot.server.exception;

import open.iot.server.common.data.exception.GrozaErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Date;


public class GrozaErrorResponse {
    // HTTP Response Status Code
    private final HttpStatus status;

    // General Error message
    private final String message;

    //Error code
    private final GrozaErrorCode errorCode;

    private final Date timestamp;

    public GrozaErrorResponse(final String message, GrozaErrorCode errorCode, HttpStatus status){
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = new Date();
    }

    public static GrozaErrorResponse of(final String message, final GrozaErrorCode errorCode, HttpStatus status) {
        return new GrozaErrorResponse(message, errorCode, status);
    }

    public Integer getStatus(){
        return status.value();
    }

    public String getMessage(){
        return message;
    }

    public GrozaErrorCode getErrorCode(){
        return errorCode;
    }

    public Date getTimestamp(){
        return timestamp;
    }
}
