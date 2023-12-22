package cz.quanti.razym.rocketapp.system

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

/**
 * [LocalSnackbar] provides default value not to crash app, but nothing happens when used.
 * To use in Composable hierarchy, add like:
 * ```
 *     val snackbarHostState = remember { SnackbarHostState() }
 *
 *     Scaffold(
 *         snackbarHost = {
 *             SnackbarHost(hostState = snackbarHostState)
 *         },
 *         ...
 *     ) { innerPadding ->
 *         CompositionLocalProvider(
 *             LocalSnackbar provides snackbarHostState,
 *         ) {
 *             ...
 *         }
 *     }
 *
 * ```
 */
// TODO this may be static composition local of.
val LocalSnackbar = compositionLocalOf {
    SnackbarHostState()
}
