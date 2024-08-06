plugins {
    alias(libs.plugins.android.application)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("/home/megatron/Projects/eas/eas-key.keystore")
            storePassword = "ongeas"
            keyAlias = "eas"
            keyPassword = "ongeas"
        }
        create("release") {
            storeFile = file("/home/megatron/Projects/eas/eas-key.keystore")
            storePassword = "ongeas"
            keyAlias = "eas"
            keyPassword = "ongeas"
        }
    }
    namespace = "com.eas.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eas.app"
        minSdk = 21
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}