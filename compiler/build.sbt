name := "tea-compiler"
scalaVersion := "3.1.0"
crossScalaVersions := Nil

libraryDependencies += "org.scala-lang" % "scala3-compiler_3" % scalaVersion.value
libraryDependencies += "org.scalatest" % "scalatest-funsuite_3" % "3.2.10"
libraryDependencies += "org.scalatest" % "scalatest-shouldmatchers_3" % "3.2.10"
libraryDependencies += "com.vladsch.flexmark" % "flexmark-all" % "0.62.2"
