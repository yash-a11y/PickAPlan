plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

repositories {
    mavenCentral()
}

android {
    namespace = "com.example.pickaplan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pickaplan"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation ("com.google.android.material:material:1.9.0") // Ensure you have this line
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4") // Optional if you're using ConstraintLayout
    // Retrofit core
    implementation("com.squareup.retrofit2:retrofit:2.9.0")


    //firebaseAuth

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Add the dependency for Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

//    // Add the dependency for Google Play services
//    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //

    // Converter for JSON (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // Razorpay SDK
    implementation("com.razorpay:checkout:1.6.33")
}