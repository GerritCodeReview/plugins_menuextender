load("//tools/bzl:plugin.bzl", "gerrit_plugin")

gerrit_plugin(
    name = "menuextender",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/**/*"]),
    manifest_entries = [
        "Gerrit-PluginName: menuextender",
        "Gerrit-Module: com.googlesource.gerrit.plugins.menuextender.Module",
    ],
)
