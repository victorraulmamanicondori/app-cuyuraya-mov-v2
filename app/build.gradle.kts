plugins {
    alias(libs.plugins.android.application)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("/home/miguel/LabCenter/nodejs/app-cuyuraya-mov-v2/almacenclave.jks")
            storePassword = "ONGEAS"
            keyAlias = "eas"
            keyPassword = "ONGEAS"
        }
        create("release") {
            storeFile = file("/home/megatron/Projects/eas/app-cuyuraya-mov-v2/almacenclave.jks")
            storePassword = "ONGEAS"
            keyAlias = "eas"
            keyPassword = "ONGEAS"
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
    implementation(libs.retrofit2)
    implementation(libs.converterGson)
    implementation(libs.itext7Core)
    implementation(libs.viewPager2)
    implementation(libs.recyclerview)
    implementation(libs.recyclerviewSelection)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
