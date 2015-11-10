addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "0.5.1")

resolvers += Resolver.file("Local Ivy Repository", file("/home/julianpeeters/.ivy2/local/"))(Resolver.ivyStylePatterns)
