

name := "tea"
scalaVersion := "3.2.0"
crossScalaVersions ++= Seq("2.13.10")



//mainClass in (Compile, run) := Some("org.odfi.tea.testReflect")

mainClass := Some("org.odfi.tea.testReflect")


scalacOptions ++=  Seq(
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked" // Enable additional warnings where generated code depends on assumptions.

 //"-Werror" // Fail the compilation if there are any warnings.

)