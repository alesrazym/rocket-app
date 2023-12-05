# detekt & ktlint

[detekt](https://detekt.dev/) is a static code analysis tool for the Kotlin programming language.  
[ktlint](https://pinterest.github.io/ktlint) is an anti-bikeshedding Kotlin linter with built-in
formatter.

In general, preferred is to use `detekt` to find code smells and formatting issues,
as well as formatting itself. `ktlint` is used to enforce formatting where `detekt` fails.

## Setup

### Android Studio / IDEA plugins
In Android Studio, go to `Settings > Plugins > Marketplace` and install
 * `detekt` and
 * [ktlint](https://plugins.jetbrains.com/plugin/15057-ktlint) v0.20.0 or higher.

_NOTE: You may have to install plugins one by one, otherwise installation fails._

_NOTE: Keep `ktlint` version that matches ktlint idea plugin and additional rule-sets._

We use the 'manual' mode of `ktlint` plugin now, so ktlint format is never run automatically. 
To format use `Refactor > Format With Ktlint`.

### Gradle plugins
`detekt` is added as per 
[doc](https://detekt.dev/docs/gettingstarted/gradle#configuration-for-android-projects).

## Configuration

We use [detekt.yml](./detekt.yml) and [.editorconfig](./.editorconfig) files to configure the
plugins and gradle tasks.

Some rules must be disabled or handled with care, as they overlap each other, e.g.:
`MaxLineLength` and `MaximumLineLength`. Also, see [Known issues](#known-issues).

### Rules

`detect` is configured in module level build.gradle.kts with `detect { }` closure.
We use additional `detektPlugins`, see module level build.gradle.kts file dependencies for
reference.
Rules are configured in [detekt.yml](./detekt.yml) file,
where we use all rules generated with `./gradlew detectGenerateConfig` command.
When updating to new version, re-generate config file, e.g. like this:

1. Rename `detekt.yml` to e.g. `detekt_custom.yml`
2. Run `./gradlew detektGenerateConfig` with old rule-set, rename output `detekt.yml`
   to `detect_old.yml`
3. Update rule-set, run `./gradlew detektGenerateConfig`, rename output `detekt.yml`
   to `detect_new.yml`
4. Use diff to check (and apply) changes.
    - I.e. check old and new are the same
    - Or check all 3. In Android Studio, select them one by one with
      Ctrl/Cmd. Order matters. Then use context menu or Ctrl/Cmd + D to see 3way diff.
      Apply changes that make sense.
5. Finally, drop new and old, rename `detekt_custom.yml` back to `detekt.yml`
6. Synchronize changes that are applicable to `.editorconfig` file.

_TODO: We may want to set git hooks._

## Usage

### Formatting
1. Use the `Refactor > Autocorrect by detect Rules` action to fix issues using detekt.yml settings.  
   Alternatively, use `./gradlew detekt --auto-correct` command.
2. Where `detect` fails, use the `Refactor > Format With Ktlint` action to run formatting based on
`.editorconfig` and IDEA / Android Studio settings.

### Static code analysis (Code smells)
Use `./gradlew detekt` to run `detekt` plugin. Thanks to IDE plugin, it should also be available
right in editor checks.

To automatically fix issues, where possible, use `./gradlew detekt --auto-correct`.

## Known issues
Listed issues may not be active in current configuration.

### Parameter List Wrapping & Function signature
 * https://detekt.dev/docs/rules/formatting/#functionsignature
 * https://detekt.dev/docs/rules/formatting/#parameterlistwrapping
 * https://pinterest.github.io/ktlint/latest/rules/standard/#function-signature
 * https://pinterest.github.io/ktlint/latest/rules/standard/#parameter-list-wrapping
Code:
```kotlin
    @GET("v4/rockets/{id}")
    suspend fun getRocket(@Path("id") id: String): RocketData
```
is ok for detekt, but ktlint wants it to be:
```kotlin
    @GET("v4/rockets/{id}")
    suspend fun getRocket(
        @Path("id") id: String
    ): RocketData
```
It can be suppressed using
```kotlin
    @Suppress("ktlint:standard:function-signature", "ktlint:standard:parameter-list-wrapping")
```

### Multiline expression wrapping & Function signature
NOTE: Since `ktlint_function_signature_body_expression_wrapping = multiline` in ktlint official,
this problem should not happen anymore.

* https://detekt.dev/docs/rules/formatting/#multilineexpressionwrapping
* https://detekt.dev/docs/rules/formatting#functionsignature
* https://pinterest.github.io/ktlint/latest/rules/experimental/#multiline-expression-wrapping
* https://pinterest.github.io/ktlint/latest/rules/standard/#function-signature
 
When set `ktlint_function_signature_body_expression_wrapping = default`,
by function signature wrapping rules this code is ok:
```kotlin
private fun previewRockets(num: Int = 9) = List(num) {
    previewRocket(it)
}
```
ktlint complains about it and wants it to be:
```kotlin
private fun previewRockets(num: Int = 9) =
    List(num) {
        previewRocket(it)
    }
```
But then, it complains about function signature.

### Indentation
Rule `standard_indent` does not work as expected with detekt (as `formatting:Indentation`) when
Multiline expression wrapping & Function signature is on.
* https://detekt.dev/docs/rules/formatting/#indentation
* https://pinterest.github.io/ktlint/latest/rules/standard/#indentation

Code formatted with `ktlint` as follows:
```kotlin
return RocketDetailUiState(
    id = "1",
    name = "Falcon 1",
    firstStage =
        Stage(
            reusable = false,
            engines = 1,
            fuelAmountTons = 44.3,
            burnTimeSec = 169,
        ).asStageUiState(),
    // ...
    )
```
is reported by `detekt` and formatted as follows:
```kotlin
return RocketDetailUiState(
    id = "1",
    name = "Falcon 1",
    firstStage =
    Stage(
        reusable = false,
        engines = 1,
        fuelAmountTons = 44.3,
        burnTimeSec = 169,
    ).asStageUiState(),
    // ...
)
```

### detekt cannot do what ktlint does
* standard:chain-method-continuation - not yet released as of 1.23.4, 
  but [already implemented](https://github.com/detekt/detekt/blob/main/detekt-formatting/src/main/kotlin/io/gitlab/arturbosch/detekt/formatting/wrappers/ChainMethodContinuation.kt).
