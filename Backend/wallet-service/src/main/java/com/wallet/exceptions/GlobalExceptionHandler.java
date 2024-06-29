package com.wallet.exceptions;

import com.wallet.payloads.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exception)
    {
        logger.error("GlobalException: {}", exception);
        String message=exception.getMessage();
        ApiResponse apiResponse=new ApiResponse(message,false,null);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception exception)
    {
        logger.error("GlobalException: {}", exception);
        String message= exception.getMessage();
        ApiResponse apiResponse=new ApiResponse(message,false,null);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CurrencySaveException.class)
    public ResponseEntity<ApiResponse> handleCurrencySaveException(CurrencySaveException exception)
    {
        logger.error("GlobalException: {}", exception);
        String message=exception.getMessage();
        ApiResponse apiResponse=new ApiResponse(message,false,null);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
