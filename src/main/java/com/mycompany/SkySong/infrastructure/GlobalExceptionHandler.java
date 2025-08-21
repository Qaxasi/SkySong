//package com.mycompany.SkySong.adapter.exception.handler;
//
//import com.mycompany.SkySong.shared.error.AppException;
//import com.mycompany.SkySong.shared.response.ErrorResponse;
//import com.mycompany.SkySong.shared.error.ErrorType;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@Slf4j
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(AppException.class)
//    public ResponseEntity<ErrorResponse> handleBaseApiException(AppException ex) {
//        return ResponseEntity
//                .status(ex.getErrorType().getHttpStatus())
//                .body(new ErrorResponse(
//                        ex.getMessage(),
//                        ex.getErrorType().name(),
//                        ex.getErrorType().getHttpStatus().value()));
//
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
//        return ResponseEntity
//                .status(ErrorType.PERSISTENCE_ERROR.getHttpStatus())
//                .body(new ErrorResponse("An unexpected error occurred. Please try again later.",
//                        ErrorType.PERSISTENCE_ERROR.name(),
//                        ErrorType.PERSISTENCE_ERROR.getHttpStatus().value()));
//    }
//}
