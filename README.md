Reactivemongo for Play 2.4 Example
=======================
This is a Play CRUD Example using reactiveMongo driver. It demontrates:
<ul>
<li>MongoDb Connection using reactiveMongoDb in Play</li>
<li>BSONReader and BSONWriter Implementation</li>
<li>CRUD using different data type(String, JodaDate, Integer, Double, Boolean and List)</li>
<li>22 Fields Limit workaround by using multiple case class in Model</li>
</ul>

This example use the following:
<ul>
<li>Play Framework 2.4.11</li>
<li>Reactive Scala Driver for MongoDB 0.12.0-play24</li>
<li>Scala 2.11.8</li>
<li>MongoDb</li>
<li>JQuery</li>
</ul>

Setup Instruction
=======================
Modify build.sbt
<div class="highlight highlight-scala"><pre>
name := """play-reactivemongo-crud"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.12.0-play24"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
</pre></div>
