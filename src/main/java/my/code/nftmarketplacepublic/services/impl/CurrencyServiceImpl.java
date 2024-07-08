package my.code.nftmarketplacepublic.services.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.jdbc.nftmarketplace2.dtos.CurrencyDto;
import com.jdbc.nftmarketplace2.dtos.CurrencyRatesDto;
import com.jdbc.nftmarketplace2.entities.Currency;
import com.jdbc.nftmarketplace2.enums.CurrencyType;
import com.jdbc.nftmarketplace2.mapper.CurrencyMapper;
import com.jdbc.nftmarketplace2.repositories.CurrencyRepo;
import com.jdbc.nftmarketplace2.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final RestTemplate restTemplate;
    private final CurrencyRepo currencyRepo;
    private final XmlMapper xmlMapper;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(RestTemplate restTemplate, CurrencyRepo currencyRepo, CurrencyMapper currencyMapper) {
        this.restTemplate = restTemplate;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new JavaTimeModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.currencyRepo = currencyRepo;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public void refreshCurrencyRates() {
        try {
            List<CurrencyDto> currencyDtos = getModelFromApi().getCurrencyModelList();

            currencyDtos
                    .stream()
                    .map(currencyMapper::toEntity)
                    .forEach(newCurrency -> {
                        newCurrency.setRefreshDate(new Timestamp(System.currentTimeMillis()));

                        Currency oldCurrency = currencyRepo.findByIsoCode(newCurrency.getIsoCode()).orElse(null);

                        if (oldCurrency != null) {
                            oldCurrency.setValue(newCurrency.getValue());
                            oldCurrency.setRefreshDate(newCurrency.getRefreshDate());
                            currencyRepo.save(oldCurrency);
                        } else {
                            currencyRepo.save(newCurrency);
                        }
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getCurrencyVolume(CurrencyType isoCode) {
        return Objects.requireNonNull(currencyRepo.findByIsoCode(isoCode).orElse(null)).getValue();
    }

    private CurrencyRatesDto getModelFromApi() throws Exception {
        String xml = restTemplate.getForObject("https://www.nbkr.kg/XML/daily.xml", String.class);
        return xmlMapper.readValue(xml, CurrencyRatesDto.class);
    }
}
