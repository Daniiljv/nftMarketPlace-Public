package my.code.nftmarketplacepublic.exceptions;

public class PasswordCanNotBeChangedException extends RuntimeException{
    public PasswordCanNotBeChangedException(String message){
        super(message);
    }
}
