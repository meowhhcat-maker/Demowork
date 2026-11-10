package com.example.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandCyanPrimary

@Composable
fun AdminLoginScreen(
  onCancel: () -> Unit,
  onAuthenticate: (String) -> Boolean,
  onAuthenticationSuccess: () -> Unit
) {
  var pin by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var passwordVisible by remember { mutableStateOf(false) }

  val submitPin = {
    if (onAuthenticate(pin)) {
      onAuthenticationSuccess()
    } else {
      errorMessage = "Incorrect PIN. (Default: 1234)"
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .testTag("admin_login_screen")
  ) {
    // Top Bar
    Surface(
      color = MaterialTheme.colorScheme.background,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onCancel) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
          text = "Admin Security Verification",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, BrandCyanPrimary.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Surface(
            color = BrandCyanPrimary.copy(alpha = 0.12f),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.size(64.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = BrandCyanPrimary,
                modifier = Modifier.size(32.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          Text(
            text = "AUERAA Console Access",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Enter your administrator security PIN to manage live service packages, customer leads, custom proposals, and CRM analytics.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(24.dp))

          OutlinedTextField(
            value = pin,
            onValueChange = {
              pin = it
              errorMessage = null
            },
            label = { Text("Security PIN") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.NumberPassword,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { submitPin() }),
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
                Text(text = "Default PIN: 1234 (Configurable in Settings)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_login_screen_pin_field")
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = { submitPin() },
            colors = ButtonDefaults.buttonColors(containerColor = BrandCyanPrimary),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_login_screen_submit_btn")
          ) {
            Text(
              text = "Unlock Admin Console",
              color = Color.Black,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Cancel & Return Home")
          }
        }
      }
    }
  }
}
