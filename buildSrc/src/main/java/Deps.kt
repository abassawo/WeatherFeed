object Versions {
    const val hiltVersion = "2.45"
    const val retrofit = "2.9.0"
    const val retrofitSerializationAdapter = "0.8.0"
    const val kotlinxSerizalition = "0.8.0"
}

object Libs {

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitAdapter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitSerializationAdapter}"

    object Hilt {
        val base = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
        val compiler = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"
    }
}

