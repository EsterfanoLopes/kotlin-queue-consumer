package com.example.consumer.service

import com.example.consumer.repository.PersonRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PersonService(
    private val personRepository: PersonRepository
) {
    suspend fun receiveMessage(externalId: UUID): Boolean {
        personRepository.findOneByExternalId(externalId)
            .awaitFirstOrNull()
            ?.let{ found ->
                found.validate()
                personRepository
                    .save(found.copy(delivered = true))
                    .awaitFirstOrNull()
            } ?: throw Exception("person not found")
        return true
    }
}