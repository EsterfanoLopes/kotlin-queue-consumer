package com.example.consumer.service

import com.example.consumer.model.Person
import com.example.consumer.repository.PersonRepository
import com.rabbitmq.client.Channel
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class AckListener(
    private val personRepository: PersonRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["FIRST-QUEUE-ADVANCED"], ackMode = "MANUAL", concurrency="5-6")
    fun consumerFirstQueue(
        person: Person,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long?
    ) = runBlocking {
        log.info("person ${person}")
        try {
            personRepository.findOneByExternalId(person.externalId)
                .awaitFirstOrNull()
                ?.let{ found ->
                    log.info("found ${found}")
                    when(found.collageCompletedYear) {
                        null -> {
                            log.warn("message wrong")
                            channel.basicNack(tag ?: 0L, false, false)
                        }
                        else -> {
                            log.info("message Ok")
                            personRepository
                                .save(found.copy(delivered = true))
                                .awaitFirstOrNull()
                                ?.let {
                                    channel.basicAck(tag ?: 0L, false)
                                }
                        }
                    }
                } ?: throw Exception("person not found")
        } catch (e: Exception) {
            log.warn(e.message)
        }
    }
}