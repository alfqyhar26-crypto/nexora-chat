package com.nexora.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nexora.chat.domain.model.*
import com.nexora.shared.theme.NexoraColors
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick).background(NexoraColors.Background).padding(16.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(NexoraColors.SurfaceVariant), contentAlignment = Alignment.Center) {
            Text(text = (conversation.name ?: "?").take(2).uppercase(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NexoraColors.TextPrimary)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = conversation.name ?: "Unknown", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = NexoraColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                Text(text = formatTime(conversation.updatedAt), fontSize = 12.sp, color = NexoraColors.TextTertiary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = conversation.lastMessage?.content ?: "No messages", fontSize = 14.sp, color = NexoraColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                if (conversation.unreadCount > 0) {
                    Box(modifier = Modifier.size(24.dp).background(NexoraColors.Primary, CircleShape), contentAlignment = Alignment.Center) {
                        Text(text = if (conversation.unreadCount > 99) "99+" else conversation.unreadCount.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isOwnMessage: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp, 4.dp), horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start) {
        Box(modifier = Modifier.widthIn(max = 280.dp).background(if (isOwnMessage) NexoraColors.MessageSent else NexoraColors.MessageReceived, RoundedCornerShape(16.dp, 16.dp, if (isOwnMessage) 16.dp else 4.dp, if (isOwnMessage) 4.dp else 16.dp)).padding(12.dp, 8.dp)) {
            Text(text = message.content, fontSize = 15.sp, color = NexoraColors.TextPrimary)
        }
        Text(text = formatTime(message.createdAt), fontSize = 11.sp, color = NexoraColors.TextTertiary, modifier = Modifier.padding(8.dp, 2.dp))
    }
}

@Composable
fun ChatInputBar(message: String, onMessageChange: (String) -> Unit, onSend: () -> Unit, onAttachClick: () -> Unit, onVoiceClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().background(NexoraColors.Surface).padding(8.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(onClick = onAttachClick) { Icon(Icons.Default.AttachFile, "Attach", tint = NexoraColors.TextSecondary) }
        Box(modifier = Modifier.weight(1f).background(NexoraColors.SurfaceVariant, RoundedCornerShape(24.dp)).padding(16.dp, 8.dp)) {
            TextField(value = message, onValueChange = onMessageChange, placeholder = { Text("Type a message...", color = NexoraColors.TextTertiary) }, colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent), modifier = Modifier.fillMaxWidth(), maxLines = 4)
        }
        if (message.isBlank()) {
            IconButton(onClick = onVoiceClick) { Icon(Icons.Default.Mic, "Voice", tint = NexoraColors.Primary) }
        } else {
            IconButton(onClick = onSend, modifier = Modifier.size(48.dp).background(NexoraColors.Primary, CircleShape)) { Icon(Icons.Default.Send, "Send", tint = Color.White) }
        }
    }
}

@Composable
fun ConversationsList(conversations: List<Conversation>, onConversationClick: (Conversation) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.background(NexoraColors.Background), state = rememberLazyListState()) {
        items(conversations, key = { it.id }) { conv ->
            ConversationItem(conversation = conv, onClick = { onConversationClick(conv) })
            Divider(color = NexoraColors.Divider, thickness = 0.5.dp)
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "now"
        diff < 3600_000 -> "${diff / 60_000}m"
        diff < 86400_000 -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
        else -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date(timestamp))
    }
}
