package com.example.consumer.controller

import com.example.consumer.model.Person
import com.example.consumer.service.PersonService
import com.example.consumer.util.exceptions.ValidationException
import com.rabbitmq.client.Channel
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Controller

@Controller
class AckListener(
    private val personService: PersonService
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
            personService.receiveMessage(person.externalId)
            channel.basicAck(tag ?: 0L, false)
        } catch (e: Throwable) {
            when(e) {
                is ValidationException -> log.warn("validation error: ${e.message}")
                else -> log.error("unexpected error: ${e.message}")
            }
            channel.basicNack(tag ?: 0L, false, false)
        }
    }
}