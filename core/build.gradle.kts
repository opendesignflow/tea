plugins {

    // Scala
    id("scala")

    id("java-library")

    // Publish
    id("maven-publish")

    id("com.github.maiflai.scalatest") version "0.31"



}

// Versions
//-----------------
var scalaMajorVersion  by extra("2.13")
var scalaMinorVersion by extra("6")
val scalaVersion = "$scalaMajorVersion.$scalaMinorVersion"


var lib_version_base  : String by rootProject.extra
var lib_version_suffix : String  by rootProject.extra

// Add pre to version
version = lib_version_base+".pre3"+lib_version_suffix

println("Core Version is $version")

// Sources
//---------------
sourceSets {
    main {
        scala {
            srcDir("src/main/java")
        }

        java {
            this.setSrcDirs(emptyList<File>())
        }
    }

}

// Deps
//-----------
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
    // withJavadocJar()
    withSourcesJar()
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks.withType<ScalaCompile>().configureEach {
    scalaCompileOptions.additionalParameters = listOf("")
}



dependencies {

    api("org.scala-lang:scala-library:$scalaVersion")


    testImplementation("org.scalatest:scalatest-funsuite_$scalaMajorVersion:3.2.9")
    testImplementation("org.scalatest:scalatest-shouldmatchers_$scalaMajorVersion:3.2.9")
    testImplementation ("com.vladsch.flexmark:flexmark-all:0.35.10")
}

repositories {

    mavenLocal()
    mavenCentral()
    maven {
        name = "Sonatype Nexus Snapshots"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "ODFI Releases"
        url = uri("https://www.opendesignflow.org/maven/repository/internal/")
    }
    maven {
        name = "ODFI Snapshots"
        url = uri("https://www.opendesignflow.org/maven/repository/snapshots/")
    }
    maven {
        url = uri("https://repo.triplequote.com/libs-release/")
    }
    google()
}

publishing {
    publications {

        create<MavenPublication>("maven") {
            artifactId = "tea"
            from(components["java"])

            pom {
                name.set("Tea Scala Utils Library")
                description.set("A simple collection of old utility functions")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("richnou")
                        name.set("Richnou")
                        email.set("leys.richard@gmail.com")
                    }
                }
            }
        }

    }
    repositories {
        maven {

            // change URLs to point to your repos, e.g. http://my.org/repo
            var releasesRepoUrl = uri("https://www.opendesignflow.org/maven/repository/internal/")
            var snapshotsRepoUrl = uri("https://www.opendesignflow.org/maven/repository/snapshots")

            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            // Credentials
            //-------------
            credentials {
                username = System.getenv("PUBLISH_USERNAME")
                password = System.getenv("PUBLISH_PASSWORD")
            }
        }
    }
}

