package my.code.nftmarketplacepublic.controllers;

import com.jdbc.nftmarketplace2.dtos.CustomResponseDto;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.CollectionNotFoundException;
import com.jdbc.nftmarketplace2.exceptions.InsufficientAccessException;
import com.jdbc.nftmarketplace2.exceptions.NftNotFoundException;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.services.NftCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/nftCollections")
@RequiredArgsConstructor
public class NftCollectionController {

    private final NftCollectionService nftCollectionService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Коллекция создана успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден")
    })
    @Operation(summary = "Эндпоинт принимает название новой коллекции остальное берет из авторизации")
    @PostMapping
    public CustomResponseDto<String> create(@RequestParam String name) {
        try {
            return new CustomResponseDto<>(StatusCode.CREATED.getCode(), nftCollectionService.create(name),
                    "Успешно");
        }
        catch (UserNotFoundException userNotFoundException) {
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null,
                    userNotFoundException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Нфт добавленна успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не достаточно прав доступа"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, коллекция или нфт не найдены")
    })
    @Operation(summary = "Эндпоинт принимает название коллекции куда будет добаленна нфт и id нфт")
    @PutMapping("/addNft")
    public CustomResponseDto<String> addNft(@RequestParam String collectionName, @RequestParam Long nftId) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(), nftCollectionService.addNft(collectionName, nftId),
                    "Успешно");
        }
        catch (UserNotFoundException | NftNotFoundException | CollectionNotFoundException notFound) {
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null, notFound.getMessage());
        }
        catch (IllegalArgumentException | InsufficientAccessException badRequest) {
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null, badRequest.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Нфт удалена из коллекции успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не достаточно прав доступа"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, коллекция или нфт не найдены")
    })
    @Operation(summary = "Эндпоинт принимает название коллекции от куда будет удалена нфт и id нфт")
    @PutMapping("/removeNft")
    public CustomResponseDto<String> removeNft(@RequestParam String collectionName, @RequestParam Long nftId) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(), nftCollectionService.removeNft(collectionName, nftId),
                    "Успешно");
        }
        catch (UserNotFoundException | NftNotFoundException | CollectionNotFoundException notFound) {
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null, notFound.getMessage());
        }
        catch (InsufficientAccessException insufficientAccessException) {
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null,
                    insufficientAccessException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Коллекция удалена успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не достаточно прав доступа"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь или коллекция не найдены")
    })
    @Operation(summary = "Эндпоинт принимает название коллекции которая будет удалена")
    @DeleteMapping
    public CustomResponseDto<String> delete(@RequestParam String name) {
        try {
return new CustomResponseDto<>(StatusCode.OK.getCode(), nftCollectionService.delete(name),
        "Успешно");
        }
        catch (UserNotFoundException | CollectionNotFoundException notFound) {
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null,
                    notFound.getMessage());
        }
        catch (InsufficientAccessException insufficientAccessException) {
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null,
                    insufficientAccessException.getMessage());
        }
    }
}
