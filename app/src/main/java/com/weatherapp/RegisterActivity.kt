package com.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.weatherapp.ui.theme.MapPage.WeatherAppTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatedPassword by rememberSaveable { mutableStateOf("") }
    val activity = LocalActivity.current as Activity
    Column(
        modifier = modifier.fillMaxWidth(fraction = 0.9f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Tela de Registro",
            fontSize = 24.sp
        )
        OutlinedTextField(
            value = name,
            label = { Text(text = "Digite seu nome") },
            modifier = modifier,
            onValueChange = {
                name = it
            }
        )
        OutlinedTextField(
            value = email,
            label = { Text(text = "Digite seu e-mail") },
            modifier = modifier,
            onValueChange = {
                email = it
            }
        )
        OutlinedTextField(
            value = password,
            label = { Text(text = "Digite sua senha") },
            modifier = modifier,
            onValueChange = {
                password = it
            },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = repeatedPassword,
            label = { Text(text = "Repita sua senha") },
            modifier = modifier,
            onValueChange = {
                repeatedPassword = it
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Row(modifier = modifier.padding(12.dp).fillMaxSize(), horizontalArrangement = Arrangement.Center) {
            Button( onClick = {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity,
                                "Registro OK!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(activity,
                                "Registro FALHOU!", Toast.LENGTH_LONG).show()
                        }
                    }
            },
                enabled = (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty())&&(password == repeatedPassword)) {
                Text("Registrar")
            }
            Button(
                onClick = { email = ""; password = "";name="";password="";repeatedPassword=""}
            ) {
                Text("Limpar")
            }
        }
    }
}