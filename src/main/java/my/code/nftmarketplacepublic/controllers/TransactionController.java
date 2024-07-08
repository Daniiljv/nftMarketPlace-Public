package my.code.nftmarketplacepublic.controllers;

import com.jdbc.nftmarketplace2.dtos.CustomResponseDto;
import com.jdbc.nftmarketplace2.dtos.TransactionOrderDto;
import com.jdbc.nftmarketplace2.enums.MeansOfPayment;
import com.jdbc.nftmarketplace2.enums.StatusCode;
import com.jdbc.nftmarketplace2.exceptions.*;
import com.jdbc.nftmarketplace2.services.TransactionOrderService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionOrderService transactionOrderService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тразакция прошла успешно ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Одна из сотавляющих транзакции не найдена"),
            @ApiResponse(
                    responseCode = "402",
                    description = "У покупателя не достаточно средств"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Способ оплаты не доступен"
            )
    })
    @Operation(summary = "Эндпоит принимает id nft и способ оплаты META_MASK_WALLET либо BANK_CARD," +
            " дату создания указывать не нужно." +
            " Создает транзакцию покупки через метамаск, проверяет есть ли он у участников транзакции")

    @PostMapping
    public CustomResponseDto<String> buyNft(@RequestParam Long nftId,
                                            @RequestParam MeansOfPayment meansOfPayment) {
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    transactionOrderService.createTransactionOrder(nftId, meansOfPayment),
                    "Успешно");
        }
        catch (InsufficientFundsException insufficientFundsException) {
            log.error(insufficientFundsException.getMessage());
            return new CustomResponseDto<>(StatusCode.PAYMENT_REQUIRED.getCode(),
                    null, insufficientFundsException.getMessage());
        }
        catch (UserNotFoundException | NoMetamaskWalletException |
               NoBankCardException | NftIsNotSellingException notFoundException) {
            log.error(notFoundException.getMessage());
            return new CustomResponseDto<>(StatusCode.NOT_FOUND.getCode(),
                    null, notFoundException.getMessage());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            log.error(illegalArgumentException.getMessage());
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(),
                    null, illegalArgumentException.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "История транзакий выведена успешно",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Что то пошло не так")
    })
    @Operation(summary = "Эндпоит по токену выдает историю транзакций пользователя")

    @GetMapping("/userTransactions")
    public CustomResponseDto<List<TransactionOrderDto>> getUserOrders(){
        try {
            return new CustomResponseDto<>(StatusCode.OK.getCode(),
                    transactionOrderService.getUserOrders(), "Успешно");
        }
        catch (Exception e){
            return new CustomResponseDto<>(StatusCode.BAD_REQUEST.getCode(), null,
                    e.getMessage());
        }
    }
}