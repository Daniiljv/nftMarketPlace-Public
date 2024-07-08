package my.code.nftmarketplacepublic.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CryptoCurrencyType {
    ETH("Эфириум"),
    BTC("Биткоин");

    String name;
}
