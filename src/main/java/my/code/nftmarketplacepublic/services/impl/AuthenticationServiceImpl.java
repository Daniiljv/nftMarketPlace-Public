package my.code.nftmarketplacepublic.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jdbc.nftmarketplace2.dtos.AuthenticatedUserDto;
import com.jdbc.nftmarketplace2.dtos.CreateUserDto;
import com.jdbc.nftmarketplace2.entities.User;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.services.AuthenticationService;
import com.jdbc.nftmarketplace2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticatedUserDto attemptAuthentication(String username, String password) {

        var user = userService.findByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль!");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return successfulAuthentication(authenticationManager.authenticate(authenticationToken));

    }

    @Override
    public AuthenticatedUserDto successfulAuthentication(Authentication authentication) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User myUser = userService.findByUsername(user.getUsername());

        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();
        authenticatedUserDto.setId(myUser.getId());
        authenticatedUserDto.setUsername(myUser.getUsername());

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 24))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 30))
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        authenticatedUserDto.setTokens(tokens);

        return authenticatedUserDto;
    }

    @Override
    public AuthenticatedUserDto googleAuth(OAuth2AuthenticationToken googleToken) {
        OAuth2User oauth2User = googleToken.getPrincipal();

        String name = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");

        try {
            var user = userService.findByUsername(name);

            if (!passwordEncoder.matches(email, user.getPassword())) {
                throw new IllegalArgumentException("Неверный пароль!");
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(name, email);

            return successfulAuthentication(authenticationManager.authenticate(authenticationToken));

        } catch (UserNotFoundException userNotFoundException) {

            userService.create(new CreateUserDto(null, name, email,
                    passwordEncoder.encode(email)));

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(name, email);

            return successfulAuthentication(authenticationManager.authenticate(authenticationToken));
        }
    }


}
