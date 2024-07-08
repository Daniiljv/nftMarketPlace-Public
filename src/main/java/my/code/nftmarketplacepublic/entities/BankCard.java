package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_card_seq_generator")
    @SequenceGenerator(name = "bank_card_seq_generator", sequenceName = "bank_card_seq", allocationSize = 1)
    private Long id;

    @Size(min = 16, max = 16, message = "Номер карты должен состоять из 16 цифр" )
    @NotNull(message = "Номер карты не должен быть пустым")
    private String cardNumber;

    @NotNull(message = "Время экспирации карты не должено быть пустым")
    private String cardExpiryDate;

    @Size(min = 3, max = 3, message = "Код должен состоять из 3 цифр")
    @NotNull(message = "Код карты не должен быть пустым")
    private String cvv;

    @NotNull(message = "Баланс карты не должен быть пустым")
    private BigDecimal balance;
}
