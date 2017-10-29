# sbt-avrohugger


[![Travis CI](https://travis-ci.org/julianpeeters/sbt-avrohugger.svg?branch=master)](https://travis-ci.org/sbt/sbt-avrohugger)
[![Join the chat at https://gitter.im/julianpeeters/avrohugger](https://badges.gitter.im/julianpeeters/avrohugger.svg)](https://gitter.im/julianpeeters/avrohugger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


sbt plugin for generating Scala case classes and ADTs from Apache Avro schemas, datafiles, and protocols.


Install the plugin (compatible with sbt 0.13 and 1.0)
---------------------------------------

Add the following lines to the file ``myproject/project/plugins.sbt`` in your
project directory:

    addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "1.0.0")


Usage
-----

The following tasks and settings are automatically imported to your build:

### Tasks:

| Name                        | Description                                                                     |
| --------------------------- | -------------------------------------------------------------------------------:|
| `avroScalaGenerate`         |  Compiles the Avro files into Scala case classes.                               |
| `avroScalaGenerateScavro`   |  Compiles the Avro files into Scala case class Scavro wrapper classes.          |
| `avroScalaGenerateSpecific` |  Compiles the Avro files into Scala case classes implementing `SpecificRecord`. |

Run the tasks manually in your sbt shell, or wire them into `compile` in your `build.sbt`:

e.g., `sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue`


### Settings:

_**Standard Settings**_

| Name                       | Default                               | Description                                                      |
| -------------------------- | -------------------------------------:| ----------------------------------------------------------------:|
| `avroSourceDirectory`      | ``src/main/avro``                     | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| `avroScalaSource`          | ``$sourceManaged/main/compiled_avro`` | Path for the generated ``*.scala`` or ``*.java``  files.         |
| `avroScalaCustomTypes`     | ``Map.empty[String, Class[_]]``       | Map for reassigning `array` to `Array`, `List`, or `Vector`.     |
| `avroScalaCustomNamespace` | ``Map.empty[String, String]``         | Map for reassigning namespaces.                                  |
| `avroScalaCustomEnumStyle` | ``Map.empty[String, String]``         | Map for reassigning enum style to `java enum` or `case object`.  |

_**Scavro Settings**_

| Name                             | Default                               | Description                                                      |
| -------------------------------- | -------------------------------------:| ----------------------------------------------------------------:|
| `avroScavroSourceDirectory`      | ``src/main/avro``                     | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| `avroScavroScalaSource`          | ``$sourceManaged/main/compiled_avro`` | Path for the generated ``*.scala`` or ``*.java``  files.         |
| `avroScalaScavroCustomTypes`     | ``Map.empty[String, Class[_]]``       | Map for reassigning `array` to `Array`, `List`, or `Vector`.     |
| `avroScalaScavroCustomNamespace` | ``Map.empty[String, String]``         | Map for reassigning namespaces.                                  |
| `avroScalaScavroCustomEnumStyle` | ``Map.empty[String, String]``         | Map for reassigning enum style to `java enum` or `case object`.  |

_**SpecificRecord Settings**_

| Name                               | Default                               | Description                                                      |
| ---------------------------------- | -------------------------------------:| ----------------------------------------------------------------:|
| `avroSpecificSourceDirectory`      | ``src/main/avro``                     | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| `avroSpecificScalaSource`          | ``$sourceManaged/main/compiled_avro`` | Path for the generated ``*.scala`` or ``*.java``  files.         |
| `avroScalaSpecificCustomTypes`     | ``Map.empty[String, Class[_]]``       | Map for reassigning `array` to `Array`, `List`, or `Vector`.     |
| `avroScalaSpecificCustomNamespace` | ``Map.empty[String, String]``         | Map for reassigning namespaces.                                  |
| `avroScalaSpecificCustomEnumStyle` | ``Map.empty[String, String]``         | Map for reassigning enum style to `java enum` or `case object`.  |


Changing Settings
-----------------

Settings can be overridden by adding lines to your `build.sbt` file:

```scala    
(avroScalaSource in Compile) := new java.io.File("myScalaSource")

(avroScalaCustomTypes in Compile) := Map("array"->classOf[Array[_]])

(avroScalaCustomNamespace in Compile) := Map("example"->"overridden")

(avroScalaCustomEnumStyle in Compile) := Map("enum"->"java enum")

```


Datatypes
---------

Supports generating case classes with arbitrary fields of the following
datatypes: see [avrohugger docs - supported datatypes](https://github.com/julianpeeters/avrohugger#supports-generating-case-classes-with-arbitrary-fields-of-the-following-datatypes)


Testing
-------

Please run unit tests in `src/test/scala` with `^ test`, and integration tests
in `src/sbt-test` with `^ scripted`.


Credits
-------

`sbt-avrohugger` is based on [sbt-avro](https://github.com/cavorite/sbt-avro) by [Juan Manuel Caicedo](http://cavorite.com/), and depends on [avrohugger](https://github.com/julianpeeters/avrohugger).


Contributors
------------

- [Marius Soutier](https://github.com/mariussoutier)
- [Brennan Saeta](https://github.com/saeta)
- [Daniel Lundin](https://github.com/dln)
- [Vince Tse](https://github.com/vtonehundred)
- [Jerome Wacongne](https://github.com/ch4mpy)
- [Ryan Koval](http://github.ryankoval.com)
- [Saket](https://github.com/skate056)
- [Jon Morra](https://github.com/jon-morra-zefr)
- [Simonas Gelazevicius](https://github.com/simsasg)

#### Fork away, just make sure the tests pass before you send a pull request.


#### Criticism is appreciated.
