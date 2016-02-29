name := """competiFit"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies +=  "org.mongodb" % "mongo-java-driver" % "3.2.0"

libraryDependencies ++= Seq(
  "org.jongo" % "jongo" % "1.2",
  "com.google.code.gson" % "gson" % "2.2.4"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
