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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwtexpui.globalkey.client.NpTextBox;

import java.util.ArrayList;
import java.util.List;

public class StringListPanel extends FlowPanel {
  private final StringListTable t;
  private final HorizontalPanel titlePanel;
  protected final HorizontalPanel buttonPanel;
  private final Button deleteButton;
  private Image info;
  protected FocusWidget widget;

  public StringListPanel(String title, List<String> fieldNames, FocusWidget w,
      boolean autoSort) {
    widget = w;
    titlePanel = new HorizontalPanel();
    Label titleLabel = new Label(title);
    titleLabel.setStyleName("menuextender-smallHeading");
    titlePanel.add(titleLabel);
    add(titlePanel);

    t = new StringListTable(fieldNames, autoSort);
    add(t);

    buttonPanel = new HorizontalPanel();
    buttonPanel.setStyleName("menuextender-stringListPanelButtons");
    deleteButton = new Button("Delete");
    deleteButton.setEnabled(false);
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        widget.setEnabled(true);
        t.deleteChecked();
      }
    });
    buttonPanel.add(deleteButton);
    add(buttonPanel);
  }

  public void display(List<List<String>> values) {
    t.display(values);
  }

  public void setInfo(String msg) {
    if (info == null) {
      info = new Image(MenuExtenderPlugin.RESOURCES.info());
      titlePanel.add(info);
    }
    info.setTitle(msg);
  }

  public List<List<String>> getValues() {
    return t.getValues();
  }

  private class StringListTable extends FlexTable {
    private final List<NpTextBox> inputs;
    private final boolean autoSort;

    StringListTable(List<String> names, boolean autoSort) {
      this.autoSort = autoSort;

      Button addButton =
          new Button(new ImageResourceRenderer().render(MenuExtenderPlugin.RESOURCES.listAdd()));
      addButton.setTitle("Add");
      OnEditEnabler e = new OnEditEnabler(addButton);
      inputs = new ArrayList<>();

      setStyleName("menuextender-stringListTable");
      FlexCellFormatter fmt = getFlexCellFormatter();
      fmt.addStyleName(0, 0, "iconHeader");
      fmt.addStyleName(0, 0, "leftMostCell");
      for (int i = 0; i < names.size(); i++) {
        fmt.addStyleName(0, i + 1, "dataHeader");
        setText(0, i + 1, names.get(i));

        NpTextBox input = new NpTextBox();
        input.setVisibleLength(35);
        input.addKeyPressHandler(new KeyPressHandler() {
          @Override
          public void onKeyPress(KeyPressEvent event) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
              widget.setEnabled(true);
              add();
            }
          }
        });
        inputs.add(input);
        fmt.addStyleName(1, i + 1, "dataHeader");
        setWidget(1, i + 1, input);
        e.listenTo(input);
      }
      addButton.setEnabled(false);

      addButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          widget.setEnabled(true);
          add();
        }
      });
      fmt.addStyleName(1, 0, "iconHeader");
      fmt.addStyleName(1, 0, "leftMostCell");
      setWidget(1, 0, addButton);

      if (!autoSort) {
        fmt.addStyleName(0, names.size() + 1, "iconHeader");
        fmt.addStyleName(0, names.size() + 2, "iconHeader");
        fmt.addStyleName(1, names.size() + 1, "iconHeader");
        fmt.addStyleName(1, names.size() + 2, "iconHeader");
      }
    }

    void display(List<List<String>> values) {
      for (int row = 2; row < getRowCount(); row++) {
        removeRow(row--);
      }
      int row = 2;
      for (List<String> v : values) {
        populate(row, v);
        row++;
      }
      updateNavigationLinks();
    }

    List<List<String>> getValues() {
      List<List<String>> values = new ArrayList<>();
      for (int row = 2; row < getRowCount(); row++) {
        values.add(getRowItem(row));
      }
      return values;
    }

    protected List<String> getRowItem(int row) {
      List<String> v = new ArrayList<>();
      for (int i = 0; i < inputs.size(); i++) {
        v.add(getText(row, i + 1));
      }
      return v;
    }

    private void populate(final int row, List<String> values) {
      FlexCellFormatter fmt = getFlexCellFormatter();
      fmt.addStyleName(row, 0, "leftMostCell");
      fmt.addStyleName(row, 0, "iconCell");
      CheckBox checkBox = new CheckBox();
      checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
          enableDelete();
        }
      });
      setWidget(row, 0, checkBox);
      for (int i = 0; i < values.size(); i++) {
        fmt.addStyleName(row, i + 1, "dataCell");
        setText(row, i + 1, values.get(i));
      }
      if (!autoSort) {
        fmt.addStyleName(row, values.size() + 1, "iconCell");
        fmt.addStyleName(row, values.size() + 2, "dataCell");

        Image down = new Image(MenuExtenderPlugin.RESOURCES.arrowDown());
        down.setTitle("Down");
        down.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            moveDown(row);
          }
        });
        setWidget(row, values.size() + 1, down);

        Image up = new Image(MenuExtenderPlugin.RESOURCES.arrowUp());
        up.setTitle("Up");
        up.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            moveUp(row);
          }
        });
        setWidget(row, values.size() + 2, up);
      }
    }

    void moveDown(int row) {
      if (row < getRowCount() - 1) {
        swap(row, row + 1);
      }
    }

    void moveUp(int row) {
      if (row > 2) {
        swap(row - 1, row);
      }
    }

    void swap(int row1, int row2) {
      List<String> value = getRowItem(row1);
      List<String> nextValue = getRowItem(row2);
      populate(row1, nextValue);
      populate(row2, value);
      updateNavigationLinks();
      widget.setEnabled(true);
    }

    private void updateNavigationLinks() {
      if (!autoSort) {
        for (int row = 2; row < getRowCount(); row++) {
          getWidget(row, inputs.size() + 1).setVisible(
              row < getRowCount() - 1);
          getWidget(row, inputs.size() + 2).setVisible(row > 2);
        }
      }
    }

    void add() {
      List<String> values = new ArrayList<>();
      for (NpTextBox input : inputs) {
        values.add(input.getValue().trim());
        input.setValue("");
      }
      insert(values);
    }

    void insert(List<String> v) {
      int insertPos = getRowCount();
      if (autoSort) {
        for (int row = 1; row < getRowCount(); row++) {
          int compareResult = v.get(0).compareTo(getText(row, 1));
          if (compareResult < 0)  {
            insertPos = row;
            break;
          } else if (compareResult == 0) {
            return;
          }
        }
      }
      insertRow(insertPos);
      populate(insertPos, v);
      updateNavigationLinks();
    }

    void enableDelete() {
      for (int row = 2; row < getRowCount(); row++) {
        if (((CheckBox) getWidget(row, 0)).getValue()) {
          deleteButton.setEnabled(true);
          return;
        }
      }
      deleteButton.setEnabled(false);
    }

    void deleteChecked() {
      deleteButton.setEnabled(false);
      for (int row = 2; row < getRowCount(); row++) {
        if (((CheckBox) getWidget(row, 0)).getValue()) {
          removeRow(row--);
        }
      }
      updateNavigationLinks();
    }
  }
}
