addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.0.0-RC14")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)


