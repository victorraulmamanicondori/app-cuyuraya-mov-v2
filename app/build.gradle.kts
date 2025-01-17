plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.eas.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eas.app"
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
            applicationIdSuffix = ".eas_app"
            versionNameSuffix = "v1.0.0"
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
    implementation(libs.viewPager2)
    implementation(libs.recyclerview)
    implementation(libs.recyclerviewSelection)
    implementation(libs.poi) {
        exclude(group = "org.apache.xmlbeans", module = "xmlbeans")
    }
    implementation(libs.poiOoxmlSchemas) {
        exclude(group = "org.apache.xmlbeans", module = "xmlbeans")
    }
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}