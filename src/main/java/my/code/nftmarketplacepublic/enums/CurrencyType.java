package my.code.nftmarketplacepublic.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CurrencyType {
    USD("доллар"),
    EUR("евро"),
    KZT("тенге"),
    RUB("рубль"),
    KGS("сом");

    String description;
}
