name := "jaxrs-demo"

organization := "me.ivanmykhailov"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.glassfish.jersey.containers" % "jersey-container-grizzly2-http" % "2.13"
)


//Tests
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0" % "test"
)


scalacOptions ++= Seq(
    "-deprecation"
  , "-feature"
  , "-unchecked"
  , "-Xlint"
  , "-Yno-adapted-args"
  , "-Ywarn-all"
  , "-Ywarn-dead-code"
  , "-language:postfixOps"
  , "-language:implicitConversions"
)

//seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

net.virtualvoid.sbt.graph.Plugin.graphSettings

//fork := true
