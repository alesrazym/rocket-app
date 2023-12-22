package cz.quanti.razym.rocketropository

interface Platform {
    val name: String
}

// TODO: as per docs, https://kotlinlang.org/docs/multiplatform-discover-project.html#apple-device-and-simulator-targets
//  here it is correct actual usage in `iosMain`.
//  There are some issues on youtrack, marked fixed, but not included in build yet. For several years.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect fun getPlatform(): Platform
