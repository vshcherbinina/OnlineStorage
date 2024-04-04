package onlinestore.orderservice.service;

import onlinestore.orderservice.dto.OrderDetailDto;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.dto.StatusDto;
import onlinestore.orderservice.event.OrderProcessor;
import onlinestore.orderservice.exception.IncorrectInputException;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.entity.OrderStatusHistoryEntity;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final RepositoryUtil repositoryUtil;

    private final OrderProcessor orderProcessor;

    @Autowired
    public OrderServiceImpl(RepositoryUtil repositoryUtil,
                            OrderProcessor orderProcessor) {
        this.repositoryUtil = repositoryUtil;
        this.orderProcessor = orderProcessor;
    }
    
    private boolean isMoreThanZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.valueOf(0)) > 0;
    }

    @Override
    public void checkDataAndCorrectAmount(OrderDto orderDto) {
        if (orderDto.getUserName() == null || orderDto.getUserName().isBlank()){
            throw new IncorrectInputException("'userName' is required to identify the payer in the system");
        }
        if (orderDto.getDetails() == null || orderDto.getDetails().isEmpty()){
            throw new IncorrectInputException("indicate at least one product detail");
        }
        if (orderDto.getDestinationAddress() == null || orderDto.getDestinationAddress().isBlank()){
            throw new IncorrectInputException("'destinationAddress' is required");
        }
        BigDecimal amount = BigDecimal.valueOf(0);
        for (OrderDetailDto detail: orderDto.getDetails()) {
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(0D) <= 0) {
                throw new IncorrectInputException("indicate the quantity of the product for the article '" + detail.getProductArticle() + "'");
            }
            if (!isMoreThanZero(detail.getAmount())) {
                if (!isMoreThanZero(detail.getPrice())) {
                    throw new IncorrectInputException("for a product with an article '" + detail.getProductArticle()+ "' the price or amount must be indicated");
                }
                detail.setAmount(BigDecimal.valueOf(detail.getQuantity() * detail.getPrice().doubleValue()));
            }
            amount = amount.add(detail.getAmount());
        }
        if (!isMoreThanZero(orderDto.getAmount())) {
            orderDto.setAmount(amount);
        } else {
            if (orderDto.getAmount().compareTo(amount) != 0) {
                throw new IncorrectInputException("the order amount is not equal to the total for the parts: amount by details = " + amount + ", but actual = " + orderDto.getAmount());
            }
        }
    }


    @Override
    @Transactional
    public OrderDto addOrder(OrderDto input) {
        OrderEntity order = input.toOrder();
        repositoryUtil.saveOrder(order);
        orderProcessor.process(order);
        OrderDto orderDto = OrderDto.fromOrder(order);
        repositoryUtil.loadHistory(orderDto);
        return orderDto;
    }

    @Override
    public void updateOrderStatus(Long orderId, StatusDto statusDto) {
        repositoryUtil.updateOrderStatus(
                orderId, statusDto.getStatus(),
                statusDto.getDateModified(), "");
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderDto> orderDtoList = new ArrayList<>();
        repositoryUtil.getAllOrders().forEach(order -> {
            repositoryUtil.loadOrderDetails(order);
            OrderDto orderDto = OrderDto.fromOrder(order);
            repositoryUtil.loadHistory(orderDto);
            orderDtoList.add(orderDto);
        });
        return orderDtoList;
    }

    @Override
    public OrderDto getOrderById(Long id) {
        OrderEntity order = repositoryUtil.getOrderByIdWithDetails(id);
        OrderDto orderDto = OrderDto.fromOrder(order);
        repositoryUtil.loadHistory(orderDto);
        return orderDto;
    }
}
