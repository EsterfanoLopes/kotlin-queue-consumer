package com.example.consumer.model

import com.example.consumer.util.exceptions.ValidationException
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.util.UUID

data class Person(
    @Id
    val id: ObjectId?,
    val externalId: UUID,
    val name: String,
    val collageCompletedYear: Int?,
    val bornAt: LocalDate,
    val active: Boolean,
    var delivered: Boolean?
) {
    fun validate() {
        if (this.collageCompletedYear == null) {
            throw ValidationException("collage completed year is missing.")
        }
    }
}
