package my.code.nftmarketplacepublic.mapper;

import com.jdbc.nftmarketplace2.dtos.CurrencyDto;
import com.jdbc.nftmarketplace2.entities.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    Currency toEntity(CurrencyDto currencyDto);
}
