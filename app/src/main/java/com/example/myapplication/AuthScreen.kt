package com.example.myapplication

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
@Composable
fun AuthScreen(
    navController: NavController,
    isLoginScreen: Boolean,
    onAuthAction: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Text
        HeaderText(isLoginScreen)

        // Description Text
        DescriptionText(isLoginScreen)

        Spacer(modifier = Modifier.padding(16.dp))

        // Email TextField
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Password TextField
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true
        )

        // Auth Button
        AuthButton(
            onClick = { onAuthAction(email, password) },
            isLoginScreen = isLoginScreen
        )

        // Google Auth Option
        Text("or ${if (isLoginScreen) "Log In" else "Sign Up"} with", modifier = Modifier.padding(top = 12.dp), color = colorResource(R.color.gray))
        IconButton(onClick = {}, modifier = Modifier.padding(top = 12.dp)) {
            Image(painter = painterResource(id= R.drawable.google), contentDescription = "Add")
        }

        // Navigation Prompt
        NavigationPrompt(navController, isLoginScreen)
    }
}

@Composable
fun HeaderText(isLoginScreen: Boolean) {
    Text(
        if (isLoginScreen) "Log In" else "Sign Up",
        color = colorResource(R.color.black),
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun DescriptionText(isLoginScreen: Boolean) {
    Text(
        "${if (isLoginScreen) "Login to ${stringResource(R.string.app_name)} and continue your contribution"
        else "Sign Up to ${stringResource(R.string.app_name)} and Contribute"} to a greener Earth and economical pocket.",
        modifier = Modifier.padding(top = 16.dp),
        color = colorResource(R.color.gray),
        fontSize = 15.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
            .clip(shape = RoundedCornerShape(5.dp))
            .border(width = 1.dp, color = colorResource(R.color.forest_green), shape = RoundedCornerShape(5.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_gray),
            focusedLabelColor = colorResource(R.color.dark_green),
            focusedIndicatorColor = colorResource(R.color.dark_green),
            focusedTextColor = colorResource(R.color.dark_green),
            unfocusedContainerColor = colorResource(R.color.light_gray)
        ),
        textStyle = TextStyle(color = colorResource(R.color.black)),
    )
}


@Composable
fun AuthButton(onClick: () -> Unit, isLoginScreen: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.leaf_green),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Text(if (isLoginScreen) "Log In" else "Sign Up")
    }
}

@Composable
fun NavigationPrompt(navController: NavController, isLoginScreen: Boolean) {
    val promptText = if (isLoginScreen) "Don't have an account? Sign Up" else "Already have an account? Log In"
    val navigateTo = if (isLoginScreen) "signup" else "login"
    Button(
        onClick = { navController.navigate(navigateTo) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(promptText)
    }
}
