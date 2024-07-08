package my.code.nftmarketplacepublic.exceptions;

public class SameCardTransactionException extends RuntimeException{
    public SameCardTransactionException(String message){
        super(message);
    }
}
