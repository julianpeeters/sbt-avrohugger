addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.5.6")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(Resolver.ivyStylePatterns)
