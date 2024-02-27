package com.example.locationapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, graph = hshs)

}

sealed class Screen(val route:String)