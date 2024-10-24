package com.example.myapplication

import TutorialScreen
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.chat.ChatScreen
import com.example.myapplication.viewmodel.LocationViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContent {
            val viewModel: LocationViewModel = viewModel()

            navController = rememberNavController()
            val startdest = if (FirebaseAuth.getInstance().currentUser!=null) "home" else "signup"
            NavHost(navController = navController, startDestination =  startdest, builder = {
                composable("login"){
                    AuthScreen( navController = navController,
                        isLoginScreen = true,
                        onAuthAction =  { email, password ->
                        signIn(
                            email,
                            password
                        )
                    } )
                }
                composable("signup"){
                    AuthScreen( navController = navController,
                        isLoginScreen = false,
                        onAuthAction = { email, password -> signUp(email, password) })
                }
                composable("home"){
                    home(navController = navController, user = auth.currentUser?.email.toString(), viewModel)
                }
                composable("chat/{chatId}"){
                    val chatId = it.arguments?.getString("chatId") ?: "chat1"
                    Log.i("Chat id", chatId)
                    ChatScreen(chatId = chatId , auth.currentUser?.email.toString())
                }
                composable("tutorial"){
                    TutorialScreen(navController)
                }
            })
        }
        auth = FirebaseAuth.getInstance()

    }

    private fun signIn(email: String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                navController.navigate("home")
            } else {

            }
        }
    }
    private  fun signUp(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                navController.navigate("tutorial")
            } else {

            }
        }
    }

}