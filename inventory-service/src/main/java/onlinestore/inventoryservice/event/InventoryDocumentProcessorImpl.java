package onlinestore.inventoryservice.event;

import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class InventoryDocumentProcessorImpl implements InventoryDocumentProcessor{

    private final Sinks.Many<InventoryDocumentEvent> sink;

    @Autowired
    public InventoryDocumentProcessorImpl(Sinks.Many<InventoryDocumentEvent> sink) {
        this.sink = sink;
    }

    @Override
    public void process(InventoryDocumentEntity document) {
        sink.emitNext(InventoryDocumentEvent.fromInventoryDocumentEntity(document), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
