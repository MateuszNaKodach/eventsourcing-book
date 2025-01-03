package de.eventsourcingbook.cart.inventories.internal

import de.eventsourcingbook.cart.events.InventoryChangedEvent
import de.eventsourcingbook.cart.inventories.InventoriesReadModelEntity
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.SequenceNumber
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.UUID

interface InventoriesReadModelRepository : JpaRepository<InventoriesReadModelEntity, UUID>

@ProcessingGroup("inventories")
@Component
class InventoriesReadModelProjector(
    var repository: InventoriesReadModelRepository,
    var versionRepository: InventoryProjectionVersionRepository,
) {
    @EventHandler
    fun on(
        event: InventoryChangedEvent,
        @SequenceNumber sequenceNumber: Long,
    ) {
        Thread.sleep(5000)
        val entity = this.repository.findById(event.productId).orElse(InventoriesReadModelEntity())
        entity
            .apply {
                inventory = event.inventory
                productId = event.productId
            }.also { this.repository.save(it) }
        this.versionRepository.save(InventoryProjectionVersion(event.productId, sequenceNumber))
    }
}
