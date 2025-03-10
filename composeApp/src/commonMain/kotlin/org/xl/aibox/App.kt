package org.xl.aibox

import CustomTextField
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import com.xl.composemultiplatformapp.data.ChatMessage
import com.xl.composemultiplatformapp.model.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
@Preview
fun App() {
    var isSidebarExpanded by remember { mutableStateOf(false) }
    val sidebarWidth = if (isSidebarExpanded) 200.dp else 60.dp

    // Add a state to track the selected navigation item
    var selectedItem by remember { mutableStateOf<String?>(null) }

    // Update the color scheme to match the image
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFFB0E0E6), // 浅蓝色
            background = Color(0xFFFFE4B5), // 浅橙色背景
            surface = Color(0xFFFFFFFF) // 白色表面
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar
                Surface(
                    modifier = Modifier.width(sidebarWidth).fillMaxHeight(),
                    color = Color(0xFFB0E0E6) // 侧边栏颜色
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Logo area
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (isSidebarExpanded) {
                                Text(
                                    text = "deepseek",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { isSidebarExpanded = !isSidebarExpanded }) {
                                Icon(
                                    imageVector = if (isSidebarExpanded) Icons.AutoMirrored.Filled.Send else Icons.AutoMirrored.Filled.List,
                                    contentDescription = if (isSidebarExpanded) "收起" else "展开",
                                    tint = Color.White
                                )
                            }
                        }

                        // Navigation list
                        Column(
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                        ) {
                            if (isSidebarExpanded) {
                                val items = listOf(
                                    "新手上路",
                                    "用户如何获取访问权限",
                                    "新手天天向上",
                                    "新增汽车写作优化任务",
                                    "公文写作与写作技巧",
                                    "机械Word文档编辑",
                                    "人教育与写作基础"
                                )
                                items.forEach { item ->
                                    NavigationItem(
                                        text = item,
                                        selected = item == selectedItem,
                                        onClick = { selectedItem = item }
                                    )
                                }
                            }
                        }

                        // Bottom user info
                        if (isSidebarExpanded) {
                            Surface(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                color = Color(0xFF800080), // 浅绿色用于用户信息
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "个人信息",
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Main content area
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top title bar
                    TopAppBar(
                        backgroundColor = Color(0xFFC1FFC1), // 紫色用于顶部
                        elevation = 0.dp
                    ) {
                        Text(
                            text = "我是 DeepSeek，很高兴见到你！",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    // Middle content area
                    var messages = remember { mutableStateListOf<ChatMessage>() }
                    val currentDisplayText = remember { mutableStateMapOf<Int, String>() }
                    val listState = rememberLazyListState()
                    val coroutineScope = rememberCoroutineScope()
                    var aiResponse = ""
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFFFFE4B5)) // 浅橙色用于中间区域
                            .padding(16.dp)
                    ) {
                        itemsIndexed(messages) { index, message ->
                            ChatBubble(
                                message = message,
                                displayText = currentDisplayText[index] ?: "",
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    // Scroll to the last message when a new message is added
                    LaunchedEffect(messages.size) {
                        coroutineScope.launch {
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }

                    // Bottom input area
                    BottomInputArea { newMessage ->

                        messages.add(newMessage)
                        currentDisplayText[messages.lastIndex] = newMessage.message


                        aiResponse = ""
                        messages.add(ChatMessage("思考过程", false))
                        MainViewModel.chat(messages) { newMessage ->
                            currentDisplayText[messages.lastIndex] = ""
                            aiResponse = aiResponse + newMessage.message
                            coroutineScope.launch {
                                currentDisplayText[messages.lastIndex] = aiResponse
                                delay(100)
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(if (selected) Color(0xFF800080) else Color.Transparent) // 紫色选中
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun BottomInputArea(onMessageSend: (ChatMessage) -> Unit) {
    var textValue by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFB0E0E6), // 匹配侧边栏颜色
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                textValue = textValue,
                onTextValueChange = { newValue ->
                    textValue = newValue
                },
                onEnterClick = {
                    if (textValue.isNotBlank()) {
                        onMessageSend(ChatMessage(textValue, true))
                        textValue = ""
                    }
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (textValue.isNotBlank()) {
                        onMessageSend(ChatMessage(textValue, false))
                        textValue = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "发送",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatMessage(chatMessage: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (chatMessage.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth(align = if (chatMessage.isUser) Alignment.End else Alignment.Start)
                .padding(vertical = 4.dp)
                .background(
                    color = if (chatMessage.isUser) Color(0xFFC1FFC1) else Color(0xFFB0E0E6),
                    shape = RoundedCornerShape(8.dp)
                ).padding(16.dp),
            contentAlignment = Alignment.CenterStart // 始终左对齐，因为Box的对齐由Row控制
        ) {
            Text(
                text = chatMessage.message,
                color = Color.Black,
                textAlign = TextAlign.Start // 始终左对齐
            )
        }
    }
}
@Composable
fun ChatBubble(
    message: ChatMessage,
    displayText: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        val backgroundColor = if (message.isUser) Color(0xFFC1FFC1) else Color(0xFFB0E0E6)
        val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart

        Box(modifier = Modifier
                .align(alignment)
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = displayText,
                color = Color.Black,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}