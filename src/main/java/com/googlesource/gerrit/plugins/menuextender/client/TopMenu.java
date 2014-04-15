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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import java.util.List;

public class TopMenu extends JavaScriptObject {
  public static TopMenu create(String name, List<TopMenuItem> items) {
    TopMenu m = createObject().cast();
    m.name(name);
    m.setItems(items);
    return m;
  }

  protected TopMenu() {
  }

  public final native String getName() /*-{ return this.name; }-*/;
  public final native JsArray<TopMenuItem> getItems() /*-{ return this.items; }-*/;

  public final native void name(String n) /*-{ this.name = n }-*/;

  final void setItems(List<TopMenuItem> items) {
    initItems();
    for (TopMenuItem i : items) {
      addItem(i);
    }

  }
  final native void initItems() /*-{ this.items = []; }-*/;
  final native void addItem(TopMenuItem i) /*-{ this.items.push(i); }-*/;
}
