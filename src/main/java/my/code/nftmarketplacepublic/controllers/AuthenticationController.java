package my.code.nftmarketplacepublic.controllers;

import com.jdbc.nftmarketplace2.dtos.AuthenticatedUserDto;
import com.jdbc.nftmarketplace2.dtos.CustomResponseDto;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/getGoogleToken")
    public RedirectView getGoogleToken() {
        return new RedirectView("/oauth2/authorization/google");
    }
    @GetMapping("/google")
    public CustomResponseDto<AuthenticatedUserDto> googleAuth(OAuth2AuthenticationToken googleToken) {
        try {

            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    authenticationService.googleAuth(googleToken), "Успешно");
        }
        catch (Exception e){
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь аутентифицирован ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticatedUserDto.class))}),

            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),

            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный пароль",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = String.class))})
    })
    @Operation(summary = "Эндпоинт имя пользователя и пароль а после аутентифицирует")

    @GetMapping
    public CustomResponseDto<AuthenticatedUserDto> authentication(@RequestParam String username,
                                                                  @RequestParam String password) {
        try {

            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    authenticationService.attemptAuthentication(username, password),
                    "Успешно");

        }
        catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null, userNotFoundException.getMessage());

        }
        catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null, illegalArgumentException.getMessage());
        }
    }
}