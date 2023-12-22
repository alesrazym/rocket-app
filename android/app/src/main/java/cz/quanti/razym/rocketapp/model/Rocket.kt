package cz.quanti.razym.rocketapp.model

import cz.quanti.razym.rocketapp.presentation.UiText

class Rocket(
    val name: UiText,
    val firstFlight: UiText,
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
