package com.nexora.chat.domain.model

data class Conversation(
    val id: String,
    val type: ConversationType,
    val name: String?,
    val avatarUrl: String?,
    val lastMessage: Message?,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val members: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long
)

enum class ConversationType {
    DIRECT,
    GROUP,
    CHANNEL
}

data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String?,
    val senderAvatar: String?,
    val type: MessageType,
    val content: String,
    val replyTo: Message? = null,
    val reactions: List<Reaction> = emptyList(),
    val mediaUrl: String? = null,
    val mediaType: String? = null,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val isPinned: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
)

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    VOICE,
    FILE,
    GIF,
    STICKER,
    LOCATION,
    CONTACT
}

data class Reaction(
    val emoji: String,
    val userId: String,
    val count: Int = 1
)

data class TypingIndicator(
    val conversationId: String,
    val userId: String,
    val userName: String,
    val isTyping: Boolean
)

data class ChatUiState(
    val conversations: List<Conversation> = emptyList(),
    val currentConversation: Conversation? = null,
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null,
    val typingUsers: List<TypingIndicator> = emptyList(),
    val currentMessage: String = ""
)
