package com.example.consumer.repository

import com.example.consumer.model.Person
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*

interface PersonRepository: ReactiveMongoRepository<Person, String> {
    fun findOneByExternalId(externalId: UUID): Mono<Person>
}