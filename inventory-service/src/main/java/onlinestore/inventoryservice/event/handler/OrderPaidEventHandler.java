package onlinestore.inventoryservice.event.handler;

import onlinestore.inventoryservice.event.InventoryDocumentProcessor;
import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.event.OrderStatusEvent;
import onlinestore.inventoryservice.model.entity.DocumentStatus;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderPaidEventHandler implements EventHandler<OrderEvent, OrderStatusEvent> {

    private final InventoryService inventoryService;
    private final InventoryDocumentProcessor documentProcessor;

    @Autowired
    public OrderPaidEventHandler(InventoryService inventoryService, InventoryDocumentProcessor documentProcessor) {
        this.inventoryService = inventoryService;
        this.documentProcessor = documentProcessor;
    }

    @Override
    @Transactional
    public OrderStatusEvent handleEvent(OrderEvent event) {
        InventoryDocumentEntity document = event.toInventoryDocumentEntity();
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
