package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_generator")
    @SequenceGenerator(name = "role_seq_generator", sequenceName = "role_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Название роли не указанно")
    private String name;
}