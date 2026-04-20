addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.16.6-SNAPSHOT")

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(using Resolver.ivyStylePatterns)