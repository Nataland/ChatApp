package com.nataland.chatapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.nataland.chatapp.meow.MeowGPTScreen
import com.nataland.chatapp.picker.CatPickerScreen
import kotlinx.serialization.Serializable

@Serializable
object MeowGPT

@Serializable
data class CatPicker(val message: String)

@Composable
fun ChatNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigation: ChatNavigation = remember(navController) {
        ChatNavigation(navController)
    }
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        graph = navController.createGraph(startDestination = MeowGPT) {
            composable<MeowGPT> {
                MeowGPTScreen {
                    navigation.navigateToCatPicker()
                }
            }
            composable<CatPicker> { backEntry ->
//                val parentEntry = remember { navController. getBackStackEntry(MeowGPT) }
//                val viewModel = hiltViewModel<CatPickerViewModel>(parentEntry)
                CatPickerScreen((backEntry.toRoute() as CatPicker).message) {
                    navController.popBackStack()
                }
            }
        }
    )
}

data class ChatNavigation(val navController: NavHostController) {
    fun navigateToCatPicker() {
        navController.navigate(CatPicker("Select a cat")) {
            popUpTo(MeowGPT)
        }
    }
}
