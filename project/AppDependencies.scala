import sbt._

object AppDependencies {
  val silencerVersion = "1.7.0"
  val compile         = Seq(
    "uk.gov.hmrc"    %% "bootstrap-backend-play-27" % "3.2.0",
    compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib"              % silencerVersion % Provided cross CrossVersion.full
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.2.3" % Test,
    "org.pegdown"    % "pegdown"   % "1.6.0" % "test"
  )
}
