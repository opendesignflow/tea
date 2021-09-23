

ThisBuild / organization := "org.odfi"
ThisBuild / version := "4.1.0-SNAPSHOT"

/*
lazy val root = (project in file("."))
  .aggregate(compiler, core)*/

lazy val core = (project in file("core"))
lazy val compiler = (project.dependsOn(core) in file("compiler"))
