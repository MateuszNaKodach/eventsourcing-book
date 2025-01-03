package de.eventsourcingbook.cart.domain

import de.eventsourcingbook.cart.common.CommandResult
import de.eventsourcingbook.cart.domain.commands.changeinventory.ChangeInventoryCommand
import de.eventsourcingbook.cart.events.InventoryChangedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class InventoryAggregate {
    @AggregateIdentifier lateinit var aggregateId: UUID

    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    @CommandHandler
    fun handle(command: ChangeInventoryCommand): CommandResult {
        AggregateLifecycle.apply(InventoryChangedEvent(command.productId, command.inventory))
        return CommandResult(command.productId, AggregateLifecycle.getVersion())
    }

    @EventSourcingHandler
    fun on(inventoryChangedEvent: InventoryChangedEvent) {
        this.aggregateId = inventoryChangedEvent.productId
    }
}
