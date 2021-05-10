import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "cds-reimbursement-claim-stubs"

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    majorVersion := 0,
    addCompilerPlugin(scalafixSemanticdb),
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
  )
  .settings(routesImport := Seq.empty)
  .settings(TwirlKeys.templateImports := Seq.empty)
  .settings(
    scalacOptions ++= Seq(
      "-Yrangepos",
      "-language:postfixOps"
    ),
    Test / scalacOptions --= Seq("-Ywarn-value-discard"),
    scalacOptions += "-P:silencer:pathFilters=routes"
  )
  .settings(scalaVersion := "2.12.12")
  .settings(publishingSettings: _*)
  .settings(Compile / resourceDirectory := baseDirectory.value / "/conf")
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(PlayKeys.playDefaultPort := 7502)
  .settings(resolvers ++= Seq(Resolver.jcenterRepo, "emueller-bintray" at "https://dl.bintray.com/emueller/maven"))
