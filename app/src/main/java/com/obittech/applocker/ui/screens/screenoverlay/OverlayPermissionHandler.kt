package com.obittech.applocker.ui.screens.screenoverlay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.Preferences
import androidx.core.net.toUri

@HiltViewModel
class OverlayPermissionViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _shouldAsk = MutableStateFlow(false)
    val shouldAsk: StateFlow<Boolean> = _shouldAsk.asStateFlow()

    companion object {
        val KEY_NEVER_ASK = booleanPreferencesKey("overlay_never_ask")
        val KEY_REMIND_LATER = booleanPreferencesKey("overlay_remind_later")
    }

    init {
        viewModelScope.launch {
            dataStore.data.collect { prefs ->
                val neverAsk = prefs[KEY_NEVER_ASK] ?: false
                val remindLater = prefs[KEY_REMIND_LATER] ?: true
                val isGranted = Settings.canDrawOverlays(context)
                _shouldAsk.value = !isGranted && !neverAsk && remindLater
            }
        }
    }

    fun onAllowClicked() {
        context.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
//            data = Uri.parse("package:${context.packageName}")
            data = "package:${context.packageName}".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun onRemindLaterClicked() {
        viewModelScope.launch {
            dataStore.edit {
                it[KEY_REMIND_LATER] = true
                it[KEY_NEVER_ASK] = false
            }
        }
    }

    fun onDontAllowClicked() {
        viewModelScope.launch {
            dataStore.edit {
                it[KEY_NEVER_ASK] = true
                it[KEY_REMIND_LATER] = false
            }
        }
    }
}

@Composable
fun OverlayPermissionDialog(
    viewModel: OverlayPermissionViewModel = hiltViewModel()
) {
    val shouldAsk by viewModel.shouldAsk.collectAsState()

    if (shouldAsk) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Overlay Permission Required") },
            text = { Text("To show the floating unlock dialog, allow this app to draw over other apps.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onAllowClicked() }) {
                    Text("Allow")
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { viewModel.onRemindLaterClicked() }) {
                        Text("Remind Me Later")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { viewModel.onDontAllowClicked() }) {
                        Text("Don't Allow")
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun OverlayPermissionDialogPreview() {
    OverlayPermissionDialog()
}
