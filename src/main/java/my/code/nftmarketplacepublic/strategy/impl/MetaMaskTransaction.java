package my.code.nftmarketplacepublic.strategy.impl;

import com.jdbc.nftmarketplace2.entities.*;
import com.jdbc.nftmarketplace2.exceptions.*;
import com.jdbc.nftmarketplace2.repositories.*;
import com.jdbc.nftmarketplace2.strategy.TransactionStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Slf4j
@Component
@RequiredArgsConstructor
public class MetaMaskTransaction implements TransactionStrategy {

    private final NftRepo nftRepo;
    private final UserRepo userRepo;
    private final MetaMaskRepo metaMaskRepo;
    private final TransactionRepo transactionRepo;
    @Transactional
    public Long buyNft(Long nftId, String buyerUsername) {

        User buyer = userRepo.findByUsername(buyerUsername)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем " + buyerUsername + " не найден"));

        Nft nft = nftRepo.findById(nftId)
                .orElseThrow(() -> new NullPointerException("Нфт с id " + nftId + " не найдена"));

        User seller = userRepo.findById(nft.getOwnerId())
                .orElseThrow(() -> new NullPointerException("Пользователь с id " + nft.getOwnerId() + " не найден"));

        if (buyer.getId().equals(seller.getId())) {
            throw new SameUserTransactionException("Пользователь не может купить совю собственную нфт!");
        }

        if (!nft.isOnSale()) {
            throw new NftIsNotSellingException("Нфт с id " + nftId + " не продается!");
        }

        if (seller.getMetaMaskWallet() == null && buyer.getMetaMaskWallet() == null) {
            throw new NoMetamaskWalletException("У кого то из участников транзакции отсутствует MetaMask!");
        }

        if(buyer.getMetaMaskWallet().equals(seller.getMetaMaskWallet())){
            throw new SameMetaMaskTransactionException("Транзакция не может быть проведена на один и тот же кошелек");
        }

        try {
            transactionByMetamask(buyer.getMetaMaskWallet(), seller.getMetaMaskWallet(), nft.getDollarPrice());
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

    public void transactionByMetamask(MetaMaskWallet walletFrom, MetaMaskWallet walletTo, BigDecimal transactionValue) {

        if (walletFrom.getBalance().compareTo(transactionValue) < 0) {
            throw new InsufficientFundsException("У покупателя не достаточно среств для транзакции!" +
                    "\nПополните свой баланс на : " + transactionValue.subtract(walletFrom.getBalance()) + " $$$");
        }

        walletFrom.setBalance(walletFrom.getBalance().subtract(transactionValue));
        walletTo.setBalance(walletTo.getBalance().add(transactionValue));

        metaMaskRepo.save(walletFrom);
        metaMaskRepo.save(walletTo);

    }

}
