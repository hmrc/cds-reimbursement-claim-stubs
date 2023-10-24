import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "cds-reimbursement-claim-stubs"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    majorVersion := 0,
    addCompilerPlugin(scalafixSemanticdb),
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
  )
  .settings(routesImport := Seq("_root_.controllers.Assets.Asset"))
  .settings(TwirlKeys.templateImports := Seq.empty)
  .settings(
    scalacOptions ++= Seq(
      "-Yrangepos",
      "-language:postfixOps"
    ),
    Test / scalacOptions --= Seq("-Ywarn-value-discard")
  )
  .settings(Assets / pipelineStages := Seq(uglify))
  .settings(scalaVersion := "2.13.10")
  .settings(Compile / resourceDirectory := baseDirectory.value / "/conf")
  .settings(PlayKeys.playDefaultPort := 7502)
  .settings(resolvers ++= Seq(Resolver.jcenterRepo))
