package com.tomaszrykala.recsandfx.feature.permissions

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// TODO Not all permissions have to be requested at launch. Can be delayed to the FxDetail screen.
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsScreen(
    contentPadding: PaddingValues = PaddingValues(),
    permissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(getPermissionsList())
) {
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {
        Text(
            getTextToShowGivenPermissions(
                permissionsState.revokedPermissions,
                permissionsState.shouldShowRationale
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
            Text("Request permissions")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> textToShow.append(", and ")
            i == revokedPermissionsSize - 1 -> textToShow.append(" ")
            else -> textToShow.append(", ")
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}

fun getPermissionsList(): List<String> = mutableListOf(
    Manifest.permission.RECORD_AUDIO,
).apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        add(Manifest.permission.READ_EXTERNAL_STORAGE)
        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}
