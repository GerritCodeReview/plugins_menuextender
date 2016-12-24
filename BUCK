include_defs('//bucklets/gerrit_plugin.bucklet')

MODULE = 'com.googlesource.gerrit.plugins.menuextender.MenuExtenderPlugin'

gerrit_plugin(
  name = 'menuextender',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/**/*']),
  gwt_module = MODULE,
  manifest_entries = [
    'Gerrit-PluginName: menuextender',
    'Gerrit-Module: com.googlesource.gerrit.plugins.menuextender.Module',
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.menuextender.HttpModule',
  ]
)
