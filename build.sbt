val CatsVersion = "2.6.1"
val CatsEffectVersion = "3.2.1"
val CirceVersion = "0.14.1"
val Http4sVersion = "0.23.0"
val Log4catsVersion = "2.1.1"
val LogbackVersion = "1.2.5"
val MunitCatsEffectVersion = "1.0.5"
val MunitVersion = "0.7.27"
val PureConfigVersion = "0.16.0"
val TapirVersion = "0.19.0-M4"

lazy val root = (project in file("."))
  .settings(
    organization := "47deg",
    name := "tapir-playground",
    version := "0.1",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "ch.qos.logback"               % "logback-classic"          % LogbackVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core"               % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"  % TapirVersion,
      "io.circe"                    %% "circe-core"               % CirceVersion,
      "io.circe"                    %% "circe-generic"            % CirceVersion,
      "org.http4s"                  %% "http4s-blaze-server"      % Http4sVersion,
      "org.http4s"                  %% "http4s-circe"             % Http4sVersion,
      "org.http4s"                  %% "http4s-dsl"               % Http4sVersion,
      "org.typelevel"               %% "cats-core"                % CatsVersion,
      "org.typelevel"               %% "cats-effect"              % CatsEffectVersion,
      "org.typelevel"               %% "log4cats-slf4j"           % Log4catsVersion,
      "org.scalameta"               %% "munit"                    % MunitVersion           % Test,
      "org.typelevel"               %% "munit-cats-effect-3"      % MunitCatsEffectVersion % Test
    ),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    testFrameworks += new TestFramework("munit.Framework")
  )
