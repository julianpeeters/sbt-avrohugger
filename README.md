# sbt-avrohugger
sbt plugin for generating Scala case classes from Apache Avro schemas, datafiles, and protocols.


Install the plugin
------------------

Add the following lines to the file ``myproject/project/plugins.sbt`` in your
project directory:

    addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "0.4.1")


Import the plugin settings
--------------------------

To activate the plugin, import its settings by adding one of the following lines to
your ``myproject/build.sbt`` file:


To get the 'generate' task for generating standard Scala Case Classes use:

    sbtavrohugger.SbtAvrohugger.avroSettings


To get the `generate-specific` task for generating Scala Case Classes that are compatible with the Avro Specific API, use:

    sbtavrohugger.SbtAvrohugger.specificAvroSettings



Scope
-----
All settings and tasks are in the ``avro`` scope. E.g., to execute the
``generate`` task directly, just run ``avro:generate``.


Settings
--------

| Name          | Name in shell | Default  | Description  |
| ------------- |:-------------:| -----:| -----:|
| sourceDirectory     | ``source-directory`` | ``src/main/avro`` | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| scalaSource      | ``scala-source``      |   ``$sourceManaged/main`` |   Path for the generated ``*.scala`` or ``*.java``  files. |


Changing Settings
-----------------

Settings can be overridden by adding a line to ``myproject/build.sbt``:

    (scalaSource in avroConfig) := new java.io.File("myscalaSource")



Tasks
-----
Each task is automatically executed every time the project is compiled.*
* as of Intellij IDEA 14.1.4, and possibly for other IDEs, the task must be run manually from the integrated Terminal:  ``avro:generate``.


| Name          | Name in shell | Description  |
| ------------- |:-------------:| -----:|
| generate      | ``generate`` | Compiles the Avro files into Scala case classes. |
| generateSpecific      | ``generate-specific``      |   Compiles the Avro files into Scala case classes implementing `SpecificRecord`. |





Datatypes
---------
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
- [Jerome Wacongne](https://github.com/ch4mpy)

#### Fork away, just make sure the tests pass before you send a pull request.


#### Criticism is appreciated.
