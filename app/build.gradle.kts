plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mrm.tirewise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mrm.tirewise"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        mlModelBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    testImplementation("junit:junit:4.13.2")
//    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    androidTestImplementation("com.google.truth:truth::0.40") // Latest version 1.1.3


    //for unit test
    testImplementation("com.google.truth:truth:1.1.3")
    //for integration test
    androidTestImplementation("com.google.truth:truth:1.1.3")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // See https://firebase.google.com/docs/android/setup#available-libraries
    // For example, add the dependencies for Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Credential manager
    implementation("com.google.android.libraries.identity.googleid:googleid:<latest version>")


    // Newly Added ---------------------------------

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Firebase UI for sign in
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")

    // More newly added
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // Camera X Dependencies
    val camerax_version = "1.3.1"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")

    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation("androidx.concurrent:concurrent-futures-ktx:1.1.0")

    // Image Cropper
    implementation("com.vanniktech:android-image-cropper:4.5.0")

    // Coil library for images
    implementation("io.coil-kt:coil-compose:2.6.0")

    //for date and time
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.1.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.1.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:clock:1.1.0")

    // collect as state with lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // For lottie animation for the splash screen
     val lottieVersion = "6.4.0"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    // Preference DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Google Acompanist Permission
    implementation("com.google.accompanist:accompanist-permissions:0.31.0-alpha")

// Needed for createAndroidComposeRule, but not createComposeRule:
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.5")

//    androidTestImplementation("io.mockk:mockk:1.12.2")
//    implementation ("org.mockito:mockito-core:5.3.1")
//    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")


    testImplementation ("org.mockito:mockito-core:3.10.0")
    androidTestImplementation ("org.mockito:mockito-android:3.10.0")

    testImplementation("net.bytebuddy:byte-buddy:1.12.9")


    //testing navigations:
    val nav_version = "2.7.7"

    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))
    androidTestImplementation("androidx.navigation:navigation-testing:2.6.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
}