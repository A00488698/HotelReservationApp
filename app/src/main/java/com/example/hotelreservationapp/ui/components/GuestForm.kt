package com.example.hotelreservationapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotelreservationapp.model.Guest

@Composable
fun GuestForm(
    index: Int,
    guest: Guest,
    onGuestChanged: (Guest) -> Unit,
    showError: Boolean
) {
    var name by remember { mutableStateOf(guest.guest_name) }
    var gender by remember { mutableStateOf(guest.gender) }

    Column(modifier = Modifier.padding(8.dp)) {
        Text("Guest $index", style = MaterialTheme.typography.titleSmall)
        OutlinedTextField(
            value = name,
            onValueChange = { newName ->
                name = newName
                onGuestChanged(Guest(guest_name = newName, gender = gender))
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            // 仅在提交时且为空才显示错误状态
            isError = showError && name.trim().isEmpty()
        )
        if (showError && name.trim().isEmpty()) {
            Text(
                text = "Name cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            RadioButton(
                selected = gender == "Male",
                onClick = {
                    gender = "Male"
                    onGuestChanged(Guest(guest_name = name, gender = "Male"))
                }
            )
            Text("Male", modifier = Modifier.padding(end = 16.dp))
            RadioButton(
                selected = gender == "Female",
                onClick = {
                    gender = "Female"
                    onGuestChanged(Guest(guest_name = name, gender = "Female"))
                }
            )
            Text("Female")
        }
    }
}