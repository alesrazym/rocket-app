# detekt & ktlint

[detekt](https://detekt.dev/) is a static code analysis tool for the Kotlin programming language.  
[ktlint](https://pinterest.github.io/ktlint) is an anti-bikeshedding Kotlin linter with built-in
formatter.
[kotlinter](https://github.com/jeremymailen/kotlinter-gradle) is a fast Gradle integration
of `ktlint`.

In general, `detekt` is used to find code smells, while `ktlint` is used to enforce formatting.

_Note: [kotlinter](https://github.com/jeremymailen/kotlinter-gradle) is faster
then [ktlint](https://github.com/jlleitschuh/ktlint-gradle) Gradle plugin._

## Setup

### Android Studio / IDEA plugins
In Android Studio, go to `Settings > Plugins > Marketplace` and install
 * `detekt` and
 * [ktlint](https://plugins.jetbrains.com/plugin/15057-ktlint) v0.20.0 or higher.

_NOTE: You may have to install plugins one by one, otherwise installation fails._

_NOTE: Keep `ktlint` version that matches ktlint idea plugin and additional rule-sets._

We use the 'manual' mode of `ktlint` plugin now, so ktlint format is never run automatically. 
To format use `Refactor > Format With Ktlint`.

#### Additional rule-sets
##### [compose-rules](https://github.com/mrmans0n/compose-rules)  
[Download rule-set](https://github.com/mrmans0n/compose-rules/releases) for ktlint that match
the version specified in [.idea/ktlint-plugin.xml](.idea/ktlint-plugin.xml) file.
Place it in the project root folder, keep git-ignored.

_NOTE: Compose rules are not yet tested and set up._

### Gradle plugins
`detekt` is added as per 
[doc](https://detekt.dev/docs/gettingstarted/gradle#configuration-for-android-projects).

`kotlinter` is added as per [doc](https://github.com/jeremymailen/kotlinter-gradle#readme) with 
custom rule-set (force rules version, add compose rules).

## Configuration

We use [detekt.yml](./detekt.yml) and [.editorconfig](./.editorconfig) files to configure the
plugins and gradle tasks.

Some rules must be disabled or handled with care, as they overlap each other, e.g.:
`MaxLineLength` and `MaximumLineLength`.

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

`kotlinter` is configured in top level build.gradle.kts with custom and specific rule-set
and reporter versions (reporters are subject to remove if we don't use).

## Usage

### Formatting
Use the `Code > Reformat Code` action to run formatting based on
`.editorconfig` and IDEA / Android Studio settings.

You can use also Gradle tasks from `kotlinter` plugin (to be used in CI or git hooks):
1. `./gradlew lintKotlin`: report Kotlin lint errors and by default fail the build.
2. `./gradlew formatKotlin`: format Kotlin source code according to ktlint rules or 
warn when auto-format not possible.
3. `./gradlew check`: becomes dependent on `lintKotlin`.

### Static code analysis (Code smells)
Use `./gradlew detekt` to run `detekt` plugin. Thanks to IDE plugin, it should also be available
right in editor checks.

To automatically fix issues, where possible, use `./gradlew detekt --auto-correct`.
