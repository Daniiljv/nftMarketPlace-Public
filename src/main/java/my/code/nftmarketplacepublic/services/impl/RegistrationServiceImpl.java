package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.dtos.CreateUserDto;
import com.jdbc.nftmarketplace2.entities.Registration;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.repositories.RegistrationRepo;
import com.jdbc.nftmarketplace2.services.MailService;
import com.jdbc.nftmarketplace2.services.RegistrationService;
import com.jdbc.nftmarketplace2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder passwordEncoder;

    private final RegistrationRepo registrationRepo;

    private final UserService userService;
    private final MailService mailService;

    @Override
    public String register(CreateUserDto createUserDto) {

        if(!userService.isUsernameUnique(createUserDto.getUsername()) ||
        !isUsernameUnique(createUserDto.getUsername())){
            throw new IllegalArgumentException("Пользователь с именем %s не может быть зарегистрирован!"
                    .formatted(createUserDto.getUsername()));
        }

        if(!userService.isEmailUnique(createUserDto.getEmail()) ||
                !isEmailUnique(createUserDto.getEmail())){
            throw new IllegalArgumentException("Пользователь с почтой %s не может быть зарегистрирован!"
                    .formatted(createUserDto.getEmail()));
        }

        int code = mailService.sendRegistrationCode(createUserDto.getEmail());

        String password = passwordEncoder.encode(createUserDto.getPassword());

        registrationRepo.save(new Registration(null, createUserDto.getUsername(),
                createUserDto.getEmail(), password, code,
                new Timestamp(System.currentTimeMillis()), false));

        return "Подтвердите отправленный вам код для завершения регистрации";
    }

    @Override
    public String confirmRegistration(String email, Integer code) {

        Registration userToRegister = registrationRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException
                        ("Пользователь с почтой %s не пытался зарегистрироваться!"));

        if(!userToRegister.getCode().equals(code)){
            throw new IllegalArgumentException("Неверный код подтверждения!");
        }

        userService.create(new CreateUserDto(null, userToRegister.getUsername(),
                userToRegister.getEmail(), userToRegister.getPassword()));

        userToRegister.setConfirmed(true);
        registrationRepo.save(userToRegister);

        return "Почта была успешно подверждена, теперь вы можете авторизоваться";
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return registrationRepo.findByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailUnique(String email) {
        return registrationRepo.findByEmail(email).isEmpty();
    }


}
