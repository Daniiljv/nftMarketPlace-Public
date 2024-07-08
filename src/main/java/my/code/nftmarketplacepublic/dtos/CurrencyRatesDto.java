package my.code.nftmarketplacepublic.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JacksonXmlRootElement(localName = "CurrencyRates")
public class CurrencyRatesDto {

    Long id;

    LocalDateTime createDate;

    @JacksonXmlProperty(localName = "Name", isAttribute = true)
    String name;

    @JacksonXmlProperty(localName = "Date", isAttribute = true)
    LocalDate date;

    @JacksonXmlProperty(localName = "Currency")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<CurrencyDto> currencyModelList;

}
