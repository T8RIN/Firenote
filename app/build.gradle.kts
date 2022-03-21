plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "ru.tech.firenote"
    compileSdk = 32

    defaultConfig {
        applicationId = "ru.tech.firenote"
        minSdk = 21
        targetSdk = 32
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha05"
    }
    packagingOptions {
        resources {
            excludes += ("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    //Android Essentials
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0-alpha03")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.window:window:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.38.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    implementation("androidx.lifecycle:lifecycle-service:2.4.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    //Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    //Compose
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui:1.2.0-alpha05")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0-alpha05")
    implementation("androidx.compose.material3:material3:1.0.0-alpha07")
    implementation("androidx.compose.material:material:1.2.0-alpha05")
    implementation("androidx.compose.material:material-icons-core:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0-alpha04")
    implementation("androidx.navigation:navigation-compose:2.5.0-alpha03")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")

    //Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.2-alpha")
    implementation("com.google.accompanist:accompanist-insets:0.24.2-alpha")
    implementation("com.google.accompanist:accompanist-flowlayout:0.24.2-alpha")

    //Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.0-alpha05")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0-alpha05")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.2.0-alpha05")

    //Coil
    implementation("io.coil-kt:coil:2.0.0-rc01")
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")

    //Firebase
    implementation("com.google.firebase:firebase-auth-ktx:21.0.2")
    implementation("com.google.android.gms:play-services-auth:20.1.0")
    implementation("com.google.firebase:firebase-database-ktx:20.0.4")
    implementation("com.google.firebase:firebase-storage-ktx:20.0.1")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.2.9")
    implementation("com.google.firebase:firebase-analytics-ktx:20.1.1")


//    //DataStore
//    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    implementation("androidx.datastore:datastore-preferences-core:1.0.0")
//    //Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.okhttp3:okhttp:4.9.3")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
//
//    Koin
//    implementation("io.insert-koin:koin-android:3.1.5")
//    implementation("io.insert-koin:koin-androidx-workmanager:3.1.5")
//    implementation("io.insert-koin:koin-androidx-navigation:3.1.5")
//    implementation("io.insert-koin:koin-androidx-compose:3.1.5")
//
//    //WorkManager
//    implementation("androidx.work:work-runtime-ktx:2.7.1")
//    androidTestImplementation("androidx.work:work-testing:2.7.1")
//    implementation("androidx.work:work-multiprocess:2.7.1")

}