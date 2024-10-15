addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.9.0-M2")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)


