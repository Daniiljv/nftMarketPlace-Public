package my.code.nftmarketplacepublic.controllers.settings;

import com.jdbc.nftmarketplace2.dtos.CustomResponseDto;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomResponseDto<String> runtimeExceptionHandler(RuntimeException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null, exception.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomResponseDto<String> exceptionHandler(Exception exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),null, exception.getMessage());
    }
}
