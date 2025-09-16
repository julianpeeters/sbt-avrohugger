addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.15.0-SNAPSHOT")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(using Resolver.ivyStylePatterns)