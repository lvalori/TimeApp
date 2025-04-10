package com.lvalori.timeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lvalori.timeapp.domain.model.Phase

@Composable
fun PhaseSelectionDialog(
    onDismiss: () -> Unit,
    onConfirm: (phase: Phase, customPhase: String?, description: String) -> Unit
) {
    var selectedPhase by remember { mutableStateOf<Phase>(Phase.SCARNITURA) }
    var customPhase by remember { mutableStateOf("") }
    var showCustomPhaseInput by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dettagli sessione") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Fase predefinita
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    Column {
                        Text(
                            "Fase",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SegmentedButton(
                                modifier = Modifier.weight(1f),
                                options = Phase.getDefaultPhases(),
                                selectedOption = selectedPhase,
                                onOptionSelect = { 
                                    selectedPhase = it
                                    showCustomPhaseInput = false
                                },
                                getLabel = { it.label }
                            )
                            
                            IconButton(
                                onClick = { 
                                    selectedPhase = Phase.CUSTOM
                                    showCustomPhaseInput = true
                                }
                            ) {
                                Icon(Icons.Default.Add, "Aggiungi fase personalizzata")
                            }
                        }
                    }
                }

                // Input fase personalizzata
                if (showCustomPhaseInput) {
                    OutlinedTextField(
                        value = customPhase,
                        onValueChange = { customPhase = it },
                        label = { Text("Fase personalizzata") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Descrizione
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        selectedPhase,
                        if (selectedPhase == Phase.CUSTOM) customPhase else null,
                        description
                    )
                },
                enabled = when {
                    selectedPhase == Phase.CUSTOM && customPhase.isBlank() -> false
                    else -> true
                }
            ) {
                Text("Conferma")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}

@Composable
private fun SegmentedButton(
    modifier: Modifier = Modifier,
    options: List<Phase>,
    selectedOption: Phase,
    onOptionSelect: (Phase) -> Unit,
    getLabel: (Phase) -> String
) {
    Column(modifier = modifier) {
        options.forEach { option ->
            OutlinedButton(
                onClick = { onOptionSelect(option) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedOption == option) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(getLabel(option))
            }
        }
    }
}