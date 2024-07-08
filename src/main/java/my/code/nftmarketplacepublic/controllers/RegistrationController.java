package my.code.nftmarketplacepublic.controllers;

import com.jdbc.nftmarketplace2.dtos.CreateUserDto;
import com.jdbc.nftmarketplace2.dtos.CustomResponseDto;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.services.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

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
    @Operation(summary = "Эндпоинт принимает CreateUserDto и создает заявку регистрации")

    @PostMapping
    public CustomResponseDto<String> register(@RequestBody CreateUserDto createUserDto) {
        try {
            return new CustomResponseDto<>(StatusCode.CREATED.getCode(),
                    registrationService.register(createUserDto),
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
                    description = "Пользователь подтвержден успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserDto.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не пытался зарегистрироваться"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный код подтверждения")
    })
    @Operation(summary = "Эндпоинт подтвержает код пользователя и после регистрирует его в базе")

    @PutMapping("/confirm")
    public CustomResponseDto<String> confirmRegistration(String email, Integer code) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    registrationService.confirmRegistration(email, code), "Успешно");
        }
        catch (UserNotFoundException userNotFoundException) {
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null,
                    userNotFoundException.getMessage());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null,
                    illegalArgumentException.getMessage());
        }
    }
}
