package my.code.nftmarketplacepublic.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum StatusCode {

    OK(200, "Oк"),
    CREATED(201, "Создан"),
    BAD_REQUEST(400, "Неправильный запрос"),
    NOT_FOUND(404, "Не найден"),
    BAD_CREDENTIALS(401, "Ошибка авторизации"),
    PAYMENT_REQUIRED(402, "Требуется оплата");


     long code;
     String message;

}

