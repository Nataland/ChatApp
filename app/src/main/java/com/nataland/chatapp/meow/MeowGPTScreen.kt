package com.nataland.chatapp.meow

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nataland.chatapp.R
import com.nataland.chatapp.picker.StoreCat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeowGPTScreen(
    viewModel: MeowGPTViewModel = hiltViewModel(),
    onAvatarClick: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val dataStore = remember { StoreCat(context) }
    val storedCat = dataStore.getCat.collectAsState(null).value
    LaunchedEffect(storedCat) {
        Log.d("Nataland", storedCat?.name.orEmpty())
        storedCat?.let { viewModel.setCat(it) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = onAvatarClick,
                    ) {
                        Icon(
                            painterResource(state.cat.pictureResId),
                            state.cat.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            tint = Color.Unspecified,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .imePadding()
                .padding(innerPadding)
        ) {
            val lazyListState = rememberLazyListState()
            var shouldScroll by remember { mutableStateOf(false) }
            LaunchedEffect(state.messages.size, shouldScroll) {
                if (state.messages.isNotEmpty() || shouldScroll) {
                    lazyListState.scrollToItem(state.messages.size + 1)
                    shouldScroll = false
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                state = lazyListState,
                modifier = Modifier
                    .weight(1f),
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    Text(
                        state.catAction,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(24.dp))
                }
                items(state.messages) {
                    ChatBubble(it)
                    Spacer(Modifier.height(16.dp))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var input by remember { mutableStateOf("") }

                TextField(
                    input,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    onValueChange = {
                        input = it
                    },
                    colors = TextFieldDefaults.colors().copy(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
                )
                IconButton(
                    enabled = input.isNotEmpty(),
                    onClick = {
                        viewModel.sendMessage(input)
                        input = ""
                    }
                ) {
                    Icon(
                        painterResource(android.R.drawable.ic_menu_send),
                        contentDescription = stringResource(R.string.send_button)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                start = when (message.arrangement) {
                    ChatArrangement.Start -> 16.dp
                    ChatArrangement.End -> 48.dp
                },
                end = when (message.arrangement) {
                    ChatArrangement.Start -> 48.dp
                    ChatArrangement.End -> 16.dp
                },
            ),
        horizontalArrangement = when (message.arrangement) {
            ChatArrangement.Start -> Arrangement.Start
            ChatArrangement.End -> Arrangement.End
        },
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(
                    when (message) {
                        is Message.Error -> MaterialTheme.colorScheme.errorContainer
                        is Message.Self -> MaterialTheme.colorScheme.primaryContainer
                        is Message.Server -> MaterialTheme.colorScheme.secondaryContainer
                    },
                    RoundedCornerShape(24.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.content,
                color = if (message is Message.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}
