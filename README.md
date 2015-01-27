# sbt-avrohugger
sbt plugin for generating Scala sources for Apache Avro schemas and protocols.

##Usage

Install the plugin
------------------

Add the plugin according to the `sbt documentation`_.

.. _`sbt documentation`: https://github.com/harrah/xsbt/wiki/Getting-Started-Using-Plugins

For instance, add the following lines to the file ``hello/project/build.sbt`` in your
project directory::

    addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "0.0.1")


Import the plugin settings
--------------------------

To activate the plugin, import its settings by adding the following lines to 
your ``hello/build.sbt`` file::

    seq( sbtavrohugger.SbtAvrohugger.avroSettings : _*)





Tasks
-----

These tasks are automatically executed every time the project is compiled:

===============     ================    ==================
Name                Name in shell        Description
===============     ================    ==================
generate            generate            Compiles the Avro files into Scala case classes.
generateSpecific    generate-specific   Compiles the Avro files into Scala case classes implementing `SpecificRecord`.
===============     ================    ==================



#### Credits

`sbt-avrohugger` is based on [sbt-avro](https://github.com/cavorite/sbt-avro) by [Juan Manuel Caicedo](http://cavorite.com/) (even this README file!).


#### Fork away, just make sure the tests pass before you send a pull request.

#### Criticism is appreciated.
