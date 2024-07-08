package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.entities.CryptoCurrency;
import com.jdbc.nftmarketplace2.enums.CryptoCurrencyType;
import com.jdbc.nftmarketplace2.repositories.CryptoCurrencyRepo;
import com.jdbc.nftmarketplace2.services.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

    private final RestTemplate restTemplate;

    private final CryptoCurrencyRepo cryptoCurrencyRepo;


    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=ETHUSDT";

    @Override
    public void refreshPrice() {
        try {
            String response = restTemplate.getForObject(BINANCE_API_URL, String.class);
            BigDecimal ethereumPrice = extractPriceFromResponse(Objects.requireNonNull(response));

            CryptoCurrency cryptoCurrency = cryptoCurrencyRepo
                    .findByCryptoCurrencyType(CryptoCurrencyType.ETH);

            if(cryptoCurrency == null){
                cryptoCurrencyRepo
                        .save(new CryptoCurrency(null, CryptoCurrencyType.ETH,
                                ethereumPrice, new Timestamp(System.currentTimeMillis())));
            }
            else {
                cryptoCurrency.setPrice(ethereumPrice);
                cryptoCurrency.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
                cryptoCurrencyRepo.save(cryptoCurrency);
            }

        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private BigDecimal extractPriceFromResponse(String response) {
        int priceIndex = response.indexOf("price\":\"") + 8;
        int endIndex = response.indexOf("\"", priceIndex);
        String priceString = response.substring(priceIndex, endIndex);
        return BigDecimal.valueOf(Double.parseDouble(priceString));
    }
}
