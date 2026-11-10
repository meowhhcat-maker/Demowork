package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BrandCyanPrimary

@Composable
fun AdminLoginDialog(
  onDismiss: () -> Unit,
  onAuthenticate: (String) -> Boolean
) {
  var pin by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var passwordVisible by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("admin_login_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Surface(
          color = BrandCyanPrimary.copy(alpha = 0.15f),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.size(54.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = BrandCyanPrimary,
              modifier = Modifier.size(28.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "AUERAA Admin Access",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Enter your security PIN to manage packages, clients, and enquiries.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
          value = pin,
          onValueChange = {
            pin = it
            errorMessage = null
          },
          label = { Text("Admin PIN") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Toggle PIN visibility",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          },
          isError = errorMessage != null,
          supportingText = {
            if (errorMessage != null) {
              Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            } else {
              Text(text = "Default PIN: 1234", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().testTag("admin_pin_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f).testTag("admin_cancel_btn")
          ) {
            Text("Cancel")
          }

          Button(
            onClick = {
              if (onAuthenticate(pin)) {
                onDismiss()
              } else {
                errorMessage = "Incorrect PIN. Try default '1234'"
              }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
            modifier = Modifier.weight(1f).testTag("admin_submit_btn")
          ) {
            Text("Unlock", color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
