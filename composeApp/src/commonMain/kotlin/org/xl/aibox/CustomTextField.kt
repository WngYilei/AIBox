import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomTextField(
    textValue: String,
    onTextValueChange: (String) -> Unit,
    onEnterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    // 光标闪烁动画
    val cursorAlpha = remember {
        Animatable(1f)
    }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            while (true) {
                cursorAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing),
                )
                cursorAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing),
                )
            }
        } else {
            cursorAlpha.snapTo(1f)
        }
    }

    TextField(
        value = textValue,
        onValueChange = { newText ->
            onTextValueChange(newText)
        },
        modifier = modifier
            .background(
                MaterialTheme.colors.surface,
                RoundedCornerShape(8.dp)
            )
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.key == Key.Enter) {
                    println("案件俺家n")
                    onEnterClick()
                    return@onKeyEvent true
                }
                false
            }
            .focusRequester(focusRequester)
            .onFocusEvent { focusState ->
                isFocused = focusState.isFocused
            },
        placeholder = { Text("向 DeepSeek 发消息", color = Color.Gray) },
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = Color.Black,
            cursorColor = Color.Black
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onEnterClick()
            }
        ),
        singleLine = true
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
} 