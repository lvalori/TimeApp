package com.lvalori.timeapp.ui.screens

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.PermissionStatus
import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lvalori.timeapp.domain.model.Project
import com.lvalori.timeapp.util.ImageUtils
import java.io.File
import java.util.UUID
import java.time.LocalDateTime

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NewProjectScreen(
    onProjectCreated: (Project) -> Unit,
    onNavigateBack: () -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempPhotoFile: File? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Cleanup effect for temporary files
    DisposableEffect(Unit) {
        onDispose {
            ImageUtils.deleteTemporaryFiles(context)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempPhotoFile?.let { file ->
                imageUri = ImageUtils.getUriForFile(context, file)
            }
        } else {
            // Clean up the temp file if the photo wasn't taken
            tempPhotoFile?.delete()
            tempPhotoFile = null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imageUri = it }
    }

    fun handleCameraCapture() {
        try {
            tempPhotoFile = ImageUtils.createImageFile(context)
            tempPhotoFile?.let { file ->
                val uri = ImageUtils.getUriForFile(context, file)
                cameraLauncher.launch(uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo Progetto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text("Nome Progetto") },
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Project image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Seleziona un'immagine")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        when (cameraPermissionState.status) {
                            is PermissionStatus.Granted -> handleCameraCapture()
                            is PermissionStatus.Denied -> cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Outlined.Camera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scatta Foto")
                }

                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galleria")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (projectName.isNotBlank() && imageUri != null) {
                        val newProject = Project(
                            id = UUID.randomUUID().toString(),
                            name = projectName,
                            imagePath = imageUri.toString(),
                            createdAt = LocalDateTime.now()
                        )
                        onProjectCreated(newProject)
                    }
                },
                enabled = projectName.isNotBlank() && imageUri != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crea Progetto")
            }
        }
    }
}