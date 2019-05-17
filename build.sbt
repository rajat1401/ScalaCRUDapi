enablePlugins(JavaAppPackaging, AshScriptPlugin)

name := "todoapi"

version := "0.1"

scalaVersion := "2.12.8"
val akkaHttp = "10.1.1"
val akka = "2.5.11"
val circeVersion= "0.9.3"
resolvers ++= Seq(
  Resolver.bintrayRepo("lonelyplanet", "maven")
)

dockerBaseImage := "openjdk:12-jre-alpine"
packageName in Docker := "User api"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8", "io.spray" %% "spray-json" % "1.3.4",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.4.2",
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe.akka" %% "akka-http-core"  % akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % akka,
  "com.typesafe.akka" %% "akka-slf4j" % akka,
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-testkit" % akka % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akka % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.25.2",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "joda-time" % "joda-time" % "2.10.2"
)