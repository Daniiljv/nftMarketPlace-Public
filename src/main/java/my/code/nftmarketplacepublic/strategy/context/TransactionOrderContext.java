package my.code.nftmarketplacepublic.strategy.context;

import com.jdbc.nftmarketplace2.entities.TransactionOrders;
import com.jdbc.nftmarketplace2.entities.User;
import com.jdbc.nftmarketplace2.enums.MeansOfPayment;
import com.jdbc.nftmarketplace2.enums.OrderStatus;
import com.jdbc.nftmarketplace2.exceptions.UserNotFoundException;
import com.jdbc.nftmarketplace2.repositories.*;
import com.jdbc.nftmarketplace2.services.CurrencyService;
import com.jdbc.nftmarketplace2.strategy.TransactionStrategy;
import com.jdbc.nftmarketplace2.strategy.impl.BankCardTransaction;
import com.jdbc.nftmarketplace2.strategy.impl.MetaMaskTransaction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
@Slf4j
@Component
@RequiredArgsConstructor
@Setter
public class TransactionOrderContext {

    private TransactionStrategy transactionStrategy;

    private final NftRepo nftRepo;
    private final UserRepo userRepo;
    private final BankCardRepo bankCardRepo;
    private final MetaMaskRepo metaMaskRepo;
    private final TransactionRepo transactionRepo;
    private final TransactionOrderRepo transactionOrderRepo;

    private final CurrencyService currencyService;


    public String createTransactionOrder(Long nftId, MeansOfPayment meansOfPayment) {

        TransactionOrders transactionOrder = null;
        Long successfulTransactionId;

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String buyerUsername = authentication.getName();

            User orderOwner = userRepo.findByUsername(buyerUsername)
                    .orElseThrow(() -> new UserNotFoundException
                            ("Пользователь с именем " + buyerUsername + " не найден"));

            transactionOrder = new TransactionOrders(null,
                    orderOwner.getId(),
                    null,
                    meansOfPayment,
                    OrderStatus.CREATED,
                    "Создан",
                    new Timestamp(System.currentTimeMillis()),
                    null);

            transactionOrder = transactionOrderRepo.save(transactionOrder);

            switch (meansOfPayment) {

                case BANK_CARD -> {
                    transactionStrategy = new BankCardTransaction(nftRepo,
                            userRepo,
                            bankCardRepo,
                            transactionRepo,
                            currencyService);

                    successfulTransactionId = transactionStrategy.buyNft(nftId, buyerUsername);

                }

                case META_MASK_WALLET -> {
                    transactionStrategy = new MetaMaskTransaction(nftRepo,
                            userRepo,
                            metaMaskRepo,
                            transactionRepo);

                    successfulTransactionId = transactionStrategy.buyNft(nftId, buyerUsername);

                }

                default -> throw new IllegalArgumentException("Способ оплаты - %s пока не поддерживается!"
                        .formatted(meansOfPayment));
            }


            transactionOrder.setTransactionId(successfulTransactionId);
            transactionOrder.setOrderStatus(OrderStatus.SUCCESSFUL);
            transactionOrder.setDescription("Успешно");
            transactionOrder.setProcessedAt(new Timestamp(System.currentTimeMillis()));

            transactionOrderRepo.save(transactionOrder);

            return "Нфт была успешна купленна!";

        } catch (Exception e) {

            log.error(e.getMessage());

            if (transactionOrder != null) {

                transactionOrder.setOrderStatus(OrderStatus.FAILED);
                transactionOrder.setDescription(e.getMessage());

                transactionOrderRepo.save(transactionOrder);
            }
            throw new RuntimeException(e.getMessage());
        }
    }


}


