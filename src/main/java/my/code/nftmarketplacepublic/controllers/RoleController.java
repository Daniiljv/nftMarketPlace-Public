package my.code.nftmarketplacepublic.controllers;

import com.jdbc.nftmarketplace2.dtos.RoleDto;
import com.jdbc.nftmarketplace2.entities.Role;
import com.jdbc.nftmarketplace2.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Роль успешно сохранен",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не удалось сохронить пользователя")
    })
    @Operation(summary = "Этот роут для создание ролей")
    @PostMapping
    public ResponseEntity<Long> save(@RequestBody RoleDto roleDto) {
        return roleService.save(roleDto);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все записи получены успешно",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нет ни одного пользователя")
    })
    @Operation(summary = "Этот роут возвращает весь список ролей")
    @GetMapping("/getAll")
    public ResponseEntity<List<RoleDto>> getAll() {
        List<RoleDto> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Роль по айди получены успешно",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Роль по этому айди не найдена")
    })
    @Operation(summary = "Этот роут возвращает роли по айди")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {
        RoleDto role = roleService.findById(id);
        if (role != null) {
            return new ResponseEntity<>(role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Роль успешно обновился",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не удалось обновить пользователя")
    })
    @Operation(summary = "Этот роут для обновление ролей")

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Role role) {
        return roleService.update(id, role);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Роль успешно удален",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не удалось удалить по айди")
    })
    @Operation(summary = "Этот роут для удаление ролей")

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return roleService.delete(id);
    }
}