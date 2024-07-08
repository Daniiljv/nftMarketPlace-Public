package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.dtos.*;
import com.jdbc.nftmarketplace2.entities.Nft;
import com.jdbc.nftmarketplace2.entities.User;
import com.jdbc.nftmarketplace2.enums.CryptoCurrencyType;
import com.jdbc.nftmarketplace2.exceptions.InsufficientAccessException;
import com.jdbc.nftmarketplace2.exceptions.NftNotFoundException;
import com.jdbc.nftmarketplace2.mapper.NftMapper;
import com.jdbc.nftmarketplace2.repositories.CryptoCurrencyRepo;
import com.jdbc.nftmarketplace2.repositories.NftRepo;
import com.jdbc.nftmarketplace2.repositories.RoleRepo;
import com.jdbc.nftmarketplace2.repositories.UserRepo;
import com.jdbc.nftmarketplace2.services.FairBaseImageService;
import com.jdbc.nftmarketplace2.services.NftService;
import com.jdbc.nftmarketplace2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NftServiceImpl implements NftService {

    private final NftRepo nftRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final CryptoCurrencyRepo cryptoCurrencyRepo;

    private final NftMapper mapper;

    private final UserService userService;
    private final FairBaseImageService fairBaseImageService;

    @Override
    public SuccessfullyCreatedNftDto create(CreateNftDto createNftDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User creator = userService.findByUsername(authentication.getName());

            createNftDto.setCreatorId(creator.getId());
            createNftDto.setOwnerId(creator.getId());
            createNftDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            Nft nft = Nft.builder()
                    .name(createNftDto.getName())
                    .dollarPrice(createNftDto.getDollarPrice())
                    .ethereumPrice(createNftDto.getDollarPrice().divide(
                            cryptoCurrencyRepo.findByCryptoCurrencyType(CryptoCurrencyType.ETH).getPrice(),
                            8,
                            RoundingMode.HALF_UP))
                    .nftImage(fairBaseImageService.saveNftImage(createNftDto.getNftImage()))
                    .description(createNftDto.getDescription())
                    .onSale(false)
                    .collectible(false)
                    .creatorId(createNftDto.getCreatorId())
                    .ownerId(createNftDto.getOwnerId())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            createNftDto.setId(nftRepo.save(nft).getId());

            userRepo.save(creator);

            return SuccessfullyCreatedNftDto.builder()
                    .id(nft.getId())
                    .name(nft.getName())
                    .dollarPrice(nft.getDollarPrice())
                    .ethereumPrice(nft.getEthereumPrice())
                    .nftImage(nft.getNftImage())
                    .description(nft.getDescription())
                    .creatorId(nft.getCreatorId())
                    .ownerId(nft.getOwnerId())
                    .createdAt(nft.getCreatedAt())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Nft findById(Long id) {
        return nftRepo.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new NftNotFoundException("Нфт с id " + id + " не найдена"));
    }

    @Override
    public NftDto update(NftDto nftDtoToUpdate) {
        try {
            nftRepo.findById(nftDtoToUpdate.getId())
                    .orElseThrow(() -> new NullPointerException("Нфт с id " + nftDtoToUpdate.getId() + " не найдена"));

            Nft nft = mapper.toEntity(nftDtoToUpdate);
            nftRepo.save(nft);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return nftDtoToUpdate;
    }

    @Override
    public String delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());

        Nft nft = findById(id);

        if (!user.getId().equals(nft.getOwnerId()) &&
                !user.getRoles().contains(roleRepo.findByName("ADMIN"))) {

            throw new InsufficientAccessException
                    ("Пользователь - %s не может удалить эту нфт!".formatted(authentication.getName()));

        }

        nft.setOnSale(false);
        nft.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        nftRepo.save(nft);

        return "Нфт с id " + id + " была успешно удалена";
    }

    @Override
    public String pushNftToMarket(Long nftId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Nft nft = findById(nftId);

        User owner = userService.findByUsername(authentication.getName());

        if (nft.getOwnerId().equals(owner.getId())) {
            nft.setOnSale(true);
            nftRepo.save(nft);
        } else {
            throw new InsufficientAccessException("Пользователь с id " + owner.getId() +
                    " не может продать нфт с id + " + nftId);
        }

        if (owner.getBankCard() == null && owner.getMetaMaskWallet() == null) {
            throw new InsufficientAccessException("Пожалуйста, добавьте средства оплаты," +
                    " перед тем как выставить свою нфт в продажу ");
        }

        return "Нфт с id " + nftId + " выставленна на продажу!";
    }

    @Override
    public String cancelSelling(Long nftId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Nft nft = findById(nftId);

        User user = userService.findByUsername(authentication.getName());

        if (nft.getOwnerId().equals(user.getId())) {
            nft.setOnSale(false);
            nftRepo.save(nft);
        } else {
            throw new InsufficientAccessException("Пользователь с id " + user.getId() +
                    " не может отменить продажу нфт с id + " + nftId);
        }
        return "Нфт с id " + nftId + " больше не продается!";
    }

    @Override
    public List<NftForSalesDto> getNftsForSale() {
        List<Nft> nfts = nftRepo.findAllByDeletedAtIsNullAndOnSaleIsTrue()
                .orElseThrow(() -> new NftNotFoundException("Список пуст"));

        if (nfts.isEmpty()) {
            throw new NftNotFoundException("Список пуст");
        }

        List<NftForSalesDto> nftsForSale = new ArrayList<>();

        for (Nft nft : nfts) {

            nft.setEthereumPrice(nft.getDollarPrice().divide(
                    cryptoCurrencyRepo.findByCryptoCurrencyType(CryptoCurrencyType.ETH).getPrice(),
                    8,
                    RoundingMode.HALF_UP));
            nft.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
            nftRepo.save(nft);

            NftForSalesDto nftForSale = NftForSalesDto.builder()
                    .id(nft.getId())
                    .nftImage(nft.getNftImage())
                    .ownerId(nft.getOwnerId())
                    .name(nft.getName())
                    .ownerUsername(userService.findById(nft.getOwnerId()).getUsername())
                    .dollarPrice(nft.getDollarPrice())
                    .ethereumPrice(nft.getEthereumPrice())
                    .build();

            nftsForSale.add(nftForSale);
        }

        return nftsForSale;
    }

    @Override
    public ViewNftDto viewNftById(Long id) {
        Nft nft = nftRepo.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new NftNotFoundException("Нфт с id " + id + " не найдена"));

        nft.setEthereumPrice(nft.getDollarPrice().divide(
                cryptoCurrencyRepo.findByCryptoCurrencyType(CryptoCurrencyType.ETH).getPrice(),
                8,
                RoundingMode.HALF_UP));
        nft.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
        nftRepo.save(nft);

        return ViewNftDto.builder()
                .id(nft.getId())
                .nftImage(nft.getNftImage())
                .name(nft.getName())
                .createdAt(nft.getCreatedAt())
                .creatorUsername(userService.findById(nft.getOwnerId()).getUsername())
                .ownerId(nft.getOwnerId())
                .description(nft.getDescription())
                .dollarPrice(nft.getDollarPrice())
                .ethereumPrice(nft.getEthereumPrice())
                .build();
    }

    @Override
    public void save(Nft nft) {
        nftRepo.save(nft);
    }
}
