package my.code.nftmarketplacepublic.controllers.settings;

import com.jdbc.nftmarketplace2.dtos.CustomResponseDto;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public CustomResponseDto<String> insufficientFundsExceptionHandler(InsufficientFundsException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.PAYMENT_REQUIRED.getCode(), null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseDto<String> InvalidCardDataException(InvalidCardDataException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponseDto<String> NftIsNotSellingException(NftIsNotSellingException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponseDto<String> NoBankCardException(NoBankCardException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponseDto<String> NoMetamaskWalletException(NoMetamaskWalletException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseDto<String> NotValidEmailException(NotValidEmailException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseDto<String> PasswordCanNotBeChangedException(PasswordCanNotBeChangedException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseDto<String> SameUserTransactionException(SameUserTransactionException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponseDto<String> UserNotFoundException(UserNotFoundException exception){
        log.error(exception.getMessage(), exception);
        return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),null, exception.getMessage());
    }
}
