import sbt._

object AppDependencies {
  val compile = Seq(
    "uk.gov.hmrc"            %% "bootstrap-frontend-play-30" % "8.4.0",
    "uk.gov.hmrc"            %% "bootstrap-backend-play-30"  % "8.4.0",
    "uk.gov.hmrc"            %% "play-frontend-hmrc-play-30" % "8.5.0",
    "uk.gov.hmrc"            %% "stub-data-generator"        % "1.1.0",
    "com.eclipsesource"      %% "play-json-schema-validator" % "0.9.5",
    "org.julienrf"           %% "play-json-derived-codecs"   % "10.1.0",
    "org.typelevel"          %% "cats-core"                  % "2.10.0",
    "org.scala-lang.modules" %% "scala-xml"                  % "2.2.0"
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"          % "3.2.18" % Test,
    "com.vladsch.flexmark"    % "flexmark-all"       % "0.64.8" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1"  % Test
  )
}
