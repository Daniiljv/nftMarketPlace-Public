package my.code.nftmarketplacepublic.exceptions;

public class NoBankCardException extends RuntimeException{
    public NoBankCardException(String message){
        super(message);
    }
}
