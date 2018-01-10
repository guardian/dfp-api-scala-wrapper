name := "dfp-api-scala-wrapper"

version := "1.0"

scalaVersion := "2.12.4"

scalafmtOnCompile := true

val adsLibVersion = "3.10.0"

libraryDependencies ++= Seq(
  "com.google.api-ads" % "ads-lib"  % adsLibVersion,
  "com.google.api-ads" % "dfp-axis" % adsLibVersion
)
