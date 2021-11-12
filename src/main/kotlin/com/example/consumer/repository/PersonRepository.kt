package com.example.consumer.repository

import com.example.consumer.model.Person
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PersonRepository: ReactiveMongoRepository<Person, String> {

}