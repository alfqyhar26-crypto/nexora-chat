package com.nexora.chat.domain.repository

import com.nexora.chat.domain.model.*
import com.nexora.core.util.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getConversations(): Flow<List<Conversation>>
    fun getMessages(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(conversationId: String, content: String, type: MessageType, replyToId: String?): Result<Message>
    suspend fun editMessage(messageId: String, content: String): Result<Message>
    suspend fun deleteMessage(messageId: String): Result<Unit>
    suspend fun addReaction(messageId: String, emoji: String): Result<Unit>
    suspend fun removeReaction(messageId: String, emoji: String): Result<Unit>
    suspend fun markAsRead(conversationId: String): Result<Unit>
    suspend fun createConversation(userIds: List<String>, name: String?, type: ConversationType): Result<Conversation>
    suspend fun addMember(conversationId: String, userId: String): Result<Unit>
    suspend fun removeMember(conversationId: String, userId: String): Result<Unit>
    fun getTypingIndicators(conversationId: String): Flow<List<TypingIndicator>>
    suspend fun sendTypingIndicator(conversationId: String, isTyping: Boolean): Result<Unit>
}
