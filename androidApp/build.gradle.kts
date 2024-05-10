plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version("2.0.1")
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
        buildConfigField("String", "OPENWEATHER_API_KEY", "\"" + System.getenv("OPENWEATHER_API_KEY") + "\"")
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
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    val composeVersion = "1.6.7"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    val navigation = "androidx.navigation:navigation-compose:2.5.2"
    implementation(navigation)
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.6-alpha")
    implementation(Libs.Hilt.base)
    annotationProcessor(Libs.Hilt.compiler)
    implementation(Libs.retrofit)
    implementation(Libs.retrofitAdapter)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.4.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
}