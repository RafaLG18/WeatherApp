package com.weatherapp

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class WeatherApp : Application() {
    val FLAGS = FLAG_ACTIVITY_SINGLE_TOP or // Não cria atividade se no topo
            FLAG_ACTIVITY_NEW_TASK or // Cria nova tarefa
            FLAG_ACTIVITY_CLEAR_TASK // Limpa o backstack
    override fun onCreate() {
        super.onCreate()
        Firebase.auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) { goToMain()
            } else { goToLogin() }
        }
    }
    private fun goToMain() {
        this.startActivity( Intent(this, MainActivity::class.java).setFlags(FLAGS) )
    }
    private fun goToLogin() {
        this.startActivity( Intent(this, LoginActivity::class.java).setFlags(FLAGS) )
    }
}
