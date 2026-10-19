import java.util.Properties
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
// Signing: keystore.properties (local) -> VELVI_* env vars (CI) -> debug key fallback
val ks = Properties().apply { val f = rootProject.file("keystore.properties"); if (f.exists()) f.inputStream().use { load(it) } }
fun cfg(prop: String, env: String): String? = ks.getProperty(prop) ?: System.getenv(env)?.takeIf { it.isNotBlank() }
val ksFile = cfg("storeFile", "VELVI_KEYSTORE_FILE")?.let { rootProject.file(it) }?.takeIf { it.exists() && it.length() > 0 }
val hasReleaseKey = ksFile != null && cfg("storePassword", "VELVI_STORE_PASSWORD") != null && cfg("keyAlias", "VELVI_KEY_ALIAS") != null

android {
    namespace = "com.velvi.probazametki"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.velvi.probazametki"
        minSdk = 21
        targetSdk = 35
        versionCode = ((project.findProperty("appCode") as String?) ?: "1").toInt()
        versionName = "1.0"
    }
    signingConfigs {
        if (hasReleaseKey) create("release") {
            storeFile = ksFile
            storePassword = cfg("storePassword", "VELVI_STORE_PASSWORD")
            keyAlias = cfg("keyAlias", "VELVI_KEY_ALIAS")
            keyPassword = cfg("keyPassword", "VELVI_KEY_PASSWORD") ?: storePassword
            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = if (hasReleaseKey) signingConfigs.getByName("release") else signingConfigs.getByName("debug")
        }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
println(if (hasReleaseKey) "Signing: RELEASE key" else "Signing: DEBUG key (no VELVI_* secrets / keystore.properties)")
