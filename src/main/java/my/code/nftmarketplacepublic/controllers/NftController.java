package my.code.nftmarketplacepublic.controllers;


import com.jdbc.nftmarketplace2.dtos.*;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.InsufficientAccessException;
import com.jdbc.nftmarketplace2.exceptions.NftNotFoundException;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.services.NftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nfts")
public class NftController {

    private final NftService nftService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Nft созданна успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateNftDto.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nft не была добавленна в базу")
    })
    @Operation(summary = "Эндпоит принимает такую же модель которую и возвращает, создает Nft. " +
            "Название должно быть уникальным и описание от 10 до 5000 символов. " +
            " Создатель, дата создания и другие генерируются бэком")

    @PostMapping
    public CustomResponseDto<SuccessfullyCreatedNftDto> create(@ModelAttribute CreateNftDto createNftDto) {
        try {
            return new CustomResponseDto<>(StatusCode.CREATED.getCode(),
                    nftService.create(createNftDto),
                    "Успешно");
        }
        catch (RuntimeException runtimeException) {
            log.error(runtimeException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, runtimeException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nft поступила в продажу успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nft не была выставлена продажу"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nft либо владелец не найдены")
    })
    @Operation(summary = "Эндпоит принимает id Nft которую хотят продать," +
            " если пользователь не владеет этой Nft, то ее продажа не будет доступна")
    @PutMapping("/pushNftToMarket")
    public CustomResponseDto<String> pushNftToMarket(@RequestParam Long nftId) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    nftService.pushNftToMarket(nftId), "Успешно");
        }
        catch (NftNotFoundException | UserNotFoundException notFoundException){
            log.error(notFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, notFoundException.getMessage());
        }
        catch (InsufficientAccessException insufficientAccessException){
            log.error(insufficientAccessException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, insufficientAccessException.getMessage());
        }

    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nft поступила в продажу успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nft не была снята с продажи"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nft либо владелец не найдены")
    })
    @Operation(summary = "Эндпоит принимает id Nft продажу которой хотят отменить," +
            " если пользователь не владеет этой Nft, то отмена ее продажи не будет доступна")
    @PutMapping("/cancelSelling")
    public CustomResponseDto<String> cancelSelling(@RequestParam Long nftId){
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    nftService.cancelSelling(nftId), "Успешно");
        }
        catch (NftNotFoundException | UserNotFoundException notFoundException){
            log.error(notFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, notFoundException.getMessage());
        }
        catch (InsufficientAccessException insufficientAccessException){
            log.error(insufficientAccessException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, insufficientAccessException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Нфт удалена успешно ",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NftForSalesDto.class)))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нфт не найдена"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь не может удалить данную нфт")
    })
    @Operation(summary = "Эндпоинт удаляет нфт по id")
    @DeleteMapping("/{id}")
    public CustomResponseDto<String> delete(@PathVariable Long id){
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    nftService.delete(id), "Успешно");
        }
        catch (NftNotFoundException nftNotFoundException){
            log.error(nftNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(), null,
                    nftNotFoundException.getMessage());
        }
        catch (InsufficientAccessException insufficientAccessException){
            log.error(insufficientAccessException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null,
                    insufficientAccessException.getMessage());
        }
    }
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Есть нфт для продажи ",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NftForSalesDto.class)))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нфт для продажи нет")
    })
    @Operation(summary = "Эндпоинт выдает все нфт для продажи")
    @GetMapping("/forSale")
    public CustomResponseDto<List<NftForSalesDto>> nftsForSale() {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    nftService.getNftsForSale(), "Успешно");
        }
        catch (NftNotFoundException nftNotFoundException) {
            log.error(nftNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, nftNotFoundException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nft найдена ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ViewNftDto.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nft не была найдена")
    })
    @Operation(summary = "Эндпоит принимает id нфт по которому находит ее в базе и выдает " +
            "информацию о ней")

    @GetMapping("/viewNftById/{id}")
    public CustomResponseDto<ViewNftDto> viewNftById(@PathVariable Long id) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    nftService.viewNftById(id), "Успешно");
        }
        catch (NftNotFoundException nftNotFoundException) {
            log.error(nftNotFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, nftNotFoundException.getMessage());
        }
    }
}