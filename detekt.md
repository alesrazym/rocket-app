# detekt & ktlint
[detekt](https://detekt.dev/) is a static code analysis tool for the Kotlin programming language.  
[ktlint](https://pinterest.github.io/ktlint) is an anti-bikeshedding Kotlin linter with built-in formatter.


## Setup
In Android Studio, go to `Preferences > Plugins > Browse Repositories` and search for 
`detekt` and `ktlint`.

Install the plugins and restart Android Studio.

## Configuration
We use [detekt.yml](./detekt.yml) and [.editorconfig](./.editorconfig) files to configure the plugins and gradle tasks.

Source of truth is the [detekt.yml](./detekt.yml) file.
The [.editorconfig](./.editorconfig) file is used to provide formatting to `ktlint` plugin
inside Android Studio's `Code > Reformat Code` action.

### Rules
detect is configured in module level build.gradle.kts with `detect { }` closure.
We use additional `detektPlugins`, see module level build.gradle.kts file dependencies for reference.
Rules are configured in [detekt.yml](./detekt.yml) file,
where we use all rules generated with `./gradlew detectGenerateConfig` command.
When updating to new version, re-generate config file, e.g. like this:
1. Rename `detekt.yml` to e.g. `detekt_custom.yml`
2. Run `./gradlew detektGenerateConfig` with old rule-set, rename output `detekt.yml` to `detect_old.yml`
3. Update rule-set, run `./gradlew detektGenerateConfig`, rename output `detekt.yml` to `detect_new.yml`
4. Use diff to check (and apply) changes.
    - I.e. check old and new are the same
    - Or check all 3. In Android Studio, select them one by one with
      Ctrl/Cmd. Order matters. Then use context menu or Ctrl/Cmd + D to see 3way diff.
      Apply changes that make sense.
5. Finally, drop new and old, rename `detekt_custom.yml` back to `detekt.yml`
6. Synchronize changes that are applicable to `.editorconfig` file.

_TODO: We may want to set git hooks._

## Usage
Use the `Code > Reformat Code` action to run format with `ktlint` plugin.

Use `./gradlew detekt` to run `detekt` plugin.

_TODO: One may want to configure once in top level gradle file to all modules._
