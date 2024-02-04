import scala.sys.process.Process

ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := Versions.Scala_3

lazy val root = project.in(file("."))
  .settings(
    name := "Laminar Demo"
  )

lazy val client = project
  .in(file("./client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= List(
      "com.raquo" %%% "laminar" % Versions.Laminar,
    ),
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
    },
    // Generated scala.js output will call your main() method to start your app.
    scalaJSUseMainModuleInitializer := true
  )

val buildClient = taskKey[Unit]("Build client (frontend)")

buildClient := {
  // Generate Scala.js JS output for production
  (client / Compile / fullLinkJS).value

  // Install JS dependencies from package-lock.json
  val npmCiExitCode = Process("npm ci", cwd = (client / baseDirectory).value).!
  if (npmCiExitCode > 0) {
    throw new IllegalStateException(s"npm ci failed. See above for reason")
  }

  // Build the frontend with vite
  val buildExitCode = Process("npm run build", cwd = (client / baseDirectory).value).!
  if (buildExitCode > 0) {
    throw new IllegalStateException(s"Building frontend failed. See above for reason")
  }
}
// -- Aliases

// Run the frontend development loop (also run vite: `cd frontend; npm run dev`)
addCommandAlias("cup", ";~client/fastLinkJS")
// Build frontend for production
addCommandAlias("cbuild", ";buildClient")
