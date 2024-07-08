package my.code.nftmarketplacepublic.services;

import com.jdbc.nftmarketplace2.enums.CurrencyType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface CurrencyService {
    void refreshCurrencyRates();
    BigDecimal getCurrencyVolume(CurrencyType isoCode);
}
