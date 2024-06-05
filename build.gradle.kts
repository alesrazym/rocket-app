// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.nativecoroutines) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependencyAnalysis)
    alias(libs.plugins.kover)
}

dependencyAnalysis {
    // TODO: configuration goes here.
    //  Also, configure when to run `buildHealth` task.
    //  Now, this plugin is here as a reminder it exists and may be worth of use.
}

dependencies {
    kover(projects.android.app)
}

// Hack to the fact, that Android Studio's `Build -> Make Project`
// invokes task `testClasses` that does not exist, e.g.
// ```
// Executing tasks: [:shared:rocket:assemble, :shared:rocket:testClasses,
// :shared:common:assemble, :shared:common:testClasses, ...
// ```
afterEvaluate {
    allprojects {
        if (!project.path.startsWith(":shared"))
            return@allprojects

        if (tasks.any { task -> task.name == "testClasses" }) {
            println("Project \"${project.path}:${project.name}\" contains \"testClasses\" task.")
        } else {
            println("Project \"${project.path}:${project.name}\" does not contain \"testClasses\" task, creating dummy.")
            tasks.register("testClasses") {
                doLast {
                    println("This is a dummy testClasses task")
                }
            }
        }
    }
}
