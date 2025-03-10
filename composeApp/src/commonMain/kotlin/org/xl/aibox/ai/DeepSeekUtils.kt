package org.xl.aibox.ai

import com.alibaba.dashscope.aigc.generation.Generation
import com.alibaba.dashscope.aigc.generation.GenerationParam
import com.alibaba.dashscope.aigc.generation.GenerationResult
import com.alibaba.dashscope.common.Message
import com.alibaba.dashscope.common.Role
import com.xl.composemultiplatformapp.data.ChatMessage
import io.reactivex.Flowable
import kotlinx.coroutines.delay
import org.xl.aibox.KEY


object DeepSeekUtils {

    suspend fun callWithMessage(chatMessages: List<ChatMessage>): GenerationResult {
        val messages = chatMessages.map { chatMessage ->
            Message.builder()
                .role(if (chatMessage.isUser) Role.USER.value else Role.ASSISTANT.value)
                .content(chatMessage.message)
                .build()
        }

        val gen = Generation()

        val param = GenerationParam.builder()
            .apiKey(KEY)
            .model("deepseek-r1")
            .messages(messages)
            .resultFormat(GenerationParam.ResultFormat.MESSAGE)
            .incrementalOutput(false)
            .build()

        return gen.call(param)
    }


    suspend fun callWithMessageStream(chatMessages: List<ChatMessage>): Flowable<GenerationResult> {
        val messages = chatMessages.map { chatMessage ->
            Message.builder()
                .role(if (chatMessage.isUser) Role.USER.value else Role.ASSISTANT.value)
                .content(chatMessage.message)
                .build()
        }

        val gen = Generation()

        val param = GenerationParam.builder()
            .apiKey(KEY)
            .model("deepseek-r1")
            .messages(messages)
            .resultFormat(GenerationParam.ResultFormat.MESSAGE)
            .incrementalOutput(true)
            .build()

        return gen.streamCall(param)
    }


    suspend fun streamCallWithMessage(result: Flowable<GenerationResult>,onMessageSend: (ChatMessage) -> Unit) {
        result.blockingForEach { message: GenerationResult -> handleGenerationResult(message,onMessageSend) }
    }


    val reasoningContent: StringBuilder = StringBuilder()

    val finalContent: StringBuilder = StringBuilder()

    var isFirstPrint: Boolean = true

    private fun handleGenerationResult(message: GenerationResult,onMessageSend: (ChatMessage) -> Unit) {
        var reasoning = message.output.choices[0].message.reasoningContent
        var content = message.output.choices[0].message.content

        if (!reasoning.isEmpty()) {
            reasoningContent.append(reasoning)
            if (isFirstPrint) {
                println("====================思考过程====================")
                isFirstPrint = false
                reasoning = "\n"+reasoning
            }
            onMessageSend.invoke(ChatMessage(reasoning,false))
            print(reasoning)
        }

        if (!content.isEmpty()) {
            finalContent.append(content)
            if (!isFirstPrint) {
                println("\n====================完整回复====================")
                isFirstPrint = true
                content = "\n\n" + content
            }
            onMessageSend.invoke(ChatMessage(content,false))
            print(content)
        }
    }

    private fun handleGenerationResultTest(reasoning:String,content:String ,onMessageSend: (ChatMessage) -> Unit) {
        if (!reasoning.isEmpty()) {
            reasoningContent.append(reasoning)
            if (isFirstPrint) {
                println("====================思考过程====================")
                isFirstPrint = false
            }
            onMessageSend.invoke(ChatMessage(reasoning,false))
            print(reasoning)
        }

        if (!content.isEmpty()) {
            finalContent.append(content)
            if (!isFirstPrint) {
                println("\n====================完整回复====================")
                isFirstPrint = true
            }
            onMessageSend.invoke(ChatMessage(content,false))
            print(content)
        }
    }


    suspend fun tett(onMessageSend: (ChatMessage) -> Unit){
        repeat(3) {
            handleGenerationResultTest(
                "思考中思考中" + System.currentTimeMillis().toString(),
                "这是回复这是回复" + System.currentTimeMillis().toString(),

                onMessageSend

            )
            delay(500)
        }

    }
}