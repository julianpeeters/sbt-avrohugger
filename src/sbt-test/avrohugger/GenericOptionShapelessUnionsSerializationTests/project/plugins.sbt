addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.6.0-SNAPSHOT")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)


