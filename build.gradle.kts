import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.dokka") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "fr.ftnl.azodox"
val compileVersion: String = "1.0.0"
var mainClassName: String = "${group}.Main"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-alpha.17"){ exclude("opus-java") }
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.github.minndevelopment:jda-ktx:03b07e7")
    implementation("io.github.reactivecircus.cache4k:cache4k:0.7.0")
    /*
     * Logger dependencies
     */
    implementation("ch.qos.logback", "logback-classic", "1.0.9")
    implementation("ch.qos.logback", "logback-core", "1.0.9")
    implementation("org.slf4j", "slf4j-api", "1.7.2")
    implementation("org.reflections:reflections:0.10.2")



    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf(
        "-Xjvm-default=all",  // use default methods in interfaces
        "-Xlambdas=indy"      // use invoke-dynamic lambdas instead of synthetic classes
    )
}



tasks.withType<ShadowJar> {
    archiveBaseName.set("AzodoxBot")
    archiveClassifier.set("")
    archiveVersion.set(compileVersion)
}


tasks.withType<org.gradle.jvm.tasks.Jar> {
    manifest {
        attributes["Implementation-Title"] = "TranslateBot"
        attributes["Implementation-Version"] = compileVersion
        attributes["Main-Class"] = mainClassName
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokkaHtml"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(buildDir.resolve("dokkaJavadoc"))
}
