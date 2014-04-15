@PLUGIN@ Configuration
======================

The menu entries configured by the @PLUGIN@ plugin are stored in
`$site_path/etc/@PLUGIN@.config` which is a Git-style config file.

```
  [menuItem "http://code.google.com/p/gerrit/"]
    topMenu = Gerrit
    name = Homepage
```

Each menu item is stored in a `menuItem` section where the menu item
URL is used as subsection name.

<a id="topMenu">
`menuItem.<url>.topMenu`
:	The name of the top menu under which this menu item should be
	shown.

	Default is `Extensions`.

<a id="name">
`menuItem.<url>.name`
:	The name of menu item.

<a id="target">
`menuItem.<url>.target`
:	The target of the menu item link.

<a id="id">
`menuItem.<url>.id`
:	The ID of the menu item link.
