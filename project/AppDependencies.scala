import sbt._

object AppDependencies {
  val bootstrapVersion = "10.7.0"

  val compile = Seq(
    "uk.gov.hmrc"            %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "uk.gov.hmrc"            %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "uk.gov.hmrc"            %% "play-frontend-hmrc-play-30" % "13.13.0",
    "uk.gov.hmrc"            %% "play-json-schema-validator" % "0.1.0",
    "org.typelevel"          %% "cats-core"                  % "2.13.0",
    "org.scala-lang.modules" %% "scala-xml"                  % "2.4.0",
    "org.scalacheck"         %% "scalacheck"                 % "1.19.0"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion % Test,
    "uk.gov.hmrc"            %% "stub-data-generator"    % "1.6.0"          % Test exclude ("org.scalacheck", "scalacheck_3"),
    "org.scalatest"          %% "scalatest"              % "3.2.19"         % Test,
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8"         % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.2"          % Test
  )
}
