lazy val root = project
  .in(file("."))
  .aggregate(redis4s)
  .aggregate(`redis4s-test`)
  .aggregate(`redis4s-it`)
  .settings(publish / skip := true)

lazy val commonSettings = Seq(
  organization := "com.github.redis4s",
  scalaVersion := "2.12.10",
  crossScalaVersions := List("2.12.10", "2.13.1"),
  scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
  publish / skip := true,
  scalafmtOnCompile := true,
  cancelable in Global := true,
  fork in run := true,
  Test / fork := true,
  parallelExecution in Test := false,
  addCompilerPlugin("org.typelevel"    % "kind-projector"      % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"       %% "better-monadic-for" % "0.3.1"),
  addCompilerPlugin("com.github.cb372" % "scala-typed-holes"   % "0.1.3.3" cross CrossVersion.full),
  wartremoverErrors := Nil,
  testFrameworks += new TestFramework("minitest.runner.Framework"),
  version ~= (_.replace('+', '-')),
  dynver ~= (_.replace('+', '-'))
)

lazy val testDeps = Seq(
  libraryDependencies ++= {
    Seq(
      "com.codecommit"    %% "cats-effect-testing-minitest" % "0.4.0",
      "io.chrisdavenport" %% "log4cats-slf4j"               % "1.1.1",
      "io.monix"          %% "minitest"                     % "2.8.2",
      "ch.qos.logback"    % "logback-classic"               % "1.2.3"
    )
  }
)

lazy val redis4s = project
  .in(file("redis4s"))
  .settings(commonSettings)
  .settings(publish / skip := false)
  .settings(
    libraryDependencies ++= {
      Seq(
        "org.log4s"         %% "log4s"         % "1.8.2",
        "io.chrisdavenport" %% "log4cats-core" % "1.1.1",
        "org.tpolecat"      %% "natchez-core"  % "0.0.11",
        "co.fs2"            %% "fs2-io"        % "2.3.0",
        "co.fs2"            %% "fs2-core"      % "2.3.0",
        "org.typelevel"     %% "cats-free"     % "2.1.1",
        "org.scodec"        %% "scodec-core"   % "1.11.7",
        "io.chrisdavenport" %% "keypool"       % "0.2.0"
      )
    }
  )
  .settings(
    wartremoverErrors in (Compile, compile) ++= Warts.allBut(
      Wart.Any,
      Wart.AsInstanceOf,
      Wart.DefaultArguments,
      Wart.Equals,
      Wart.FinalCaseClass,
      Wart.LeakingSealed,
      Wart.NonUnitStatements,
      Wart.Nothing,
      Wart.Null,
      Wart.Serializable,
      Wart.StringPlusAny,
      Wart.TraversableOps
    )
  )

lazy val `redis4s-test` = project
  .in(file("redis4s-test"))
  .settings(commonSettings)
  .settings(testDeps)
  .dependsOn(redis4s)

lazy val `redis4s-it` = project
  .in(file("redis4s-it"))
  .settings(commonSettings)
  .settings(testDeps)
  .dependsOn(redis4s)
