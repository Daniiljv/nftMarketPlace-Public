package my.code.nftmarketplacepublic.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MeansOfPayment {

    META_MASK_WALLET("Оплата по MetaMask"),
    BANK_CARD("Оплата по Банковской карте"),
    TEST("Test");

    String description;
}
