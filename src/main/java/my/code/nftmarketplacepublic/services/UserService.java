package my.code.nftmarketplacepublic.services;


import com.jdbc.nftmarketplace2.dtos.*;
import com.jdbc.nftmarketplace2.entities.MetaMaskWallet;
import com.jdbc.nftmarketplace2.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    CreateUserDto create(CreateUserDto userToCreate);
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    UserDto update(UserDto userToUpdate);
    String delete(Long id);
    MetaMaskWallet addMetaMaskWallet(String walletAddress, String walletPassword);
    String addBankcard(AddBankCardDto cardToAdd);
    List<UserRateDto> getRateOfUsersByDays(Long days);
    UserRateDto getRateOfUserByDays(Long days);
    String changeProfilePhoto(MultipartFile multipartFile) throws IOException;
    String sendResetCode(String email);
    String confirmCode(String email, Integer code);
    String dropForgottenPassword(String email, String newPassword);
    String followUser(Long userId);
    String unfollowUser(Long userId);
    UserProfileDto getProfile(Long id);
    boolean isUsernameUnique(String username);
    boolean isEmailUnique(String email);

}