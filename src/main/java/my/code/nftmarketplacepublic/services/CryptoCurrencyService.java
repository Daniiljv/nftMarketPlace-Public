package my.code.nftmarketplacepublic.services;

import org.springframework.stereotype.Service;


@Service
public interface CryptoCurrencyService {
    void refreshPrice();
}
