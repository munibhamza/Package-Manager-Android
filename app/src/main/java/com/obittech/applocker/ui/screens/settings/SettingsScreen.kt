package com.obittech.applocker.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Lock

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.obittech.applocker.R
import com.obittech.applocker.ui.theme.AppLockerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {


    Scaffold (modifier = Modifier
        .fillMaxSize(),
        topBar = {TopAppBar(
            title = { Text(text = stringResource(id = R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = { } ) {
                    Icon(Icons.Filled.ArrowBackIosNew, stringResource(id = R.string.pop_back))
                }
            }
        )
        }
        )
    { paddingValues ->
        Column {
            Spacer(modifier = Modifier.width(8.dp))
            SettingsScreenContent(modifier = Modifier.padding(paddingValues))

        }
    }




}

@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel = hiltViewModel(), modifier: Modifier){

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.vertical_margin), horizontal = 16.dp)
    ) {
//        Text(
//            text = "Settings",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(bottom = 24.dp)
//        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    // Navigate to password change screen
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Change App Password")
            }
        }

        Text("Unlock Prompt Type", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !uiState.useOverlay,
                onClick = {
                    uiState.useOverlay = !uiState.useOverlay
                    viewModel.setOverlayUsage(uiState.useOverlay)
                }
            )
            Text("Use Activity")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = uiState.useOverlay,
                onClick = {
                    uiState.useOverlay = !uiState.useOverlay
                    viewModel.setOverlayUsage(uiState.useOverlay)
                }
            )
            Text("Use Overlay")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Checkbox(
                checked = uiState.lockOnUnlock,
                onCheckedChange = { viewModel.setLockOnUnlock(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Lock apps on device unlock")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Checkbox(
                checked = uiState.onboardingComplete,
                onCheckedChange = { viewModel.setOnboardingComplete(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Setup completed on Onboarding")
        }

    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    // Preview the SettingsScreen
    SettingsScreen()

}
