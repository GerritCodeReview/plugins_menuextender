// Copyright (C) 2014 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.menuextender;

import com.google.common.base.Strings;
import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.extensions.webui.TopMenu;
import com.google.gerrit.server.config.ConfigResource;
import com.google.gerrit.server.config.SitePaths;
import com.google.inject.Inject;

import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.internal.storage.file.FileSnapshot;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresCapability(GlobalCapability.ADMINISTRATE_SERVER)
public class GetMenus implements RestReadView<ConfigResource>, TopMenu {
  public final static String SECTION_MENU_ITEM = "menuItem";
  public final static String KEY_TOP_MENU = "topMenu";
  public final static String KEY_NAME = "name";
  public final static String KEY_TARGET = "target";
  public final static String KEY_ID = "id";

  private final static String DEFAULT_TOP_MENU = "Extensions";

  private final File cfgFile;

  private volatile FileSnapshot cfgSnapshot;
  private volatile FileBasedConfig cfg;

  @Inject
  public GetMenus(@PluginName String pluginName, SitePaths sitePaths) {
    this.cfgFile = sitePaths.etc_dir.resolve(pluginName + ".config").toFile();
  }

  @Override
  public List<TopMenu.MenuEntry> apply(ConfigResource rsrc) {
    return getEntries();
  }

  @Override
  public List<MenuEntry> getEntries() {
    if (cfg == null || cfgSnapshot.isModified(cfgFile)) {
      cfgSnapshot = FileSnapshot.save(cfgFile);
      cfg = new FileBasedConfig(cfgFile, FS.DETECTED);
      try {
        cfg.load();
      } catch (ConfigInvalidException | IOException e) {
        return Collections.emptyList();
      }
    }

    List<MenuEntry> menuEntries = new ArrayList<>();
    for (String url : cfg.getSubsections(SECTION_MENU_ITEM)) {
      String name = cfg.getString(SECTION_MENU_ITEM, url, KEY_NAME);
      if (Strings.isNullOrEmpty(name)) {
        continue;
      }

      String topMenu = cfg.getString(SECTION_MENU_ITEM, url, KEY_TOP_MENU);
      if (topMenu == null) {
        topMenu = DEFAULT_TOP_MENU;
      }

      String target = cfg.getString(SECTION_MENU_ITEM, url, KEY_TARGET);
      String id = cfg.getString(SECTION_MENU_ITEM, url, KEY_ID);

      menuEntries.add(new MenuEntry(topMenu,
          Collections.singletonList(new MenuItem(name, url, target, id))));
    }
    return menuEntries;
  }
}
