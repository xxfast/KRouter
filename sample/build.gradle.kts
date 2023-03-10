@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("kotlin-parcelize")
  kotlin("plugin.serialization")
  id("com.codingfeline.buildkonfig")
}

kotlin {
  android()

  jvm("desktop") {
    compilations.all {
      kotlinOptions {
        jvmTarget = "15"
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":krouter"))

        implementation(compose.desktop.currentOs)
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
        implementation(compose.preview)

        implementation("com.alialbaali.kamel:kamel-image:0.4.1")
        implementation("com.arkivanov.decompose:decompose:1.0.0")
        implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0")
        implementation("io.ktor:ktor-client-core:2.0.3")
        implementation("io.ktor:ktor-client-cio:2.0.3")
        implementation("io.ktor:ktor-client-content-negotiation:2.0.3")
        implementation("io.ktor:ktor-client-logging:2.0.3")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.3")
        implementation("app.cash.molecule:molecule-runtime:0.7.1")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}

android {
  namespace = "io.github.xxfast.krouter.sample"
  compileSdk = 33
  defaultConfig {
    minSdk = 24
    targetSdk = 33
  }
}

buildkonfig {
  packageName = "io.github.xxfast.krouter.sample"

  defaultConfigs {
    val apiKey: String = gradleLocalProperties(rootDir).getProperty("apiKey")

    require(apiKey.isNotEmpty()) {
      "Register your api key from thenewsapi.com and place it in local.properties as `apiKey`"
    }

    buildConfigField(STRING, "API_KEY", apiKey)
  }
}
