package my.code.nftmarketplacepublic.exceptions;

public class NoMetamaskWalletException extends RuntimeException{
    public NoMetamaskWalletException(String message){
        super(message);
    }
}
