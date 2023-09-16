package com.mycompany.SkySong.exception;

import com.mycompany.SkySong.config.ErrorResponse;
import com.mycompany.SkySong.config.ErrorResponseBuilder;
import com.mycompany.SkySong.location.exception.LocationNotFound;
import com.mycompany.SkySong.location.exception.LocationNotGiven;
import com.mycompany.SkySong.location.exception.LocationServiceException;
import com.mycompany.SkySong.location.exception.TooManyRequestsException;
import com.mycompany.SkySong.music.authorization.exception.AuthorizationException;
import com.mycompany.SkySong.weather.exception.WeatherDataSaveException;
import com.mycompany.SkySong.weather.exception.WeatherException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleTooManyRequestsException(final TooManyRequestsException exception) {
        return ErrorResponseBuilder.createFromGeneralException(exception);
    }
    @ExceptionHandler(ServerIsUnavailable.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleServerIsUnavailable() {
        ErrorResponse errorResponse = new ErrorResponse("Failed to fetch geocoding data. Please try again later");
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(LocationNotGiven.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleLocationNotGivenException(final LocationNotGiven exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
       return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }














    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleValidationException(ValidationException ex,
                                                                  WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(LocationServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleGeocodingException(LocationServiceException ex,
                                                                 WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(WeatherException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleWeatherException(WeatherException ex,
                                                         WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(WeatherDataSaveException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleWeatherDataSaveException(WeatherDataSaveException ex,
                                                                       WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleAuthorizationException(AuthorizationException ex,
                                                                     WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

}
