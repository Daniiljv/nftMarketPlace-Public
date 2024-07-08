package my.code.nftmarketplacepublic.jobs;

import com.jdbc.nftmarketplace2.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class RefreshCurrenciesRatesJob {

    private final CurrencyService currencyService;
    @Scheduled(cron = "0 0 12 * * ?")
    public void refreshRates(){
        currencyService.refreshCurrencyRates();
    }
}
