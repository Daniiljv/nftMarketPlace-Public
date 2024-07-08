package my.code.nftmarketplacepublic.exceptions;

public class CollectionNotFoundException extends RuntimeException{
    public CollectionNotFoundException(String message){
        super(message);
    }
}
