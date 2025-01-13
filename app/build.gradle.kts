val vc = 40001
val isBeta = true
val vn4 = "1.0.1."
val vn = vn4 + vc.toString() + if (isBeta) ".dev" else ""


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt") version "2.0.0"
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "kg.edu.yjut.enhancenoticehyperos"
    compileSdk = 34

    defaultConfig {
        applicationId = "kg.edu.yjut.enhancenoticehyperos"
        minSdk = 26
        targetSdk = 35
        versionCode = vc
        versionName = vn

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("dp") {
            storeFile = file("../keystore/keystore.jks")
            storePassword = "wxdqwe123"
            keyAlias = "key0"
            keyPassword = "wxdqwe123"
        }
        create("dp2") {
            storeFile = file("../keystore/keystore.jks")
            storePassword = "wxdqwe123"
            keyAlias = "key0"
            keyPassword = "wxdqwe123"
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("dp2")
        }
        debug{
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("dp2")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("dp2")

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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    val room_version = "2.6.1"

    implementation(libs.com.github.promeg.tinypinyin19) // TinyPinyin核心包，



    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    // To use Kotlin Symbol Processing (KSP)
    kapt("androidx.room:room-compiler:$room_version")
    implementation("com.belerweb:pinyin4j:2.5.0")
    //ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")
    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")
    // optional - Guavasupport for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.19.0")
    implementation("com.google.code.gson:gson:2.10.1")
}