addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.14.0")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)


