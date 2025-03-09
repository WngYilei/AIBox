package com.xl.composemultiplatformapp.data
import kotlinx.serialization.*
/**
 * @Author : wyl
 * @Date : 2023/2/18
 * Desc :
 */
@Serializable
data class ResponseBean(
    val `data`: String,
    val errorCode: Int,
    val errorMsg: String
)

data class ChatMessage(var message: String, val isUser: Boolean)