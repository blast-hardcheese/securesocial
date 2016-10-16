name := "SecureSocial-parent"

version := Common.version

scalaVersion := Common.scalaVersion

lazy val core =  project.in( file("core") )

lazy val securesocialPlay =  project.in( file("module-code") ).enablePlugins(PlayScala).dependsOn(core)

lazy val scalaDemo = project.in( file("samples/scala/demo") ).enablePlugins(PlayScala).dependsOn(securesocialPlay)

lazy val javaDemo = project.in( file("samples/java/demo") ).enablePlugins(PlayJava).dependsOn(securesocialPlay)

lazy val root = project.in( file(".") ).aggregate(core, securesocialPlay, scalaDemo, javaDemo) .settings(
     aggregate in update := false
   )
