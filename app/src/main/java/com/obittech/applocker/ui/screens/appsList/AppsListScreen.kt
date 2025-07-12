package com.obittech.applocker.ui.screens.appsList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.obittech.applocker.R
import com.obittech.applocker.domain.models.AppInfo
import com.obittech.applocker.ui.screens.components.LockPinDialog
import com.obittech.applocker.security.PasswordHasher
import com.obittech.applocker.services.AppLockAccessibilityService
import com.obittech.applocker.ui.theme.AppLockerTheme
import com.obittech.applocker.utils.AccessibilityChecker
import com.obittech.applocker.utils.AccessibilityPromptDialog
import com.obittech.applocker.utils.AppLockerTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@Composable
fun AppListScreen(
    openDrawer: () -> Unit,
    viewModel: AppsListViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showPinDialog by remember { mutableStateOf(false) }
    var clickedApp by remember { mutableStateOf<AppInfo?>(null) }
    var isSettingPin by remember { mutableStateOf(true) }
    val appsList by viewModel.apps.collectAsState()
    var shouldShowAllowAccessibilityPermission by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    AccessibilityPromptDialog(
        showDialog = viewModel.showAccessibilityPrompt.value,
        onDismiss = { viewModel.setShowAccessibilityPrompt(false) },
        onOpenSettings = {
            viewModel.setShowAccessibilityPrompt(false)
            AccessibilityChecker.allowAccessibilityServiceIntent(context)
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppLockerTopAppBar (
                openDrawer = openDrawer
            )
        }
    ) { paddingValues ->
//        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Spacer(modifier = Modifier.width(8.dp))
        AppListContent(
            apps = appsList,
            onLockToggle = { isChecked, appInfo ->
                isSettingPin = isChecked
                clickedApp = appInfo
                showPinDialog = true
            },
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (showPinDialog && clickedApp!=null){
        LockPinDialog(
            title = if (isSettingPin) "Set PIN for ${clickedApp!!.name}" else "Enter PIN to Unlock ${clickedApp!!.name}",
            onConfirm = { pin ->
                val hashedPin = PasswordHasher.hashPin(pin)
                if (isSettingPin) viewModel.lockApp(hashedPin, clickedApp!!) else viewModel.unlockApp(hashedPin, clickedApp!!)
                clickedApp = null
                showPinDialog = false
            },
            onDismiss = { showPinDialog = false }
        )
    }
}


@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun AppListContent(
    apps: List<AppInfo>,
    onLockToggle: (Boolean, AppInfo) -> Unit,
    modifier: Modifier = Modifier
){

    var query by remember { mutableStateOf("") }
    var filteredApps by remember { mutableStateOf(apps) }


    LaunchedEffect(apps) {
        snapshotFlow { query }
            .debounce(300)
            .distinctUntilChanged()
            .mapLatest {
                apps.filter { app ->
                    app.name.contains(query.trim(), ignoreCase = true) ||
                            app.name.contains(query.trim(), ignoreCase = true)
                }
            }.flowOn(Dispatchers.Default)
            .collectLatest { filteredApps = it }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.vertical_margin))
    ) {


        LazyColumn {

            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                5.dp
                            ),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                5.dp
                            ),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = {
                            Text(
                                text = "Search",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .padding(horizontal = 8.dp),
                    )
                }
            }

            items(
                items = filteredApps
            ){ appInfo ->
                ListItem(
                    app = appInfo,
                    onLockToggle = {isChecked, appInfo ->
                        onLockToggle(isChecked, appInfo)
                    }
                )
            }

        }
    }
}

@Composable
fun ListItem(
    app: AppInfo,
    onLockToggle: (Boolean, AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconPainter = remember(app.icon) {
                app.icon?.let {
                    val bitmap = it.toBitmap()
                    BitmapPainter(bitmap.asImageBitmap())
                }
            }
            Image(
                painter =  iconPainter ?: painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = app.name,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = app.packageName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Switch(
                checked = app.isLocked,
                onCheckedChange = { isChecked ->
                    onLockToggle(isChecked, app)
                }

            )
        }
    }
}

@Preview
@Composable
fun AppListContentPreview(){
    AppLockerTheme {
        Surface {
            AppListContent(
                apps = listOf(
                    AppInfo(
                        packageName = "com.example.test",
                        name = "Test App",
                        icon = null,
                        isLocked = false),
                    AppInfo(
                        packageName = "com.example.test",
                        name = "Test App",
                        icon = null,
                        isLocked = false),
                    AppInfo(
                        packageName = "com.example.test",
                        name = "Test App",
                        icon = null,
                        isLocked = false),
                    AppInfo(
                        packageName = "com.example.test",
                        name = "Test App",
                        icon = null,
                        isLocked = false),
                ),
                onLockToggle = {isChecked, appInfo ->}
            )
        }
    }
}

@Preview
@Composable
fun ListItemPreview(){
    AppLockerTheme {
        Surface {
            ListItem(
                AppInfo(
                    packageName = "com.example.test",
                    name = "Test App",
                    icon = null,
                    isLocked = false),
                onLockToggle = {isChecked, appInfo ->}
            )
        }
    }
}

