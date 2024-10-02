package com.nataland.chatapp.picker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nataland.chatapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatPickerScreen(
    message: String,
    viewModel: CatPickerViewModel = hiltViewModel(),
    onBackPress: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value
    val cats = state.cats
    val selectedCat = state.selectedCat
    val context = LocalContext.current
    val dataStore = StoreCat(context)
    val scope = rememberCoroutineScope()
    val storedCat = dataStore.getCat.collectAsState(null).value
    LaunchedEffect(storedCat != null) {
        if (storedCat != null) {
            viewModel.setCat(storedCat)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (storedCat != selectedCat && selectedCat != null) {
                                scope.launch {
                                    dataStore.saveCat(selectedCat.name)
                                }
                            }
                            onBackPress()
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_arrow),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = {
                    Text(message)
                }
            )
        }
    ) { innerPadding ->

        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(24.dp),
        ) {
            items(cats) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            viewModel.setCat(it)
                        }
                ) {
                    Icon(
                        painterResource(it.pictureResId),
                        null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .then(
                                if (selectedCat == it) {
                                    Modifier.border(
                                        width = if (it == selectedCat) {
                                            4.dp
                                        } else {
                                            0.dp
                                        },
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                } else {
                                    Modifier
                                }
                            ),
                        tint = Color.Unspecified,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        it.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
