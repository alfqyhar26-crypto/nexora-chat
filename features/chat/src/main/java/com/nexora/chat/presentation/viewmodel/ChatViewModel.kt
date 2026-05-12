package com.nexora.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.chat.domain.model.*
import com.nexora.chat.domain.repository.ChatRepository
import com.nexora.core.logger.NexoraLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    
    private val logger = NexoraLogger("ChatViewModel")
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    init {
        loadConversations()
    }
    
    private fun loadConversations() {
        viewModelScope.launch {
            chatRepository.getConversations().collect { conversations ->
                _uiState.update { it.copy(conversations = conversations) }
            }
        }
    }
    
    fun onMessageChanged(message: String) {
        _uiState.update { it.copy(currentMessage = message) }
    }
    
    fun sendMessage() {
        val conversationId = _uiState.value.currentConversation?.id ?: return
        val content = _uiState.value.currentMessage
        
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            
            val result = chatRepository.sendMessage(
                conversationId = conversationId,
                content = content,
                type = MessageType.TEXT,
                replyToId = null
            )
            
            result.onSuccess {
                _uiState.update { state ->
                    state.copy(currentMessage = "", isSending = false)
                }
                logger.info("ChatViewModel", "Message sent successfully")
            }.onError { error ->
                _uiState.update { it.copy(error = error.message, isSending = false) }
                logger.error("ChatViewModel", "Failed to send message", error)
            }
        }
    }
    
    fun selectConversation(conversation: Conversation) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentConversation = conversation, isLoading = true) }
            chatRepository.markAsRead(conversation.id)
            
            chatRepository.getMessages(conversation.id).collect { messages ->
                _uiState.update { it.copy(messages = messages, isLoading = false) }
            }
        }
    }
    
    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            chatRepository.deleteMessage(messageId)
        }
    }
    
    fun addReaction(messageId: String, emoji: String) {
        viewModelScope.launch {
            chatRepository.addReaction(messageId, emoji)
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
