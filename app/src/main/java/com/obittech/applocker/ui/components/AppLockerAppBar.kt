package com.obittech.applocker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.obittech.applocker.R
import com.obittech.applocker.ui.theme.AppLockerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLockerAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { },
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        actions = actions,
        title = title,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            AppLockerIcon(
                contentDescription = stringResource(id = R.string.navigation_drawer_open),
                modifier = Modifier
                    .size(64.dp)
                    .clickable(onClick = onNavIconPressed)
                    .padding(16.dp),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppLockerAppBarPreview() {
    AppLockerTheme {
        AppLockerAppBar(title = {Text("Preview")}, actions = { })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppLockerAppBarPreviewDark() {
    AppLockerTheme (isDarkTheme = true){
        AppLockerAppBar(title = {Text("Preview")}, actions = { })
    }
}