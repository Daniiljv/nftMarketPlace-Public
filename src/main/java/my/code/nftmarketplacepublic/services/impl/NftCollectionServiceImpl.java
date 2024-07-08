package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.entities.Nft;
import com.jdbc.nftmarketplace2.entities.NftCollection;
import com.jdbc.nftmarketplace2.entities.User;
import com.jdbc.nftmarketplace2.exceptions.CollectionNotFoundException;
import com.jdbc.nftmarketplace2.exceptions.InsufficientAccessException;
import com.jdbc.nftmarketplace2.exceptions.NftNotFoundException;
import com.jdbc.nftmarketplace2.repositories.NftCollectionRepo;
import com.jdbc.nftmarketplace2.repositories.RoleRepo;
import com.jdbc.nftmarketplace2.services.NftCollectionService;
import com.jdbc.nftmarketplace2.services.NftService;
import com.jdbc.nftmarketplace2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class NftCollectionServiceImpl implements NftCollectionService {

    private final RoleRepo roleRepo;
    private final NftCollectionRepo nftCollectionRepo;

    private final NftService nftService;
    private final UserService userService;

    @Override
    public String create(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User creator = userService.findByUsername(authentication.getName());

        nftCollectionRepo.save(new NftCollection
                (null, creator.getId(),
                name, new ArrayList<>(),
                new Timestamp(System.currentTimeMillis()),
                null, null));

        return "Коллекция с именем - %s созданна успешно!".formatted(name);
    }

    @Override
    public NftCollection findByName(String name) {

        return nftCollectionRepo.findByCollectionNameAndDeletedAtIsNull(name)
                .orElseThrow(()-> new CollectionNotFoundException
                        ("Коллекция с именем - %s не найдена!".formatted(name)));
    }

    @Override
    public String addNft(String collectionName, Long nftId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());

        Nft nftToAdd = nftService.findById(nftId);

        NftCollection nftCollection = findByName(collectionName);

        if(nftToAdd.isCollectible()){
            throw new IllegalArgumentException("Коллекционная нфт не может быть добавленна в другую коллекцию!");
        }

        if(!nftToAdd.getOwnerId().equals(user.getId())){
            throw new InsufficientAccessException("Пользователь не может добавить чужую нфт в коллекцию!");
        }

        if(!user.getId().equals(nftCollection.getOwnerId())){
            throw new InsufficientAccessException("Пользователь не может добавлять нфт в чужие коллекции!");
        }

        nftCollection.getNftIds().add(nftToAdd.getId());
        nftCollection.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
        nftCollectionRepo.save(nftCollection);

        nftToAdd.setCollectible(true);
        nftService.save(nftToAdd);

        return "Нфт была успешно добавленна в коллекцию - %s".formatted(collectionName);
    }

    @Override
    public String removeNft(String collectionName, Long nftId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());

        Nft nftToRemove = nftService.findById(nftId);

        NftCollection nftCollection = findByName(collectionName);

        if(!nftToRemove.getOwnerId().equals(user.getId())){
            throw new InsufficientAccessException("Пользователь не может удалить чужую нфт из коллекции!");
        }

        if(!user.getId().equals(nftCollection.getOwnerId())){
            throw new InsufficientAccessException("Пользователь не может удалить нфт из чужой коллекции!");
        }

        if(!nftCollection.getNftIds().contains(nftId)){
            throw new NftNotFoundException("Нфт с id - %s не была найдена в коллекции!");
        }

        nftCollection.getNftIds().remove(nftToRemove.getId());
        nftCollection.setLastUpdatedAt(new Timestamp(System.currentTimeMillis()));
        nftCollectionRepo.save(nftCollection);

        nftToRemove.setCollectible(false);
        nftService.save(nftToRemove);

        return "Нфт была успешно удалена из коллекции - %s".formatted(collectionName);
    }

    @Override
    public String delete(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());

        NftCollection nftCollection = findByName(name);

        if(!user.getId().equals(nftCollection.getOwnerId()) ||
        !user.getRoles().contains(roleRepo.findByName("ADMIN"))){
            throw new InsufficientAccessException("Пользователь не может удалить чужую коллекцию!");
        }


        nftCollection.setDeletedAt(new Timestamp(System.currentTimeMillis()));
        nftCollectionRepo.save(nftCollection);


        return "Колекция - %s была успешно удалена! ".formatted(name);
    }
}
