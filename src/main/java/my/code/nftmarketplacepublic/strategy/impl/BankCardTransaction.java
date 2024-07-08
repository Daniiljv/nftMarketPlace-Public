package my.code.nftmarketplacepublic.strategy.impl;

import com.jdbc.nftmarketplace2.entities.*;
import com.jdbc.nftmarketplace2.enums.CurrencyType;
import com.jdbc.nftmarketplace2.exceptions.*;
import com.jdbc.nftmarketplace2.repositories.*;
import com.jdbc.nftmarketplace2.services.CurrencyService;
import com.jdbc.nftmarketplace2.strategy.TransactionStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
@Slf4j
@Component
@RequiredArgsConstructor
public class BankCardTransaction implements TransactionStrategy {

    private final NftRepo nftRepo;
    private final UserRepo userRepo;
    private final BankCardRepo bankCardRepo;
    private final TransactionRepo transactionRepo;

    private final CurrencyService currencyService;

    @Transactional
    public Long buyNft(Long nftId, String buyerUsername) {

        User buyer = userRepo.findByUsername(buyerUsername)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем " + buyerUsername + " не найден"));

        Nft nft = nftRepo.findById(nftId)
                .orElseThrow(() -> new NullPointerException("Нфт с id " + nftId + " не найдена"));

        User seller = userRepo.findById(nft.getOwnerId())
                .orElseThrow(() -> new NullPointerException("Пользователь с id " + nft.getOwnerId() + " не найден"));

        if (buyer.getId().equals(seller.getId())) {
            throw new SameUserTransactionException("Пользователь не может купить собственную нфт!");
        }

        if (seller.getBankCard() == null && buyer.getBankCard() == null) {
            throw new NoBankCardException("У кого то из участников транзакции отстутствует банковская карта");
        }

        if(buyer.getBankCard().equals(seller.getBankCard())){
            throw new SameCardTransactionException("Транзакция не может быть проведена на одну и ту же карту");
        }

        if (!nft.isOnSale()) {
            throw new NftIsNotSellingException("Нфт с id " + nftId + " не продается!");
        }

        try {
            transactionByBankCard(buyer.getBankCard(), seller.getBankCard(), nft.getDollarPrice());
        }
        catch (InsufficientFundsException insufficientFundsException) {
            log.error(insufficientFundsException.getMessage());
            throw new InsufficientFundsException(insufficientFundsException.getMessage());
        }

        seller.setTransactionVolume(seller.getTransactionVolume().add(nft.getDollarPrice()));
        seller.setCountOfSoldNft(seller.getCountOfSoldNft() + 1);
        userRepo.save(seller);

        nft.setOwnerId(buyer.getId());
        nft.setOnSale(false);
        nftRepo.save(nft);

        return transactionRepo.save(new Transaction(null, seller.getId(), buyer.getId(),
                nft.getDollarPrice(), new Timestamp(System.currentTimeMillis()))).getId();
    }

    public void transactionByBankCard(BankCard cardFrom, BankCard cardTo, BigDecimal transactionValue) {
        BigDecimal dollarRate = currencyService.getCurrencyVolume(CurrencyType.USD);

        if (cardFrom
                .getBalance()
                .divide(dollarRate, RoundingMode.HALF_DOWN)
                .compareTo(transactionValue) < 0) {
            throw new InsufficientFundsException("У покупателя не достаточно средств для транзакции!" +
                    "\nПополните баланс на : " +
                    transactionValue.subtract(cardFrom.getBalance().multiply(dollarRate)) + " СОМ");
        }

        cardFrom.setBalance(cardFrom.getBalance().subtract(transactionValue.multiply(dollarRate)));
        cardTo.setBalance(cardTo.getBalance().add(transactionValue.multiply(dollarRate)));

        bankCardRepo.save(cardFrom);
        bankCardRepo.save(cardTo);

    }
}
