package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private Long id;

    private String username;

    private String email;

    private String password;
}
