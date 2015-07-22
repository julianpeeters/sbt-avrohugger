# sbt-avrohugger
sbt plugin for generating Scala sources for Apache Avro schemas, datafiles, and protocols.


Install the plugin
------------------

Add the plugin according to the `sbt documentation`_.

.. _`sbt documentation`: https://github.com/harrah/xsbt/wiki/Getting-Started-Using-Plugins

For instance, add the following lines to the file ``hello/project/build.sbt`` in your
project directory:

    addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "0.1.4")


Import the plugin settings
--------------------------

To activate the plugin, import its settings by adding the following lines to 
your ``hello/build.sbt`` file. To get the 'generate' task for generating standard Scala Case Classes use:

    seq( sbtavrohugger.SbtAvrohugger.avroSettings : _*)


To get the `generate-specific` task for generating Scala Case Classes that are compatible with the Avro Specific API, use:

    seq( sbtavrohugger.SbtAvrohugger.specificAvroSettings : _*)



Scope
-----
All settings and tasks are in the ``avro`` scope. E.g., to execute the
``generate`` task directly, just run ``avro:generate``.


Settings
--------

| Name          | Name in shell | Default  | Description  |
| ------------- |:-------------:| -----:| -----:|
| sourceDirectory     | ``source-directory`` | ``src/main/avro`` | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| scalaSource      | ``scala-source``      |   ``$sourceManaged/compiled_avro`` |   Path for the generated ``*.scala`` or ``*.java``  files. |



Tasks
-----
Each task is automatically executed everytime the project is compiled.


| Name          | Name in shell | Description  |
| ------------- |:-------------:| -----:|
| generate      | ``generate`` | Compiles the Avro files into Scala case classes. | 
| generateSpecific      | ``generate-specific``      |   Compiles the Avro files into Scala case classes implementing `SpecificRecord`. |





Datatypes
---------
Depends on [avrohugger](https://github.com/julianpeeters/avrohugger) to support the following datatypes:
Supports generating case classes with arbitrary fields of the following datatypes: 


* INT -> Int
* LONG -> Long
* FLOAT -> Float
* DOUBLE -> Double
* STRING -> String
* BOOLEAN -> Boolean
* NULL  -> Null
* MAP -> Map
* ENUM -> `generate`: scala.Enumeration, `generate-specific`: Java Enum
* BYTES -> //TODO
* FIXED -> //TODO
* ARRAY -> List
* UNION -> Option
* RECORD -> case class



Future
------
* support for more avro datatypes
* ability to override default settings in avroConfig
* ability to specify preferred collection type to represent ARRAY


Credits
-------

`sbt-avrohugger` is based on [sbt-avro](https://github.com/cavorite/sbt-avro) by [Juan Manuel Caicedo](http://cavorite.com/) (even this README file!).

#### Contributors

- [Marius Soutier](https://github.com/mariussoutier)
- [Brennan Saeta](https://github.com/saeta)
- [Daniel Lundin](https://github.com/dln)
- [Vince Tse](https://github.com/vtonehundred)


#### Fork away, just make sure the tests pass before you send a pull request.


#### Criticism is appreciated.
