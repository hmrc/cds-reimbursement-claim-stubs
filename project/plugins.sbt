resolvers += Resolver.bintrayIvyRepo("hmrc", "sbt-plugin-releases")
resolvers += Resolver.bintrayRepo("hmrc", "releases")
resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("uk.gov.hmrc"               % "sbt-auto-build"     % "2.13.0")
addSbtPlugin("uk.gov.hmrc"               % "sbt-git-versioning" % "2.2.0")
addSbtPlugin("uk.gov.hmrc"               % "sbt-distributables" % "2.1.0")
addSbtPlugin("com.typesafe.play"         % "sbt-plugin"         % "2.7.5")
addSbtPlugin("uk.gov.hmrc"               % "sbt-artifactory"    % "1.6.0")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"       % "2.2.1")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix"       % "0.9.24")
