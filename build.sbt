ThisBuild / scalaVersion := "3.7.1"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.liuyic00"

lazy val root = (project in file("."))
  .settings(
    name := "smtfmt",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "mainargs"  % "0.7.6",
      "com.lihaoyi" %% "fastparse" % "3.1.1"
    )
  )
