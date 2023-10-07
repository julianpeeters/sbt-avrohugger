addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.5.7-SNAPSHOT")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(Resolver.ivyStylePatterns)
