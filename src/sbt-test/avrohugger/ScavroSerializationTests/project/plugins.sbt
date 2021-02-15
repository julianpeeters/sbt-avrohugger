logLevel := Level.Warn

addSbtPlugin("com.cavorite" % "sbt-avro-1-8" % "1.1.4")

addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "latest.integration")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)
