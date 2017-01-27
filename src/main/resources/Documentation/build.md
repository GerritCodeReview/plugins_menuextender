Build
=====

This plugin is built with Bazel.
Clone or link this plugin to the plugins directory of Gerrit tree
and issue the command:

```
  cd plugins/menuextender

  cp -f external_plugin_deps.bzl ../

  cd ../../

  bazel build plugins/menuextender
```

The output is created in

```
  bazel-genfiles/plugins/menuextender/menuextender.jar
```

This project can be imported into the Eclipse IDE:

```
  cd plugins/menuextender

  cp -f external_plugin_deps.bzl ../

  cd ../../

  ./tools/eclipse/project.py
```

Note for compatibility reasons Maven build is provided, but it considered to
be deprecated and is going to be removed in one of the future versions of this
plugin.

```
  mvn clean package
```

When building with Maven, the Gerrit Plugin API must be available.
How to build the Gerrit Plugin API is described in the [Gerrit
documentation](../../../Documentation/dev-buck.html#_extension_and_plugin_api_jar_files).
