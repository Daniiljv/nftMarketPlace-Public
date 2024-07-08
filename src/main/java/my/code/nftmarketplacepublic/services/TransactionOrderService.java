package my.code.nftmarketplacepublic.services;


import com.jdbc.nftmarketplace2.dtos.TransactionOrderDto;
import com.jdbc.nftmarketplace2.enums.MeansOfPayment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionOrderService {
    String createTransactionOrder(Long nftId, MeansOfPayment meansOfPayment);

    List<TransactionOrderDto> getUserOrders();

}
