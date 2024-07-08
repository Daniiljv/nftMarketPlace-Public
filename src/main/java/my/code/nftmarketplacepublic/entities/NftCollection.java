package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nft_collection_seq_generator")
    @SequenceGenerator(name = "nft_collection_seq_generator", sequenceName = "nft_collection_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Владелец коллекции не указан")
    private Long ownerId;

    @NotNull(message = "Название коллекции не указанно")
    private String collectionName;

    private List<Long> nftIds;

    @NotNull(message = "Дата создания не указана")
    private Timestamp createdAt;

    private Timestamp lastUpdatedAt;

    private Timestamp deletedAt;
}
