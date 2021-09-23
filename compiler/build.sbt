name := "tea-compiler"
scalaVersion := "3.0.2"


libraryDependencies += "org.scala-lang" % "scala3-compiler_3" % scalaVersion.value
libraryDependencies += "org.scalatest" % "scalatest-funsuite_3" % "3.2.9"
libraryDependencies += "org.scalatest" % "scalatest-shouldmatchers_3" % "3.2.9"
libraryDependencies += "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"

/*
val pUser = sys.env.get("PUBLISH_USERNAME")
val pPWD = sys.env.get("PUBLISH_PASSWORD")

credentials += Credentials("Sonatype Nexus Repository Manager", "www.opendesignflow.org",pUser.getOrElse("-"),pPWD.getOrElse("-"))

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
    
}*/