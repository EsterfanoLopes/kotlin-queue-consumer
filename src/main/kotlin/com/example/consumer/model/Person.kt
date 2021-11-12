package com.example.consumer.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDate

data class Person(
    @Id
    val id: ObjectId?,
    val name: String,
    val collageCompletedYear: Int?,
    val bornAt: LocalDate,
    val active: Boolean,
    var delivered: Boolean?
)
