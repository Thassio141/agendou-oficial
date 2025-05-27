package br.com.agendou.ui.screens.service

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import br.com.agendou.domain.model.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceFormBottomSheet(
    professionalId: String,
    initialService: Service? = null,
    onDismiss: () -> Unit,
    onSave: (Service) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf(initialService?.name ?: "") }
    var description by remember { mutableStateOf(initialService?.description ?: "") }
    var priceInput by remember { mutableStateOf(initialService?.price?.toString() ?: "") }
    var durationInput by remember { mutableStateOf(initialService?.duration?.toString() ?: "") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = if (initialService == null) "Novo Serviço" else "Editar Serviço", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = priceInput,
                onValueChange = { priceInput = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                label = { Text("Preço (R$)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = durationInput,
                onValueChange = { durationInput = it.filter { ch -> ch.isDigit() } },
                label = { Text("Duração (min)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    val price = priceInput.replace(',', '.').toDoubleOrNull() ?: 0.0
                    val duration = durationInput.toIntOrNull() ?: 0
                    val service = Service(
                        id = initialService?.id ?: "",
                        professionalId = professionalId,
                        name = name,
                        description = description,
                        price = price,
                        duration = duration,
                        deletedAt = initialService?.deletedAt ?: null
                    )
                    onSave(service)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && priceInput.isNotBlank() && durationInput.isNotBlank()
            ) {
                Text(text = if (initialService == null) "Salvar" else "Atualizar")
            }
        }
    }
} 