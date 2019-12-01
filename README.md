# sbt-avrohugger


[![Travis CI](https://travis-ci.org/julianpeeters/sbt-avrohugger.svg?branch=master)](https://travis-ci.org/julianpeeters/sbt-avrohugger)
[![Join the chat at https://gitter.im/julianpeeters/avrohugger](https://badges.gitter.im/julianpeeters/avrohugger.svg)](https://gitter.im/julianpeeters/avrohugger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.julianpeeters/sbt-avrohugger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.julianpeeters/sbt-avrohugger)


sbt plugin for generating Scala case classes and ADTs from Apache Avro schemas, datafiles, and protocols.


Install the plugin (compatible with sbt 1.x)
---------------------------------------

Add the following lines to the file ``myproject/project/plugins.sbt`` in your
project directory:

    addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.0.0-RC20")
    
    
_NOTE:_ On **Nexus**, please set nexus proxy layout to `permissive` in to resolve artifacts with a sbt-version suffixes


Usage
-----

The following tasks and settings are automatically imported to your build:

### Tasks:

| Name                        | Description                                                                     |
| --------------------------- | -------------------------------------------------------------------------------:|
| `avroScalaGenerate`         |  Compiles the Avro files into Scala case classes.                               |
| `avroScalaGenerateScavro`   |  Compiles the Avro files into Scala case class Scavro wrapper classes.          |
| `avroScalaGenerateSpecific` |  Compiles the Avro files into Scala case classes implementing `SpecificRecord`. |

#### Compile

Wire the tasks into `compile` in your `build.sbt`:

e.g.: `sourceGenerators in Compile += (avroScalaGenerate in Compile).taskValue`

By [default](https://github.com/julianpeeters/sbt-avrohugger#settings), the plugin looks Avro files in `src/main/avro` and generates Scala files in `$sourceManaged`, e.g., `target/scala-2.12/src_managed/main/compiled_avro/` (to choose different locations, please see [Changing Settings](https://github.com/julianpeeters/sbt-avrohugger#changing-settings)).

#### Test

And/Or wire the tasks into the `Test` config, putting Avro files in `src/test/avro`:

e.g. `sourceGenerators in Test += (avroScalaGenerate in Test).taskValue`

#### Manually

To run the tasks manually, please see [Changing Settings](https://github.com/julianpeeters/sbt-avrohugger#changing-settings) or the [sbt docs](http://www.scala-sbt.org/1.x/docs/Howto-Customizing-Paths.html#Add+an+additional+source+directory) in order to ensure the compiler will be able to find the generated files.


#### Watch Avro Files

To enable file-watching for avro files, e.g. in `~compile`, use:

e.g.: `watchSources ++= ((avroSourceDirectory in Compile).value ** "*.avdl").get`


### Settings:

_**Standard Settings**_

| Name                       | Default                               | Description                              |
| -------------------------- | -------------------------------------:| ----------------------------------------:|
| `avroSourceDirectories`    | ``Seq("src/main/avro")``              | List of paths containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files.|
| `avroScalaSource`          | ``$sourceManaged/main/compiled_avro`` | Path for the generated ``*.scala`` or ``*.java``  files.|
| `avroScalaCustomTypes`     | ``Standard.defaultTypes``             | Customizable Type Mapping.|
| `avroScalaCustomNamespace` | ``Map.empty[String, String]``         | Map for reassigning namespaces.|


_**Scavro Settings**_

| Name                             | Default                               | Description                   |
| -------------------------------- | -------------------------------------:| -----------------------------:|
| `avroScavroSourceDirectories`    | ``Seq("src/main/avro")``              | List of paths containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| `avroScavroScalaSource`          | ``$sourceManaged/main/compiled_avro`` | Path for the generated ``*.scala`` or ``*.java``  files.         |
| `avroScalaScavroCustomTypes`     | ``Scavro.defaultTypes``               | Customizable Type Mapping.|
| `avroScalaScavroCustomNamespace` | ``Map.empty[String, String]``         | Map for reassigning namespaces.                                  |


_**SpecificRecord Settings**_

| Name                               | Default                               | Description                                                      |
| ---------------------------------- | -------------------------------------:| ----------------------------------------------------------------:|
| `avroSpecificSourceDirectories`    | ``Seq("src/main/avro")``              | Path containing ``*.avsc``, ``*.avdl``, and/or ``*.avro`` files. |
| `avroSpecificScalaSource`          | ``$sourceManaged/main/compiled_avro`` | Path for the generated ``*.scala`` or ``*.java``  files.         |
| `avroScalaSpecificCustomTypes`     | ``SpecificRecord.defaultTypes``             | Customizable Type Mapping.|
| `avroScalaSpecificCustomNamespace` | ``Map.empty[String, String]``         | Map for reassigning namespaces.                                  |


Changing Settings
-----------------

Settings for each format's task can be extended/overridden by adding lines to your `build.sbt` file.


E.g., to change how classes of `SpecificRecords` format are generated, use:

```scala    
avroSpecificSourceDirectories in Compile += (sourceDirectory in Compile).value / "myavro"

avroSpecificScalaSource in Compile := new java.io.File("myScalaSource")

avroScalaSpecificCustomNamespace in Compile := Map("example"->"overridden")

avroScalaSpecificCustomTypes in Compile := {
  avrohugger.format.SpecificRecord.defaultTypes.copy(
    array = avrohugger.types.ScalaVector)
}
```

* `record` can be assigned to `ScalaCaseClass` and `ScalaCaseClassWithSchema` (with schema in a companion object)
* `array` can be assigned to `ScalaSeq`, `ScalaArray`, `ScalaList`, and `ScalaVector`
* `enum` can be assigned to `JavaEnum`, `ScalaCaseObjectEnum`, `EnumAsScalaString`, and `ScalaEnumeration`
* `union` can be assigned to `OptionEitherShapelessCoproduct` and `OptionalShapelessCoproduct`
* `int`, `long`, `float`, `double` can be assigned to `ScalaInt`, `ScalaLong`, `ScalaFloat`, `ScalaDouble`
* `date` logical type can be assigned to `JavaTimeLocalDate` and `JavaSqlDate`
* `timestamp-millis` logical type can be assigned to `JavaTimeInstant` and `JavaSqlTimestamp`
* `uuid` logical type can be assigned to `UUID`
* `decimal` can be assigned to e.g. `ScalaBigDecimal(Some(BigDecimal.RoundingMode.HALF_EVEN))` and `ScalaBigDecimalWithPrecision(None)` (via Shapeless Tagged Types)
* `protocol` can be assigned to `ScalaADT` and `NoTypeGenerated` (see [Protocol Support](https://github.com/julianpeeters/avrohugger#protocol-support))

Datatypes
---------

Supports generating case classes with arbitrary fields of the following
datatypes: see [avrohugger docs - supported datatypes](https://github.com/julianpeeters/avrohugger#supports-generating-case-classes-with-arbitrary-fields-of-the-following-datatypes)


Testing
-------

Please run unit tests in `src/test/scala` with `^ test`, and integration tests
in `src/sbt-test` with `^ scripted sbt-avrohugger/*`, or, e.g. `scripted sbt-avrohugger/specific`.


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
- [Raúl Raja Martínez](https://github.com/raulraja)
- [Paul Snively](https://github.com/PaulAtBanno)
- [Zach Cox](https://github.com/zcox)
- [Marco Stefani](https://github.com/inafets)
- [Diego E. Alonso Blas](https://github.com/diesalbla)
- [Chris Albright](https://github.com/chrisalbright)
- [Sacha Barber](https://github.com/sachabarber)
- [Andrew Gustafson](https://github.com/agustafson)
- [Fede Fernández](https://github.com/fedefernandez)
- [sullis](https://github.com/sullis)

#### Fork away, just make sure the tests pass before you send a pull request.


#### Criticism is appreciated.
