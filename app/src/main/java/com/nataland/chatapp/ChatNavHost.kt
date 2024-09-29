package com.nataland.chatapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.nataland.chatapp.meow.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object MeowGPT

@Composable
fun ChatNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        graph = navController.createGraph(startDestination = MeowGPT) {
            composable<MeowGPT> { HomeScreen() }
        }
    )
}