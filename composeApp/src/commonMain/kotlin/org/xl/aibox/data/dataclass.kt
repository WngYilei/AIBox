package com.xl.composemultiplatformapp.data
import kotlinx.serialization.*
/**
 * @Author : wyl
 * @Date : 2023/2/18
 * Desc :
 */


data class ChatMessage(var message: String, var thinkMessage: String ="", val isUser: Boolean)