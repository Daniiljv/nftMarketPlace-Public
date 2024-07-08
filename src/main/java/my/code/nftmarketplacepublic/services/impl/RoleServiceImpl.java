package my.code.nftmarketplacepublic.services.impl;


import com.jdbc.nftmarketplace2.dtos.RoleDto;
import com.jdbc.nftmarketplace2.entities.Role;
import com.jdbc.nftmarketplace2.mapper.RoleMapper;
import com.jdbc.nftmarketplace2.repositories.RoleRepo;
import com.jdbc.nftmarketplace2.services.RoleService;
import com.jdbc.nftmarketplace2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    private final RoleMapper roleMapper;

    private final UserService userService;

    @Override
    public ResponseEntity<Long> save(RoleDto roleDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userService.findByUsername(authentication.getName());

        Role savedRole = roleRepo.save(roleMapper.toEntity(roleDto));
        return new ResponseEntity<>(savedRole.getId(), HttpStatus.CREATED);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleMapper.toDtoList(roleRepo.findAll());
    }

    @Override
    public RoleDto findById(Long id) {
        Role role = roleRepo.findById(id).orElse(null);
        if (role == null) {
            return null;
        }
        return roleMapper.toDto(role);
    }

    @Override
    public ResponseEntity<String> update(Long id, Role role) {
        Optional<Role> optionalRole = roleRepo.findById(id);
        if (optionalRole.isPresent()) {
            role.setId(id);
            roleRepo.save(role);
            return new ResponseEntity<>("Роль успешно обновлена", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Роль не найдена", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        Optional<Role> optionalRole = roleRepo.findById(id);
        if (optionalRole.isPresent()) {
            roleRepo.deleteById(id);
            return new ResponseEntity<>("Роль успешно удалена", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Роль не найдена", HttpStatus.NOT_FOUND);
        }
    }
}