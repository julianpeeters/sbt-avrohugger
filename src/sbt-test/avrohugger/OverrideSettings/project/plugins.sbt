addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.16.3")

resolvers += Resolver.file("Local Ivy Repository", file("~/.ivy2/local/"))(using Resolver.ivyStylePatterns)
