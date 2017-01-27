load("//tools/bzl:plugin.bzl", "gerrit_plugin")

gerrit_plugin(
    name = "menuextender",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/**/*"]),
    gwt_module = "com.googlesource.gerrit.plugins.menuextender.MenuExtenderPlugin",
    manifest_entries = [
        "Gerrit-PluginName: menuextender",
        "Gerrit-Module: com.googlesource.gerrit.plugins.menuextender.Module",
        "Gerrit-HttpModule: com.googlesource.gerrit.plugins.menuextender.HttpModule",
    ],
)
