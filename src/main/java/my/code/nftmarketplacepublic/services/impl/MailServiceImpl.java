package my.code.nftmarketplacepublic.services.impl;


import com.jdbc.nftmarketplace2.dtos.CreateUserDto;
import com.jdbc.nftmarketplace2.entities.TempResetCode;
import com.jdbc.nftmarketplace2.exceptions.NotValidEmailException;
import com.jdbc.nftmarketplace2.repositories.TempResetCodeRepo;
import com.jdbc.nftmarketplace2.services.MailService;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

@Service
@AllArgsConstructor
@Getter
public class MailServiceImpl implements MailService {

    private final Random random = new Random();

    private final JavaMailSender javaMailSender;

    private final TempResetCodeRepo tempResetCodeRepo;

    @Override
    public Integer sendRegistrationCode(String email) {
        if (email == null || !isValidEmailAddress(email)) {
            throw new NotValidEmailException("Почта " + email + " не валидна");
        }

        int code = random.nextInt(10000, 99999);

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("Подтверждение регистрации");
        mail.setText("""
                Это ваш код для регистрации в приложении NftMarketPlace.
                CODE:\t %s
                Если вы не совершали регистрацию, просто проигнорируйте это сообщение""".formatted(code));
        javaMailSender.send(mail);

        return code;
    }

    public void sendSuccessfulRegistration(CreateUserDto user) throws MailException {
        if (user.getEmail() != null && isValidEmailAddress(user.getEmail())) {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(user.getEmail());
            mail.setSubject("NFT MarketPlace");
            mail.setText("Ваша регистрация прошла успешно!\n" +
                    "Аккаунт : \n" +
                    "\nID - " + user.getId() +
                    "\nUsername - " + user.getUsername());
            javaMailSender.send(mail);
        }
    }

    @Override
    public void sendResetCode(String email) {
        if (email != null && isValidEmailAddress(email)) {

            int code = random.nextInt(10000, 99999);

            TempResetCode tempResetCode = tempResetCodeRepo.findByEmail(email).orElse(null);

            if (tempResetCode != null) {
                tempResetCode.setCode(code);
                tempResetCode.setExpiredTime(new Timestamp(System.currentTimeMillis() + (60 * 1000 * 300)));
                tempResetCodeRepo.save(tempResetCode);
            } else {
                tempResetCodeRepo.save(TempResetCode.builder()
                        .email(email)
                        .code(code)
                        .expiredTime(new Timestamp(System.currentTimeMillis()))
                        .build());
            }

            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(email);
            mail.setSubject("Сброс пароля");
            mail.setText("""
                    Это ваш код для сброса пароля.
                    Не сообщайте его никому!
                    CODE:\t""" + code);
            javaMailSender.send(mail);

        } else {
            throw new NotValidEmailException("Почта " + email + " не валидна");
        }
    }

    private boolean isValidEmailAddress(String mailAddress) {
        boolean result = true;
        try {
            InternetAddress email = new InternetAddress(mailAddress);
            email.validate();
        } catch (Exception e) {
            throw new NotValidEmailException("Почта " + mailAddress + " не валидна");
        }
        return result;
    }

}
