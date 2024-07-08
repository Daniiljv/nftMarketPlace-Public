package my.code.nftmarketplacepublic.mapper;

import com.jdbc.nftmarketplace2.dtos.RoleDto;
import com.jdbc.nftmarketplace2.entities.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleDto roleDto);
    List<RoleDto> toDtoList(List<Role> role);
}
