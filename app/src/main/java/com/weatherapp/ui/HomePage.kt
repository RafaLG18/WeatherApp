package com.weatherapp.HomePage

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun HomePage1(modifier: Modifier = Modifier.Companion){
    val activity = LocalActivity.current as Activity

    Column(
        modifier = modifier.fillMaxWidth(fraction = 0.9f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text(
            text = "Bem-vindo(a)!",
            fontSize = 24.sp
        )
        Row(
            modifier = modifier.padding(12.dp).fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                activity.finish()
            }) {
                Text("Exit")
            }

        }
    }
}