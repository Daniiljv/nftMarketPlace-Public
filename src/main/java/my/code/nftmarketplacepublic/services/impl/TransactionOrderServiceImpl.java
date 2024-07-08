package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.dtos.TransactionOrderDto;
import com.jdbc.nftmarketplace2.entities.TransactionOrders;
import com.jdbc.nftmarketplace2.entities.User;
import com.jdbc.nftmarketplace2.enums.MeansOfPayment;
import com.jdbc.nftmarketplace2.repositories.TransactionOrderRepo;
import com.jdbc.nftmarketplace2.services.TransactionOrderService;
import com.jdbc.nftmarketplace2.services.UserService;
import com.jdbc.nftmarketplace2.strategy.context.TransactionOrderContext;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionOrderServiceImpl implements TransactionOrderService {

    private final TransactionOrderRepo transactionOrderRepo;

    private final UserService userService;

    private final TransactionOrderContext transactionOrderContext;
    @Override
    public String createTransactionOrder(Long nftId, MeansOfPayment meansOfPayment) {
       return transactionOrderContext.createTransactionOrder(nftId, meansOfPayment);
    }

    @Override
    public List<TransactionOrderDto> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());

        List<TransactionOrders> transactionOrders = transactionOrderRepo.findAllByOwnerId(user.getId());

        List<TransactionOrderDto> transactionOrderDtos = new ArrayList<>();

        transactionOrders.forEach(t -> {
            TransactionOrderDto orderDto = new TransactionOrderDto(
                    t.getId(),
                    t.getOwnerId(),
                    t.getMeansOfPayment(),
                    t.getOrderStatus(),
                    t.getDescription(),
                    t.getTransactionId(),
                    t.getCreatedAt()
            );
            transactionOrderDtos.add(orderDto);
        });

        return transactionOrderDtos;
    }
}
