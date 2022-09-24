import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version "1.7.10"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.bybackfish"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

    implementation("fr.minuskube.inv:smart-invs:1.2.7")
    implementation("de.tr7zw:item-nbt-api-plugin:2.10.0")
    implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

val autoRelocate by tasks.register<ConfigureShadowRelocation>("configureShadowRelocation", ConfigureShadowRelocation::class) {
    target = tasks.getByName("shadowJar") as ShadowJar?
    val packageName = "${project.group}.${project.name.toLowerCase()}"
    prefix = "$packageName.shaded"
}
tasks {
    val javaVersion = JavaVersion.VERSION_17

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
    }

    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    shadowJar {
        archiveClassifier.set("")
        project.configurations.implementation.get().isCanBeResolved = true
        configurations = listOf(project.configurations.implementation.get())
     //   dependsOn(autoRelocate)
   //     minimize()

        destinationDirectory.set(file("C:\\Users\\Maik\\Desktop\\WynnBoxServer\\plugins"))
    }
    build {
        dependsOn(shadowJar)
    }
}