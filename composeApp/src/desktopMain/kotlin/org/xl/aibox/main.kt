package org.xl.aibox

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AIBox",
        state = rememberWindowState(size = DpSize(1200.dp, 900.dp))
    ) {
//        testApp()

        App()
    }
}

@Composable
fun testApp() {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val currentDisplayText = remember { mutableStateMapOf<Int, String>() }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var aiResponse = "这是AI的回复内容，演示流式输出效果 "
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = rememberLazyListState()
        ) {
            itemsIndexed(messages) { index, message ->
                ChatBubble(
                    message = message,
                    displayText = currentDisplayText[index] ?: "",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        var inputText by remember { mutableStateOf("") }
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f)
            )
            Button({
                if (inputText.isNotBlank()) {
                    val userMsg = ChatMessage(inputText, true)
                    messages.add(userMsg)
                    currentDisplayText[messages.lastIndex] = inputText

                    scope.launch {

                        messages.add(ChatMessage("", false))
                        aiResponse.forEachIndexed { i, c ->
                            currentDisplayText[messages.lastIndex] = aiResponse.take(i + 1)
                            delay(100)
                        }

                        aiResponse = aiResponse + "23121332"
                        currentDisplayText[messages.lastIndex] = aiResponse
                    }
                    inputText = ""
                }
            }) {
                Text("发送")
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    displayText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val backgroundColor = if (message.isUser) Color.Blue else Color.LightGray
        val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

        Box(
            modifier = Modifier
                .align(alignment)
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = displayText,
                color = Color.White,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}

data class ChatMessage(val content: String, val isUser: Boolean)

