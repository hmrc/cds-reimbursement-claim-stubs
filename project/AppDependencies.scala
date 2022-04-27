import sbt._

object AppDependencies {
  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28"  % "5.2.0",
    "uk.gov.hmrc"       %% "stub-data-generator"        % "0.5.3",
    "com.eclipsesource" %% "play-json-schema-validator" % "0.9.5",
    "org.julienrf"      %% "play-json-derived-codecs"  % "7.0.0",
    "org.typelevel"     %% "cats-core"                  % "2.3.1",
    "org.scala-lang.modules" %% "scala-xml"             % "2.0.0-M4"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.2.11" % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
  )
}
