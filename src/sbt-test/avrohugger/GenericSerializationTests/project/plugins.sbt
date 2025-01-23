addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.11.2")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)


