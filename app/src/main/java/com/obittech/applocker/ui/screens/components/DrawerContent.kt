package com.obittech.applocker.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.obittech.applocker.R
import com.obittech.applocker.ui.navigation.Screens

@Composable
fun DrawerContent(onDestinationClicked: (String) -> Unit) {
    Box(modifier = Modifier
        .background(MaterialTheme.colorScheme.surface)){
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.8f)
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .align(Alignment.Start)

            )
            Spacer(modifier = Modifier.height(12.dp))
            DrawerItem(label = "Settings", icon = Icons.Default.Settings) {
                onDestinationClicked(Screens.Settings.route)
            }
            DrawerItem(label = "About App", icon = Icons.Default.Info) {
                onDestinationClicked(Screens.About.route)
            }
        }
    }

}

@Composable
fun DrawerItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview
@Composable
fun DrawerContentPreview() {
    DrawerContent { {} }
}