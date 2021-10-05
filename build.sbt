

ThisBuild / organization := "org.odfi"
ThisBuild / version := "4.1.1-SNAPSHOT"
publish / skip := true

/*
lazy val root = (project in file("."))
  .aggregate(compiler, core)*/




val pUser = sys.env.get("PUBLISH_USERNAME")
val pPWD = sys.env.get("PUBLISH_PASSWORD")

lazy val commonSettings = Seq(
    credentials += Credentials("Sonatype Nexus Repository Manager", "www.opendesignflow.org",pUser.getOrElse("-"),pPWD.getOrElse("-")),
    publishTo := {
        if (pUser.isDefined && pPWD.isDefined) {
            val nexus = "https://www.opendesignflow.org/maven/repository/"
            if (isSnapshot.value)
                Some("snapshots" at nexus + "snapshots/")
            else
                Some("releases"  at nexus + "internal/")

        } else {
            None
        }
        
    }
)


lazy val core = (project in file("core")).settings(commonSettings)
lazy val compiler = (project.dependsOn(core) in file("compiler")).settings(commonSettings)


