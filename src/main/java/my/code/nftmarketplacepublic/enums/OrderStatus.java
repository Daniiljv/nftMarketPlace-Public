package my.code.nftmarketplacepublic.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
    CREATED("Ордер создан"),
    SUCCESSFUL("Транзакция прошла успешно"),
    FAILED("Транзакция была отменена");

    String description;
}
