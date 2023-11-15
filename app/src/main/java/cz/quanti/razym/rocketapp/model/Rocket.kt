package cz.quanti.razym.rocketapp.model

import java.util.Date

class Rocket(
    val name: String,
    val firstFlight: Date?,
    val id: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rocket

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
