package my.code.nftmarketplacepublic.services;

import com.jdbc.nftmarketplace2.dtos.CreateUserDto;
import org.springframework.mail.MailException;

public interface MailService {
    Integer sendRegistrationCode(String email);
    void sendSuccessfulRegistration(CreateUserDto user) throws MailException;
    void sendResetCode(String email);
}
