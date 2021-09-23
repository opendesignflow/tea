

// Versions
//-----------------
var scalaMajorVersion by extra("3")
var scalaMinorVersion by extra("0.2")
val scalaVersion = "$scalaMajorVersion.$scalaMinorVersion"

var lib_version_base by extra("4.1.0")
var lib_version_suffix by extra("-SNAPSHOT")
var branch by extra { System.getenv("BRANCH_NAME") }

if (System.getenv().getOrDefault("BRANCH_NAME", "dev").contains("release")) {
    lib_version_suffix = ""
}

var lib_version by extra { lib_version_base+lib_version_suffix }




allprojects {

    group = "org.odfi"
    version = lib_version

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
}