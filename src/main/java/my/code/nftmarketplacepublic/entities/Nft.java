package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nft {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nft_seq_generator")
    @SequenceGenerator(name = "nft_seq_generator", sequenceName = "nft_seq", allocationSize = 1)
    private Long id;

    @Size(min = 1, max = 20, message = "Название должно быть от 1 до 20 символов")
    @Column(unique = true)
    @NotNull(message = "Название нфт не должно быть пустым")
    private String name;

    @NotNull(message = "Обязательно укажите цену")
    private BigDecimal dollarPrice;

    @NotNull(message = "Название нфт не должно быть пустым")
    private BigDecimal ethereumPrice;

    @Column(unique = true)
    @NotNull(message = "Фото является обязательным")
    private String nftImage;

    @Size(min = 1, max = 5000, message = "Описание должно быть от 1 до 5000 символов")
    private String description;

    @NotNull(message = "Состояние продажи нфт обязательно")
    private boolean onSale;

    @NotNull(message = "Коллекционность нфт обязательна")
    private boolean collectible;

    @NotNull(message = "Создатель не может быть пустым")
    private Long creatorId;

    @NotNull(message = "Владелец не может быть пустым")
    private Long ownerId;

    @NotNull(message = "Дата создания является обязательным")
    private Timestamp createdAt;

    private Timestamp lastUpdatedAt;

    private Timestamp deletedAt;
}
