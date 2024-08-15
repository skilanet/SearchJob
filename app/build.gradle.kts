plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-kapt")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "HH_ACCESS_TOKEN",
            value = "\"${developProperties.hhAccessToken}\""
        )
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
        buildConfig = true
        dataBinding = true
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)
    implementation(libs.androidX.activity)
    implementation(libs.androidX.activity.ktx)
    implementation(libs.androidX.lifecycle.ktx)
    implementation(libs.androidX.fragment)
    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)
    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion
    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion
    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiller)
    // Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiller)
    // Koin
    implementation(libs.koin)
    // Paging
    implementation(libs.paging)
}
