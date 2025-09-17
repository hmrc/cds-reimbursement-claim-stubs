import sbt._

object AppDependencies {
  val compile = Seq(
    "uk.gov.hmrc"            %% "bootstrap-frontend-play-30" % "10.1.0",
    "uk.gov.hmrc"            %% "bootstrap-backend-play-30"  % "10.1.0",
    "uk.gov.hmrc"            %% "play-frontend-hmrc-play-30" % "12.12.0",
    "uk.gov.hmrc"            %% "stub-data-generator"        % "1.5.0",
    "uk.gov.hmrc"            %% "play-json-schema-validator" % "0.1.0",
    "org.typelevel"          %% "cats-core"                  % "2.13.0",
    "org.scala-lang.modules" %% "scala-xml"                  % "2.4.0"
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"          % "3.2.19" % Test,
    "com.vladsch.flexmark"    % "flexmark-all"       % "0.64.8" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2"  % Test
  )
}
