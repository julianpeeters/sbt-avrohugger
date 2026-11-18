addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % sys.props("plugin.version"))

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local/"))(using Resolver.ivyStylePatterns)
