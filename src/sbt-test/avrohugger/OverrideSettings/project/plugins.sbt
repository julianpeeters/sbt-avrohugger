addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.16.1")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(using Resolver.ivyStylePatterns)
