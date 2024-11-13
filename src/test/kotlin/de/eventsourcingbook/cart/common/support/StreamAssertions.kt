package de.eventsourcingbook.cart.common.support

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Component

@Component
class StreamAssertions(
    private val eventStore: EventStore,
) {
    fun assertMetaData(
        aggregateId: String,
        predicate: (event: Map<String, Any>) -> Boolean,
    ) {
        assertThat(eventStore.readEvents(aggregateId).asStream().map { it.metaData })
            .anyMatch(predicate)
    }

    fun assertEvent(
        aggregateId: String,
        predicate: (event: Any) -> Boolean,
    ) {
        assertThat(
            eventStore
                .readEvents(aggregateId)
                .asStream()
                .map { it.payload }
                .toList(),
        ).anyMatch(predicate)
    }
}
