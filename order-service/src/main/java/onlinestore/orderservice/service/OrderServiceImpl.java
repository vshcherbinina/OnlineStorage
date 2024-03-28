package onlinestore.orderservice.service;

import onlinestore.orderservice.dto.OrderDetailDto;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.dto.StatusDto;
import onlinestore.orderservice.event.OrderProcessor;
import onlinestore.orderservice.exception.IncorrectInputException;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
    private boolean isMoreThanZero(Double value) {
        return value != null && value > 0;
    }

    @Override
    public void checkData(OrderDto orderDto) {
        if (orderDto.getUserName() == null || orderDto.getUserName().isBlank()){
            throw new IncorrectInputException("'userName' is required to identify the payer in the system");
        }
        if (orderDto.getDetails() == null || orderDto.getDetails().isEmpty()){
            throw new IncorrectInputException("indicate at least one product detail");
        }
        Double amount = 0D;
        for (OrderDetailDto detail: orderDto.getDetails()) {
            if (!isMoreThanZero(detail.getQuantity())) {
                throw new IncorrectInputException("indicate the quantity of the product for the article '" + detail.getProductArticle() + "'");
            }
            if (!isMoreThanZero(detail.getAmount())) {
                if (!isMoreThanZero(detail.getPrice())) {
                    throw new IncorrectInputException("for a product with an article '" + detail.getProductArticle()+ "' the price or amount must be indicated");
                }
                detail.setAmount(detail.getQuantity() * detail.getPrice());
            }
            amount = amount + detail.getAmount();
        }
        if (!isMoreThanZero(orderDto.getAmount())) {
            orderDto.setAmount(amount);
        } else {
            if (!orderDto.getAmount().equals(amount)) {
                throw new IncorrectInputException("the order amount is not equal to the total for the parts: amount by details = " + amount);
            }
        }
    }

    @Override
    public Optional<OrderDto> addOrder(OrderDto orderDto) {
        OrderEntity order = orderDto.toOrder();
        repositoryUtil.saveOrder(order);
        orderProcessor.process(order);
        return Optional.of(OrderDto.fromOrder(order));
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
        repositoryUtil.getOrderRepository().findAll().forEach(order -> {
            repositoryUtil.loadOrderDetails(order);
            orderDtoList.add(OrderDto.fromOrder(order));
        });
        return orderDtoList;
    }

    @Override
    public OrderDto getOrderById(Long id) {
        OrderEntity order = repositoryUtil.getOrderByIdWithDetails(id);
        return OrderDto.fromOrder(order);
    }
}
