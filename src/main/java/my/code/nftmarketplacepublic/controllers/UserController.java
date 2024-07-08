
package my.code.nftmarketplacepublic.controllers;

import com.jdbc.nftmarketplace2.dtos.*;
import com.jdbc.nftmarketplace2.entities.MetaMaskWallet;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.*;
import com.jdbc.nftmarketplace2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь создан успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserDto.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь не был добавлен в базу")
    })
    @Operation(summary = "Эндпоинт принимает такую же модель которую и возвращает  и создает Пользователя," +
            "для создания необходимо передать только username, email, password и confirmPassword остальное сгенерирует бэк")

    @PostMapping("/registration")
    public CustomResponseDto<CreateUserDto> create(@RequestBody CreateUserDto createUserDto) {
        try {
            return new CustomResponseDto<>(StatusCode.CREATED.getCode(),
                    userService.create(createUserDto),
                    "Успешно");
        }
        catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, illegalArgumentException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кошелек добавлен успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MetaMaskWallet.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Кошелек не был добавлен к юзеру"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кошелек либо пользователь не найдены")
    })
    @Operation(summary = "Эндпоинт принимает адрес кошелька и его пароль," +
            " если пароль от кошелька введенный пользователем совпадает, то он добавляется успешно")

    @PutMapping("/addMetaMaskWallet")
    public CustomResponseDto<MetaMaskWallet> addMetaMaskWallet(@RequestParam String walletAddress,
                                                               @RequestParam String walletPassword) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.addMetaMaskWallet(walletAddress, walletPassword),
                    "Успешно");
        }
        catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, illegalArgumentException.getMessage());
        }
        catch (NoMetamaskWalletException | UserNotFoundException notFoundExceptions) {
            log.error(notFoundExceptions.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, notFoundExceptions.getMessage());

        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Банковская карта добавленная успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные карты не верны"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Карта либо пользователь не найдены")
    })
    @Operation(summary = "Эндпоинт принимает номер карты, дату экспирации и cvv код" +
            " если данные совпадают с данными карты, то она добавляется успешно")
    @PutMapping("/addBankCard")
    public CustomResponseDto<String> addBankCard(@RequestBody AddBankCardDto cardToAdd) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.addBankcard(cardToAdd), "Успешно");
        }
        catch (UserNotFoundException | NoBankCardException notFoundExceptions) {
            log.error(notFoundExceptions.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, notFoundExceptions.getMessage());
        }
        catch (InvalidCardDataException invalidCardDataException) {
            log.error(invalidCardDataException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, invalidCardDataException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Фото профиля измененно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не удалось изменить фото профиля")
    })
    @Operation(summary = "Эндпоинт принимает мультипарт файл и отправляет его в хранилише firebase")
    @PutMapping("/changeProfilePhoto")
    public CustomResponseDto<String> changeProfilePhoto(@ModelAttribute MultipartFile multipartFile) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.changeProfilePhoto(multipartFile),
                    "Успешно");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Стастика выведена успешно ",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRateDto.class)))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователей нет")
    })
    @Operation(summary = "Эндпоинт принимает колличество дней за которые нужно выдать статискику, " +
            "например 7 - это 1 неделя, вытащит статистику за последнюю неделю и тд")
    @GetMapping("/rateOfUsersByDays")
    public CustomResponseDto<List<UserRateDto>> rateOfUsersByDays(@RequestParam Long days) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.getRateOfUsersByDays(days),
                    "Успешно");
        }
        catch (UserNotFoundException u) {
            log.error(u.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null, u.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Стастика выведена успешно ",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRateDto.class)))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоинт принимает колличество дней за которые нужно выдать статискику, " +
            "например 7 - это 1 неделя, и через токен найдет пользователя статистика которого нужна")
    @GetMapping("/rateOfUserByDays")
    public CustomResponseDto<UserRateDto> rateOfUserByDays(Long days) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.getRateOfUserByDays(days), "Успешно");
        }
        catch (UserNotFoundException userNotFoundException){
            log.error(userNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, userNotFoundException.getMessage());
        }


    }


        @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Код отправлен успешно ",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не валидный адрес почты")
    })
    @Operation(summary = "Эндпоинт принимает почту на которую нужно оправить код для сброса пароля")
    @PostMapping("/getResetCode")
    public CustomResponseDto<String> sendResetCode(@RequestParam String email) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.sendResetCode(email),
                    "Успешно");
        }
        catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, userNotFoundException.getMessage());
        }
        catch (NotValidEmailException notValidEmailException) {
            log.error(notValidEmailException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, notValidEmailException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Код подверджен успешно",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пароль не может быть изменен"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоинту нужно передать почту и код для смены пароля после этого," +
            " пользователь получит временную роль для смены пароля")
    @PutMapping("/confirmCode")
    public CustomResponseDto<String> confirmCode(@RequestParam String email, @RequestParam Integer code) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.confirmCode(email, code), "Успешно");
        }
        catch (PasswordCanNotBeChangedException passwordCanNotBeChangedException) {
            log.error(passwordCanNotBeChangedException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, passwordCanNotBeChangedException.getMessage());
        }
        catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, userNotFoundException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пароль изменен успешно ",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пароль не может быть изменен"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоинту нужно передать почту и новый пароль, если у пользователя есть нужная роль, " +
            "то пароль будет изменен")
    @PutMapping("/dropForgottenPassword")
    public CustomResponseDto<String> dropForgottenPassword(@RequestParam String email, @RequestParam String newPassword) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.dropForgottenPassword(email, newPassword),
                    "Успешно");
        }
        catch (PasswordCanNotBeChangedException passwordCanNotBeChangedException) {
            log.error(passwordCanNotBeChangedException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, passwordCanNotBeChangedException.getMessage());
        }
        catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, userNotFoundException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileDto.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоит принимает id пользователя по которому находит его и выдает данные " +
            "для профиля")

    @GetMapping("/getProfile/{id}")
    public CustomResponseDto<UserProfileDto> getProfile(@PathVariable Long id) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    userService.getProfile(id),
                    "Успешно");
        }
        catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, userNotFoundException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь подписалсся",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоит принимает id пользователя на которого хочет подписаться " +
            "авторизованный юзер")
    @PutMapping("/follow/{userId}")
    public CustomResponseDto<String> followUser(@PathVariable Long userId){
        try {
             return new CustomResponseDto<>(StatusCode.OK.getCode(), userService.followUser(userId),
                     "Успешно");
        }
        catch (UserNotFoundException userNotFoundException){
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null,
                    userNotFoundException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоит принимает id пользователя от которого хочет отписаться " +
            "авторизованный юзер")
    @PutMapping("/unfollow/{userId}")
    public CustomResponseDto<String> unfollowUser(@PathVariable Long userId) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(), userService.unfollowUser(userId),
                    "Успешно");
        }
        catch (UserNotFoundException userNotFoundException) {
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null,
                    userNotFoundException.getMessage());
        }
    }
}
