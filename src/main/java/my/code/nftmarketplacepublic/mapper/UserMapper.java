package my.code.nftmarketplacepublic.mapper;


import com.jdbc.nftmarketplace2.dtos.UserDto;
import com.jdbc.nftmarketplace2.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
User toEntity(UserDto userDto);

}
