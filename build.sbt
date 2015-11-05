name := "qomo-ftp"

version := "1.0"

scalaVersion := "2.11.7"

mainClass in Compile := Some("cbb.qomo.ftp.Main")

libraryDependencies ++= Seq(
  "org.apache.ftpserver" % "ftpserver-core" % "1.0.6",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.9",
  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",
  "org.postgresql" % "postgresql" % "9.4-1205-jdbc42",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

resolvers += "CBB" at "http://cloud.big.ac.cn/mvn/content/groups/public/"
externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)