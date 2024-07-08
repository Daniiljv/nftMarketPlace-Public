package my.code.nftmarketplacepublic.services;

import com.jdbc.nftmarketplace2.dtos.AuthenticatedUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public interface AuthenticationService {

    AuthenticatedUserDto attemptAuthentication(String username, String password);
    AuthenticatedUserDto successfulAuthentication(Authentication authentication);

    AuthenticatedUserDto googleAuth(OAuth2AuthenticationToken googleToken);


}
