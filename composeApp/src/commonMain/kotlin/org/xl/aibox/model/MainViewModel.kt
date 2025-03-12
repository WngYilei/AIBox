package com.xl.composemultiplatformapp.model


import com.xl.composemultiplatformapp.data.ChatMessage
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.replay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xl.aibox.Platform
import org.xl.aibox.ai.DeepSeekUtils
import org.xl.aibox.getPlatform


/**
 * @Author : wyl
 * @Date : 2023/3/5
 * Desc :
 */
object MainViewModel : ReduxViewModel() {
    private val platform: Platform = getPlatform()

    fun chat(chatMessages: List<ChatMessage>, onMessageSend: (ChatMessage) -> Unit) {
        MainScope().launch() {
            withContext(Dispatchers.IO) {
                val result = DeepSeekUtils.callWithMessageStream(chatMessages)
                try {
                    DeepSeekUtils.streamCallWithMessage(result,onMessageSend)
                } catch (e: Exception) {
                    onMessageSend.invoke(ChatMessage(e.message.toString(), "",false))
                }
            }
        }
    }


    fun chatTest(chatMessages: List<ChatMessage>, onMessageSend: (ChatMessage) -> Unit) {
        MainScope().launch() {
            withContext(Dispatchers.IO) {

                 DeepSeekUtils.tett(onMessageSend)
            }
        }
    }

}