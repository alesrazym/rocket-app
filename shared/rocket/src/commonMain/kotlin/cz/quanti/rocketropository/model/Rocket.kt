package cz.quanti.rocketropository.model

import kotlinx.serialization.Serializable

@Serializable
class Rocket(
    val name: String,
    val firstFlight: String,
    val id: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Rocket

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
