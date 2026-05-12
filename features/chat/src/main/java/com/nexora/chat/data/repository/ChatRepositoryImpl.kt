package com.nexora.chat.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nexora.chat.domain.model.*
import com.nexora.chat.domain.repository.ChatRepository
import com.nexora.core.logger.NexoraLogger
import com.nexora.core.util.Result
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : ChatRepository {
    
    private val logger = NexoraLogger("ChatRepository")
    
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    
    override fun getConversations(): Flow<List<Conversation>> = _conversations.asStateFlow()
    
    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return _messages.asStateFlow()
    }
    
    override suspend fun sendMessage(
        conversationId: String,
        content: String,
        type: MessageType,
        replyToId: String?
    ): Result<Message> {
        return try {
            logger.info("ChatRepository", "Sending message to $conversationId")
            
            val message = Message(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = "current_user",
                senderName = "You",
                senderAvatar = null,
                type = type,
                content = content,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            // Add to local state
            _messages.value = _messages.value + message
            
            Result.Success(message)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to send message", e)
            Result.Error(e)
        }
    }
    
    override suspend fun editMessage(messageId: String, content: String): Result<Message> {
        return try {
            logger.info("ChatRepository", "Editing message $messageId")
            
            val updatedMessages = _messages.value.map { msg ->
                if (msg.id == messageId) {
                    msg.copy(content = content, isEdited = true, updatedAt = System.currentTimeMillis())
                } else msg
            }
            _messages.value = updatedMessages
            
            val editedMessage = updatedMessages.find { it.id == messageId }
                ?: throw Exception("Message not found")
            
            Result.Success(editedMessage)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to edit message", e)
            Result.Error(e)
        }
    }
    
    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        return try {
            logger.info("ChatRepository", "Deleting message $messageId")
            _messages.value = _messages.value.map { msg ->
                if (msg.id == messageId) msg.copy(isDeleted = true) else msg
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to delete message", e)
            Result.Error(e)
        }
    }
    
    override suspend fun addReaction(messageId: String, emoji: String): Result<Unit> {
        return try {
            logger.info("ChatRepository", "Adding reaction $emoji to message $messageId")
            // TODO: Implement with backend
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to add reaction", e)
            Result.Error(e)
        }
    }
    
    override suspend fun removeReaction(messageId: String, emoji: String): Result<Unit> {
        return try {
            logger.info("ChatRepository", "Removing reaction $emoji from message $messageId")
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to remove reaction", e)
            Result.Error(e)
        }
    }
    
    override suspend fun markAsRead(conversationId: String): Result<Unit> {
        return try {
            logger.info("ChatRepository", "Marking conversation $conversationId as read")
            _conversations.value = _conversations.value.map { conv ->
                if (conv.id == conversationId) conv.copy(unreadCount = 0) else conv
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to mark as read", e)
            Result.Error(e)
        }
    }
    
    override suspend fun createConversation(
        userIds: List<String>,
        name: String?,
        type: ConversationType
    ): Result<Conversation> {
        return try {
            logger.info("ChatRepository", "Creating conversation")
            val conversation = Conversation(
                id = UUID.randomUUID().toString(),
                type = type,
                name = name,
                avatarUrl = null,
                lastMessage = null,
                unreadCount = 0,
                members = userIds,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            _conversations.value = _conversations.value + conversation
            Result.Success(conversation)
        } catch (e: Exception) {
            logger.error("ChatRepository", "Failed to create conversation", e)
            Result.Error(e)
        }
    }
    
    override suspend fun addMember(conversationId: String, userId: String): Result<Unit> {
        return Result.Success(Unit)
    }
    
    override suspend fun removeMember(conversationId: String, userId: String): Result<Unit> {
        return Result.Success(Unit)
    }
    
    override fun getTypingIndicators(conversationId: String): Flow<List<TypingIndicator>> {
        return MutableStateFlow(emptyList())
    }
    
    override suspend fun sendTypingIndicator(conversationId: String, isTyping: Boolean): Result<Unit> {
        return Result.Success(Unit)
    }
}
