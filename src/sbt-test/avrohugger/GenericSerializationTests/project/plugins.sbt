addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.17.1-SNAPSHOT")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(using Resolver.ivyStylePatterns)


