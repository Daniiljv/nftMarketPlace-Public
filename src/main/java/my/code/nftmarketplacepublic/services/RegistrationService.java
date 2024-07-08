package my.code.nftmarketplacepublic.services;

import com.jdbc.nftmarketplace2.dtos.CreateUserDto;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {
    String register(CreateUserDto createUserDto);
    String confirmRegistration(String email, Integer code);

    boolean isUsernameUnique(String username);
    boolean isEmailUnique(String email);
}
