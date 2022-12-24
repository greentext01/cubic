import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "dev.oliveman"
version = "0.0.1"

val mojangMapped = System.getProperty("mojang-mapped") != null

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version "1.7.21"
    id("xyz.xenondevs.specialsource-gradle-plugin") version "1.0.0"
    id("xyz.xenondevs.string-remapper-gradle-plugin") version "1.0.0"
    id("xyz.xenondevs.nova.nova-gradle-plugin") version libs.versions.nova
}

repositories {
    mavenCentral()
    maven("https://repo.xenondevs.xyz/releases")
    mavenLocal { content { includeGroup("org.spigotmc") } }
}

dependencies {
    implementation(libs.nova)
    implementation(variantOf(libs.spigot) { classifier("remapped-mojang") })
}

addon {
    id.set(project.name)
    name.set(project.name.capitalized())
    version.set(project.version.toString())
    novaVersion.set(libs.versions.nova)
    main.set("dev.oliveman.Cubic")
    
    authors.add("Oliveman")
    spigotResourceId.set(0)
}

spigotRemap {
    spigotVersion.set(libs.versions.spigot.get().substringBefore('-'))
    sourceJarTask.set(tasks.jar)
    spigotJarClassifier.set("")
}

remapStrings {
    remapGoal.set(if (mojangMapped) "mojang" else "spigot")
    spigotVersion.set(libs.versions.spigot.get())
    classes.set(listOf(
        // Put your classes to string-remap here
    ))
}

tasks {
    register<Copy>("addonJar") {
        group = "build"
        dependsOn("addon", if (mojangMapped) "jar" else "remapObfToSpigot")
        
        from(File(File(project.buildDir, "libs"), "${project.name.capitalized()}-${project.version}.jar"))
        into(System.getProperty("outDir")?.let(::File) ?: project.buildDir)
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}