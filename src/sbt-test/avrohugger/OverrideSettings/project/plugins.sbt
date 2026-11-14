addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.18.1")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(using Resolver.ivyStylePatterns)
