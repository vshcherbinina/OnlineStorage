package onlinestore.inventoryservice.event;

import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;

public interface InventoryDocumentProcessor {
    void process(InventoryDocumentEntity document);
}
