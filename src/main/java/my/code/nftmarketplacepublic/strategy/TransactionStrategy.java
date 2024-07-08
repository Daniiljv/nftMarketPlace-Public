package my.code.nftmarketplacepublic.strategy;

import org.springframework.stereotype.Component;

@Component
public interface TransactionStrategy {
    Long buyNft(Long nftId, String buyerUsername);
}
