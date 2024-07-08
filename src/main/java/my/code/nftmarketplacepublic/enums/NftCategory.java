package my.code.nftmarketplacepublic.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum NftCategory {
    ART("ART"),
    COLLECTIBLES("COLLECTIBLES"),
    MUSIC("MUSIC"),
    PHOTOGRAPHY("PHOTOGRAPHY"),
    VIDEO("VIDEO"),
    UTILITY("UTILITY"),
    SPORT("SPORT"),
    VIRTUAL_WORLDS("VIRTUAL_WORLDS");

    String description;
}
