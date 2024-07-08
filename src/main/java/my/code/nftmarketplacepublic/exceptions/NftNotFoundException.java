package my.code.nftmarketplacepublic.exceptions;

public class NftNotFoundException extends RuntimeException {
    public NftNotFoundException(String message){
        super(message);
    }
}
