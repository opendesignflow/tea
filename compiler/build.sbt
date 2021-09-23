name := "tea-compiler"
scalaVersion := "3.0.2"


libraryDependencies += "org.scala-lang" % "scala3-compiler_3" % scalaVersion.value
libraryDependencies += "org.scalatest" % "scalatest-funsuite_3" % "3.2.9"
libraryDependencies += "org.scalatest" % "scalatest-shouldmatchers_3" % "3.2.9"
libraryDependencies += "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"
