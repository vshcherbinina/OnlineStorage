package onlinestore.inventoryservice.event.handler;

import onlinestore.inventoryservice.event.InventoryDocumentProcessor;
import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.event.OrderStatusEvent;
import onlinestore.inventoryservice.model.entity.*;
import onlinestore.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderPaidEventHandler implements EventHandler<OrderEvent, OrderStatusEvent> {


    private final InventoryService inventoryService;

    private final InventoryDocumentProcessor documentProcessor;

    private static final Logger logger = LoggerFactory.getLogger(OrderPaidEventHandler.class);

    @Autowired
    public OrderPaidEventHandler(InventoryService inventoryService, InventoryDocumentProcessor documentProcessor) {
        this.inventoryService = inventoryService;
        this.documentProcessor = documentProcessor;
    }

    @Override
    @Transactional
    public OrderStatusEvent handleEvent(OrderEvent event) {
        InventoryDocumentEntity document = new InventoryDocumentEntity(event);
        try {
            inventoryService.saveDocumentWithDetails(document);
            inventoryService.deductProductStockBalance(document);
        } catch (Exception e) {
            inventoryService.failedDocument(document, e.getMessage());
        }
        if (document.getStatus().equals(DocumentStatus.INVENTED)) {
            documentProcessor.process(document);
        }
        return OrderStatusEvent.fromInventoryDocumentEntity(document);
    }

}
