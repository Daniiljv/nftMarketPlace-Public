package my.code.nftmarketplacepublic.exceptions;

public class SameUserTransactionException extends RuntimeException{
    public SameUserTransactionException(String message){
        super(message);
    }
}
