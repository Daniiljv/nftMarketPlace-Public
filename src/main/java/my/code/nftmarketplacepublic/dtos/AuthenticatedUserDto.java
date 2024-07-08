package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDto {
    private Long id;

    private String username;

    private Map<String, String> tokens;
}
