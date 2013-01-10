name := "horta-hell"

version := "0.1-SNAPSHOT"

mainClass in (Compile, run) := Some("org.ru.codingteam.horta.Application")

scalaVersion := "2.10.0"

resolvers ++= Seq(
	"clojars" at "http://clojars.org/repo/",
	"clojure-releases" at "http://build.clojure.org/releases")

libraryDependencies += "platonus" % "platonus" % "0.1.12"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.1.0"

libraryDependencies += "org.igniterealtime.smack" % "smack" % "3.2.1"

libraryDependencies += "org.igniterealtime.smack" % "smackx" % "3.2.1"