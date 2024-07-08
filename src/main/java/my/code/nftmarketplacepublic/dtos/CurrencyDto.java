package my.code.nftmarketplacepublic.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.jdbc.nftmarketplace2.enums.CurrencyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "Currency")
public class CurrencyDto {

    @JacksonXmlProperty(localName = "ISOCode", isAttribute = true)
    @Enumerated(EnumType.STRING)
    private CurrencyType isoCode;

    @JacksonXmlProperty(localName = "Nominal")
    private Integer nominal;

    @JacksonXmlProperty(localName = "Value")
    private BigDecimal value;

    private Timestamp refreshDate;

    public void setValue(String value) {
        value = value.replace(',', '.');
        this.value = new BigDecimal(value);
    }
}