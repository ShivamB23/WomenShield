plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.project.womensafety"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.womensafety"
        minSdk = 21
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat.v161)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.appcompat.v131)
    implementation(libs.constraintlayout)
    implementation(libs.material.v140)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit.v412)
    androidTestImplementation(libs.junit.v113)
    androidTestImplementation(libs.espresso.core.v340)
    implementation(libs.lottie)
    implementation(libs.cardview)
    implementation(libs.play.services.location)
    implementation(libs.gridlayout)
    implementation(libs.glide.v4110)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.material)

    implementation(libs.cardview)

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.material.v1110)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.messaging)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.v115)
    androidTestImplementation(libs.espresso.core.v351)
    implementation(libs.volley)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}