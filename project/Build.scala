import sbt._

import Keys._
import AndroidKeys._

object General {


  val settings = Defaults.defaultSettings ++ Seq (
    name := "scaladroid",
    version := "0.1",
    versionCode := 1,
    scalaVersion := "2.9.1",
    platformName in Android := "android-15",
    scalacOptions += "-Xexperimental"
  )

  val proguardSettings = Seq (
    useProguard in Android := true
  )

  lazy val fullAndroidSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    proguardSettings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "mykey",
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    "scaladroid",
    file("."),
    settings = General.fullAndroidSettings
  )

  lazy val tests = Project (
    "test",
    file("test"),
    settings = General.settings ++
               AndroidTest.androidSettings ++
               General.proguardSettings ++ Seq (
      name := "ShowyItTests"
    )
  ) dependsOn main
}
