import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp") version "2.3.4"
}

android {
    namespace = "com.weatherapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.weatherapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val keyFile = project.rootProject.file("local.properties")
        val props = Properties()
        props.load(keyFile.inputStream())
        buildConfigField ("String", "WEATHER_API_KEY",
            props.getProperty("WEATHER_API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_21
        sourceCompatibility = JavaVersion.VERSION_21
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    // Google maps
    implementation("com.google.android.gms:play-services-maps:20.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    // Google maps for compose
    implementation("com.google.maps.android:maps-compose:8.3.0")
    implementation("io.coil-kt:coil-compose:2.7.0") // coil
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.9.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.work:work-runtime-ktx:2.10.0")
}