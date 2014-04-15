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

package com.googlesource.gerrit.plugins.menuextender.client;

import com.google.gerrit.client.rpc.Natives;
import com.google.gerrit.plugin.client.Plugin;
import com.google.gerrit.plugin.client.rpc.RestApi;
import com.google.gerrit.plugin.client.screen.Screen;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuConfigurationScreen extends VerticalPanel {

  static class Factory implements Screen.EntryPoint {
    @Override
    public void onLoad(Screen screen) {
      screen.setPageTitle("Menu Configuration");
      screen.show(new MenuConfigurationScreen());
    }
  }

  private final StringListPanel menusPanel;

  MenuConfigurationScreen() {
    setStyleName("menuextender-panel");

    Button save = new Button("Save");
    menusPanel = new StringListPanel("Menu Extensions", Arrays.asList(
        "Top Menu*", "Menu Item*", "URL*", "Target", "ID Tag"), save, false);
    add(menusPanel);
    add(save);

    save.setEnabled(false);
    save.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        doSave();
      }
    });

    new RestApi("config").id("server")
        .view(Plugin.get().getPluginName(), "menus")
        .get(new AsyncCallback<TopMenuList>() {
          @Override
          public void onSuccess(TopMenuList menuList) {
            Plugin.get().refreshMenuBar();
            display(menuList);
          }

          @Override
          public void onFailure(Throwable caught) {
            // never invoked
          }
        });
  }

  private void display(TopMenuList menuList) {
    List<List<String>> valueList = new ArrayList<>();
    for (TopMenu menu : Natives.asList(menuList)) {
      for (TopMenuItem item : Natives.asList(menu.getItems())) {
        List<String> values = new ArrayList<>();
        values.add(menu.getName());
        values.add(item.getName());
        values.add(item.getUrl());
        values.add(item.getTarget());
        values.add(item.getId());
        valueList.add(values);
      }
    }

    menusPanel.display(valueList);
  }

  private void doSave() {
    List<TopMenu> menus = new ArrayList<>();
    for (List<String> v : menusPanel.getValues()) {
      TopMenuItem item = TopMenuItem.create(v.get(1), v.get(2), v.get(3), v.get(4));
      menus.add(TopMenu.create(v.get(0), Arrays.asList(item)));
    }
    MenusInput input = MenusInput.create(menus);
    new RestApi("config").id("server")
        .view(Plugin.get().getPluginName(), "menus")
        .put(input, new AsyncCallback<TopMenuList>() {
          @Override
          public void onSuccess(TopMenuList menuList) {
            Plugin.get().refresh();
          }

          @Override
          public void onFailure(Throwable caught) {
            // never invoked
          }
        });
  }
}
