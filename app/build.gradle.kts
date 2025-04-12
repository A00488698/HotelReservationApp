import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.hotelreservationapp"
    compileSdk = 35

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "com.example.hotelreservationapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // -------------------------------
    // 常用核心依赖
    // -------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // -------------------------------
    // Compose BOM（统一管理 Compose 相关版本）
    // -------------------------------
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // -------------------------------
    // Compose 基础UI库
    // -------------------------------
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // -------------------------------
    // Material3：只保留一个引用，升级到1.3.1（使用版本配置中的 material3 = "1.3.1"）
    // -------------------------------
    implementation(libs.androidx.material3)

    // -------------------------------
    // 其他依赖（如导航、AppCompat、Retrofit等）
    // -------------------------------
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation(libs.datetime)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // 第三方日期对话框库（如你需要的话，可以保留或移除，和官方 DatePicker 不冲突）
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // -------------------------------
    // 测试库
    // -------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
