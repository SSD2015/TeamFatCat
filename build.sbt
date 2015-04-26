name := """ExceedVote"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6", "-encoding", "UTF-8", "-Xlint:unchecked")

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.mariadb.jdbc" % "mariadb-java-client" % "1.1.8",
  "org.hibernate" % "hibernate-annotations" % "3.5.6-Final",
  "org.mindrot" % "jbcrypt" % "0.3m"
)