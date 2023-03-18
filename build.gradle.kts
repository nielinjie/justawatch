import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val kotlinVersion = "1.7.20-Beta"
val serializationVersion = "1.3.3"
val ktorVersion = "2.0.3"
val logbackVersion = "1.2.11"
val kotlinWrappersVersion = "1.0.0-pre.354"
val kmongoVersion = "4.5.0"

plugins {
    kotlin("multiplatform") version "1.7.20-Beta"
    application //to run JVM part
    kotlin("plugin.serialization") version "1.7.20-Beta"
    id("io.kotest") version "0.3.8"


}
fun kotlinw(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"


group = "xyz.nietongxue"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.universablockchain.com/")

}


kotlin {
    jvm {
        withJava()
    }
    js {
        browser {
            binaries.executable()
            commonWebpackConfig{
                devServer?.proxy = mutableMapOf("/api" to "http://localhost:9000/")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("net.sergeych:mp_stools:1.2.2")

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-server-cors:$ktorVersion")
                implementation("io.ktor:ktor-server-compression:$ktorVersion")
                implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongoVersion")
                implementation("io.klogging:klogging-jvm:0.4.12")
                implementation("org.kodein.di:kodein-di:7.18.0")
                implementation("io.ktor:ktor-server-conditional-headers:$ktorVersion")
                implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation(kotlin("test-common"))
                implementation(kotlin("test"))
                implementation("io.kotest:kotest-runner-junit5:5.3.1")

            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
                implementation(npm("color","^4.2.3"))
                implementation(kotlinw("mui"))
                implementation(kotlinw("mui-icons"))
            }
        }
    }
}

application {
    mainClass.set("xyz.nietongxue.soccerTime.ServerKt")
}

// include JS artifacts in any JAR we generate
tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}

// Alias "installDist" as "stage" (for cloud providers)
tasks.create("stage") {
    dependsOn(tasks.getByName("installDist"))
}

tasks.getByName<JavaExec>("run") {
    classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}