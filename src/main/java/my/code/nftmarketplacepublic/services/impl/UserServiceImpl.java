package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.dtos.*;
import com.jdbc.nftmarketplace2.entities.*;
import com.jdbc.nftmarketplace2.enums.CryptoCurrencyType;
import com.jdbc.nftmarketplace2.exceptions.*;
import com.jdbc.nftmarketplace2.repositories.*;
import com.jdbc.nftmarketplace2.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final NftRepo nftRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final MetaMaskRepo metaMaskRepo;
    private final BankCardRepo bankCardRepo;
    private final TempResetCodeRepo tempResetCodeRepo;
    private final CryptoCurrencyRepo cryptoCurrencyRepo;

    private final MailService mailService;
    private final FairBaseImageService fairBaseImageService;
    private final TransactionInfoService transactionInfoService;

    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    @Override
    public CreateUserDto create(CreateUserDto userToCreate) {
        try {

            if (!isEmailUnique(userToCreate.getEmail())) {
                throw new IllegalArgumentException("Пользователь с почтой - %s не может быть зарегистрирован!"
                        .formatted(userToCreate.getEmail()));
            }

            if (!isUsernameUnique(userToCreate.getUsername())) {
                throw new IllegalArgumentException("Пользователь с именем - %s не может быть зарегистрирован!"
                        .formatted(userToCreate.getUsername()));
            }

            Role roleUser = roleRepo.findByName("USER");
            Set<Role> roleEntities = new HashSet<>();
            roleEntities.add(roleUser);

            User user = User.builder()
                    .username(userToCreate.getUsername())
                    .email(userToCreate.getEmail())
                    .password(userToCreate.getPassword())
                    .profileImage("https://firebasestorage.googleapis.com/v0/b/nftmarketplace-f8ce1.appspot.com/o/profileImages%2Fphoto_5422392114257451921_x.jpg?alt=media&token=f36f3c61-7779-430c-a069-1dc9a88d76ac")
                    .countOfSoldNft(0L)
                    .transactionVolume(BigDecimal.valueOf(0))
                    .followersIds(new ArrayList<>())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .roles(roleEntities)
                    .build();

            userToCreate.setId(userRepo.save(user).getId());

            mailService.sendSuccessfulRegistration(userToCreate);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return userToCreate;
    }

    @Override
    public User findById(Long id) {
        return userRepo.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден!"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем" + username + " не найден"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UserNotFoundException
                        (String.format("Пользователь с почтой: %s не найден", email)));
    }

    @Override
    public UserDto update(UserDto userToUpdate) {

        User user = findById(userToUpdate.getId());

        user.setUsername(userToUpdate.getUsername());
        user.setEmail(userToUpdate.getEmail());
        user.setPassword(userToUpdate.getPassword());
        user.setCountOfSoldNft(userToUpdate.getCountOfSoldNft());
        user.setTransactionVolume(userToUpdate.getTransactionVolume());
        user.setFollowersIds(userToUpdate.getFollowersIds());
        user.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
        userRepo.save(user);

        userToUpdate.setLastUpdatedAt(user.getLastUpdatedAt());
        return userToUpdate;
    }

    @Override
    public String delete(Long id) {
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();

        User requestUser = findByUsername(authentication.getName());
        User userToDelete = findById(id);

        if (!requestUser.getId().equals(userToDelete.getId()) &&
                !requestUser.getRoles().contains(roleRepo.findByName("ADMIN"))){
            throw new InsufficientAccessException("Пользователь не может быть удален!");
        }

        userToDelete.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        userRepo.save(userToDelete);

        return "Пользователь с id " + id + " был удален успешно";
    }

    @Override
    public MetaMaskWallet addMetaMaskWallet(String walletAddress, String walletPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = findByUsername(authentication.getName());

        MetaMaskWallet metaMaskWallet = metaMaskRepo.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new NoMetamaskWalletException("MetaMask с адрессом - " + walletAddress + " не найден"));

        if (!metaMaskWallet.getPassword().equals(walletPassword)) {
            throw new IllegalArgumentException("Пароль от MetaMask не верный!");
        }

        user.setMetaMaskWallet(metaMaskWallet);
        userRepo.save(user);

        return metaMaskWallet;
    }

    @Override
    public String addBankcard(AddBankCardDto cardToAdd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = findByUsername(authentication.getName());

        BankCard bankCard = bankCardRepo.findByCardNumber(cardToAdd.getCardNumber())
                .orElseThrow(() -> new NoBankCardException("Карта с номером - %s не найдена!"
                        .formatted(cardToAdd.getCardNumber())));

        if (!bankCard.getCardExpiryDate().equals(cardToAdd.getCardExpiryDate()) ||
                !bankCard.getCvv().equals(cardToAdd.getCvv())) {
            throw new InvalidCardDataException("Время экспирации или cvv код не верны!");
        }

        user.setBankCard(bankCard);
        userRepo.save(user);

        return "Карта с номером - " + bankCard.getCardNumber() + " была успешно добавленна!";
    }

    @Override
    public List<UserRateDto> getRateOfUsersByDays(Long days) {
        List<UserRateDto> result = new ArrayList<>();

        List<User> userList = userRepo.findAllByDeletedAtIsNull().
                orElseThrow(() -> new UserNotFoundException("Список пуст"));

        for (User u : userList) {
            UserRateDto userRateDto = UserRateDto.builder()
                    .profileImage(u.getProfileImage())
                    .username(u.getUsername())
                    .statistic(transactionInfoService.getUserStatisticForDays(u.getId(), days))
                    .countOfSoldNft(u.getCountOfSoldNft())
                    .transactionVolume(u.getTransactionVolume())
                    .build();
            result.add(userRateDto);
        }

        result.sort(Comparator.comparing(UserRateDto::getTransactionVolume).reversed());

        return result;
    }

    @Override
    public UserRateDto getRateOfUserByDays(Long days) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(authentication.getName());

        return UserRateDto.builder()
                .profileImage(user.getProfileImage())
                .username(user.getUsername())
                .statistic(transactionInfoService.getUserStatisticForDays(user.getId(), days))
                .countOfSoldNft(user.getCountOfSoldNft())
                .transactionVolume(user.getTransactionVolume())
                .build();

    }

    @Override
    public String changeProfilePhoto(MultipartFile multipartFile) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User user = userRepo.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с именем " + authentication.getName() + " не найден"));

            user.setProfileImage(fairBaseImageService.saveUserProfileImage(multipartFile));
            user.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
            userRepo.save(user);

            return user.getProfileImage();

        } catch (IOException ioException) {
            log.error(ioException.getMessage());
            throw new RuntimeException(ioException.getMessage());
        }
    }

    @Override
    public String sendResetCode(String email) {
        try {
            findByEmail(email);
            mailService.sendResetCode(email);

        } catch (NotValidEmailException notValidEmailException) {
            log.error(notValidEmailException.getMessage());
            throw new NotValidEmailException(notValidEmailException.getMessage());
        }

        return "Код был успешно отправлен на почту " + email;
    }

    @Override
    public String confirmCode(String email, Integer code) {
        try {
            TempResetCode tempResetCode = tempResetCodeRepo.findByEmail(email)
                    .orElseThrow(() -> new PasswordCanNotBeChangedException("Пароль не может быть изменен"));

            if (!tempResetCode.getCode().equals(code) &&
                    (tempResetCode.getExpiredTime().compareTo(new Timestamp(System.currentTimeMillis())) < 0)) {
                throw new RuntimeException("Время экспирации кода истекло, попробуйте получить новый!");
            }

            User user = findByEmail(email);

            Role role = roleRepo.findByName("ABLE_DROP_FORGOTTEN_PASSWORD");
            Set<Role> userRoles = user.getRoles();
            userRoles.add(role);
            userRepo.save(user);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return "Код был подтвержден успешно, теперь вы можете сбросить свой пароль";
    }

    @Override
    public String dropForgottenPassword(String email, String newPassword) {
        User user = findByEmail(email);

        Role accessRole = roleRepo.findByName("ABLE_DROP_FORGOTTEN_PASSWORD");

        if (!user.getRoles().contains(accessRole)) {
            throw new PasswordCanNotBeChangedException("Пароль не может быть сброшен!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.getRoles().remove(accessRole);
        userRepo.save(user);

        return "Пароль был изменен успешно!";
    }

    @Override
    public String followUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User follower = findByUsername(authentication.getName());
        User userToFollow = findById(userId);

        if(userToFollow.getFollowersIds().contains(follower.getId())){
            return "Пользователь уже подписан на этот аккаунт";
        }

        userToFollow.getFollowersIds().add(follower.getId());
        userRepo.save(userToFollow);

        return "Пользователь успешно подписался!";
    }

    @Override
    public String unfollowUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User follower = findByUsername(authentication.getName());
        User userToUnfollow = findById(userId);

        if(userToUnfollow.getFollowersIds().contains(follower.getId())){
            return "Пользователь не подписан на этот аккаунт";
        }

        userToUnfollow.getFollowersIds().remove(follower.getId());
        userRepo.save(userToUnfollow);

        return "Пользователь успешно отписался!";
    }

    @Override
    public UserProfileDto getProfile(Long id) {
        User user = findById(id);

        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .countOfSoldNft(user.getCountOfSoldNft())
                .transactionVolume(user.getTransactionVolume())
                .profileImage(user.getProfileImage())

                .followersCount(user.getFollowersIds() == null ? 0 : (long) user.getFollowersIds().size())

                .createdNfts(nftRepo.findAllByDeletedAtIsNullAndCreatorId(id)
                        .stream()
                        .map(nft -> {
                            nft.setEthereumPrice(nft.getDollarPrice().divide(
                                    cryptoCurrencyRepo.findByCryptoCurrencyType(CryptoCurrencyType.ETH).getPrice(),
                                    8,
                                    RoundingMode.HALF_UP));
                            nft.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
                            nftRepo.save(nft);

                            User nftCreator = userRepo.findUserById(nft.getCreatorId());
                            return new ProfileNftDto(nft.getId(),
                                    nft.getName(),
                                    nft.getDollarPrice(),
                                    nft.getEthereumPrice(),
                                    nft.getNftImage(),
                                    nftCreator.getUsername(),
                                    nftCreator.getProfileImage());
                        }).toList())

                .ownedNfts(nftRepo.findAllByDeletedAtIsNullAndOwnerId(id)
                        .stream()
                        .map(nft -> {
                            nft.setEthereumPrice(nft.getDollarPrice().divide(
                                    cryptoCurrencyRepo.findByCryptoCurrencyType(CryptoCurrencyType.ETH).getPrice(),
                                    8,
                                    RoundingMode.HALF_UP));
                            nft.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
                            nftRepo.save(nft);

                            User nftCreator = userRepo.findUserById(nft.getCreatorId());
                            return new ProfileNftDto(nft.getId(),
                                    nft.getName(),
                                    nft.getDollarPrice(),
                                    nft.getEthereumPrice(),
                                    nft.getNftImage(),
                                    nftCreator.getUsername(),
                                    nftCreator.getProfileImage());
                        }).toList())
                .build();
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return userRepo.findByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userRepo.findByEmail(email).isEmpty();
    }

}
