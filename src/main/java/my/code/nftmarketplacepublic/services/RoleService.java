package my.code.nftmarketplacepublic.services;

import com.jdbc.nftmarketplace2.dtos.RoleDto;
import com.jdbc.nftmarketplace2.entities.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface RoleService {
    ResponseEntity<Long> save(RoleDto role);

    List<RoleDto> getAllRoles();

    RoleDto findById(Long id);

    ResponseEntity<String> update(Long id, Role role);

    ResponseEntity<String> delete(Long id);
}