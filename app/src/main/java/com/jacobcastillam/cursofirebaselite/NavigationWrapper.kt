package com.jacobcastillam.cursofirebaselite

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jacobcastillam.cursofirebaselite.presentation.home.HomeScreen
import com.jacobcastillam.cursofirebaselite.presentation.initial.InitialScreen
import com.jacobcastillam.cursofirebaselite.presentation.login.LoginScreen
import com.jacobcastillam.cursofirebaselite.presentation.singup.SingUpScreen


@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth,

) {

    NavHost(navController = navHostController, startDestination = "initial") {
        composable("initial") {
            InitialScreen(
                navigateToLogin = {navHostController.navigate("login")},
                navigateToSingUp = {navHostController.navigate("singUp")}
            )
        }
        composable("login") {
            LoginScreen(auth){navHostController.navigate("home")}
        }
        composable("SingUp") {
            SingUpScreen(auth)
        }
        composable("home"){
            HomeScreen()
        }
    }
}