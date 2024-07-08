package my.code.nftmarketplacepublic.exceptions;

public class InsufficientAccessException extends RuntimeException {
    public InsufficientAccessException(String message){
        super(message);
    }
}
