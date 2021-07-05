name := """stock-quote-challenge"""
organization := "com.github.jordanbethea"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "com.yahoofinance-api" % "YahooFinanceAPI" % "3.14.0"
libraryDependencies += "org.webjars" % "jquery" % "3.6.0"
libraryDependencies += "org.webjars.npm" % "chart.js" % "3.3.0"
libraryDependencies += "org.webjars" % "bootstrap" % "4.4.1" exclude("org.webjars", "jquery")
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.jordanbethea.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.jordanbethea.binders._"
