package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val WhatsAppGreen = Color(0xFF25D366)

@Composable
fun WhatsAppButton(
  text: String = "Chat on WhatsApp",
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isOutlined: Boolean = false
) {
  if (isOutlined) {
    OutlinedButton(
      onClick = onClick,
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = WhatsAppGreen),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, WhatsAppGreen),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
      modifier = modifier.testTag("whatsapp_button_outlined")
    ) {
      Icon(
        imageVector = Icons.Default.Chat,
        contentDescription = "WhatsApp",
        tint = WhatsAppGreen,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
    }
  } else {
    Button(
      onClick = onClick,
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = WhatsAppGreen,
        contentColor = Color.White
      ),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
      modifier = modifier.testTag("whatsapp_button_filled")
    ) {
      Icon(
        imageVector = Icons.Default.Chat,
        contentDescription = "WhatsApp",
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
    }
  }
}
