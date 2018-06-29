name := """play-reactivemongo-crud"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
	guice,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.13.0-play26",
  "com.typesafe.play" %% "play-json-joda" % "2.6.8",
  jodaForms
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


val elastic4sVersion = "0.90.2.8"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-tcp" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-http" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-circe" % "5.5.3",
  "org.apache.logging.log4j" % "log4j-api" % "2.9.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.9.1",
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "5.5.3" % "test"
)