addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.5.5")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(Resolver.ivyStylePatterns)
