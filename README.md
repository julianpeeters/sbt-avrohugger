# sbt-avrohugger
sbt plugin for generating Scala case classes and ADTs from Apache Avro schemas, datafiles, and protocols.


Install the plugin
------------------

Add the following lines to the file ``myproject/project/plugins.sbt`` in your
project directory:

    addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "0.10.0")


Import the plugin settings
--------------------------

To activate the plugin, import its settings by adding one of the following lines to
your ``myproject/build.sbt`` file:


To get the 'generate' task for generating standard Scala Case Classes use:

    sbtavrohugger.SbtAvrohugger.avroSettings


To get the `generate-specific` task for generating Scala Case Classes that are compatible with the Avro Specific API, use:

    sbtavrohugger.SbtAvrohugger.specificAvroSettings



To get the `generate-scavro` task for generating Scala Case Class wrapper classes (Java generated classes supplied separately) for use with [Scavro](https://github.com/oedura/scavro), use:

    sbtavrohugger.SbtAvrohugger.scavroSettings


Scope
-----
All settings and tasks are in the ``avro`` scope. E.g., to execute the
``generate`` task directly, just run ``avro:generate``.


Settings
--------

| Name          | Name in shell | Default  | Description  |
| ------------- |:-------------:| -----:| -----:|
| sourceDirectory      | ``source-directory``  | ``src/main/avro`` | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| scalaSource          | ``scala-source``      |   ``$sourceManaged/main`` |   Path for the generated ``*.scala`` or ``*.java``  files. |
| avroScalaCustomTypes      | ``avro-scala-custom-types`` |   ``Map.empty[String, Class[_]]`` | Map for reassigning `array` to `Array`, `List`, or `Seq`. |
| avroScalaCustomNamespace | ``avro-scala-custom-namespace`` |   ``Map.empty[String, String]`` | Map for reassigning namespaces. |

Changing Settings
-----------------

Settings can be overridden by adding lines to ``myproject/build.sbt``:

```scala    
(scalaSource in avroConfig) := new java.io.File("myscalaSource")

```


`avro-scala-custom-types` and `avro-scala-custom-namespace` require additional imports (I'm not sure why these aren't picked up with the other settings, anybody know why we have to import them separately?):

```scala
import sbtavrohugger.AvrohuggerSettings.{ avroScalaCustomTypes, avroScalaCustomNamespace }

(avroScalaCustomTypes in avroConfig) := Map("array"->classOf[Array[_]])

(avroScalaCustomNamespace in avroConfig) := Map("example"->"overridden")
```

Tasks
-----
Each task is automatically executed every time the project is compiled.*
* as of Intellij IDEA 14.1.4, and possibly for other IDEs, the task must be run manually from the integrated Terminal:  ``avro:generate``.


| Name          | Name in shell | Description  |
| ------------- |:-------------:| -----:|
| generate      | ``generate`` | Compiles the Avro files into Scala case classes. |
| generateSpecific      | ``generate-specific``      |   Compiles the Avro files into Scala case classes implementing `SpecificRecord`. |
| generateScavro      | ``generate-scavro``      |   Compiles the Avro files into Scala case class Scavro wrapper classes. |






Datatypes
---------
Supports generating case classes with arbitrary fields of the following datatypes:


* INT &rarr; Int
* LONG &rarr; Long
* FLOAT &rarr; Float
* DOUBLE &rarr; Double
* STRING &rarr; String
* BOOLEAN &rarr; Boolean
* NULL &rarr; Null
* MAP &rarr; Map
* ENUM &rarr; `generate`: scala.Enumeration, `generate-specific`: Java Enum
* BYTES &rarr; Array[Byte]
* FIXED &rarr; //TODO
* ARRAY &rarr; List (`generate-scavro`: Array). To reassign, please see Settings above.
* UNION &rarr; Option
* RECORD &rarr; case class



Future
------
* support for more avro datatypes
* decimal support for avrohugger via logical types
* more codegen situations, e.g. exploding Spark Rows?

Credits
-------

`sbt-avrohugger` is based on [sbt-avro](https://github.com/cavorite/sbt-avro) by [Juan Manuel Caicedo](http://cavorite.com/), and depends on [avrohugger](https://github.com/julianpeeters/avrohugger).

#### Contributors

- [Marius Soutier](https://github.com/mariussoutier)
- [Brennan Saeta](https://github.com/saeta)
- [Daniel Lundin](https://github.com/dln)
- [Vince Tse](https://github.com/vtonehundred)
- [Jerome Wacongne](https://github.com/ch4mpy)
- [Ryan Koval](http://github.ryankoval.com)
- [Saket](https://github.com/skate056)

#### Fork away, just make sure the tests pass before you send a pull request.


#### Criticism is appreciated.
