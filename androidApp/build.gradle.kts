plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.lindenlabs.weatherfeed.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.lindenlabs.weatherfeed.android"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    val composeVersion = "1.3.1"
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3:1.1.0-alpha07")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    val navigation = "androidx.navigation:navigation-compose:2.5.2"
    implementation(navigation)
    implementation("io.ktor:ktor-client-android:2.2.4")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.6-alpha")

//    implementation("com.github.bumptech.glide:compose:1.0.0-beta01+")
    implementation(Libs.Hilt.base)
    annotationProcessor(Libs.Hilt.compiler)
    implementation(Libs.retrofit)
    implementation(Libs.retrofitAdapter)
}