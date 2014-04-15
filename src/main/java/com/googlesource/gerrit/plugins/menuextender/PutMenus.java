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

import static com.googlesource.gerrit.plugins.menuextender.GetMenus.KEY_ID;
import static com.googlesource.gerrit.plugins.menuextender.GetMenus.KEY_NAME;
import static com.googlesource.gerrit.plugins.menuextender.GetMenus.KEY_TARGET;
import static com.googlesource.gerrit.plugins.menuextender.GetMenus.KEY_TOP_MENU;
import static com.googlesource.gerrit.plugins.menuextender.GetMenus.SECTION_MENU_ITEM;

import com.google.common.base.Strings;
import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.extensions.webui.TopMenu;
import com.google.gerrit.server.config.ConfigResource;
import com.google.gerrit.server.config.SitePaths;
import com.google.inject.Inject;

import com.googlesource.gerrit.plugins.menuextender.PutMenus.Input;

import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiresCapability(GlobalCapability.ADMINISTRATE_SERVER)
public class PutMenus implements RestModifyView<ConfigResource, Input> {
  public static class Input {
    List<TopMenu.MenuEntry> menus;
  }

  private final String pluginName;
  private final SitePaths sitePaths;
  private final GetMenus getMenus;

  @Inject
  public PutMenus(@PluginName String pluginName, SitePaths sitePaths,
      GetMenus getMenus) {
    this.pluginName = pluginName;
    this.sitePaths = sitePaths;
    this.getMenus = getMenus;
  }

  @Override
  public List<TopMenu.MenuEntry> apply(ConfigResource rsrc, Input input)
      throws IOException, ConfigInvalidException {
    FileBasedConfig cfg =
        new FileBasedConfig(
            new File(sitePaths.etc_dir, pluginName + ".config"), FS.DETECTED);
    cfg.load();
    cfg.clear();
    if (input.menus != null) {
      for (TopMenu.MenuEntry menuEntry : input.menus) {
        for (TopMenu.MenuItem menuItem : menuEntry.items) {
          if (menuItem.name == null) {
            continue;
          }
          cfg.setString(SECTION_MENU_ITEM, menuItem.url, KEY_NAME, menuItem.name);
          cfg.setString(SECTION_MENU_ITEM, menuItem.url, KEY_TOP_MENU, menuEntry.name);

          if (!Strings.isNullOrEmpty(menuItem.target)) {
            cfg.setString(SECTION_MENU_ITEM, menuItem.url, KEY_TARGET, menuItem.target);
          }

          if (!Strings.isNullOrEmpty(menuItem.id)) {
            cfg.setString(SECTION_MENU_ITEM, menuItem.url, KEY_ID, menuItem.id);
          }
        }
      }
    }
    cfg.save();
    return getMenus.getEntries();
  }
}
