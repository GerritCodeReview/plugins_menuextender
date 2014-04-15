@PLUGIN@ - /config/ REST API
============================

This page describes the REST endpoints that are added by the @PLUGIN@
plugin.

Please also take note of the general information on the
[REST API](../../../Documentation/rest-api.html).

<a id="config-endpoints"> Config Endpoints
------------------------------------------

### <a id="get-menus"> Get Menus
_GET /config/\{server\}/@PLUGIN@~menus/_

Gets the additional menus entries.

#### Request

```
  GET /config/server/@PLUGIN@~menus/ HTTP/1.0
```

As response a list of [TopMenuEntryInfo](../../../Documentation/rest-api-config.html#top-menu-entry-info)
entities is returned that describe the additional menu entries.

#### Response

```
  HTTP/1.1 200 OK
  Content-Disposition: attachment
  Content-Type: application/json;charset=UTF-8

  )]}'
  [
    {
      "name": "Gerrit",
      "items": [
        {
          "url": "http://code.google.com/p/gerrit/",
          "name": "Homepage"
        }
      ]
    }
  ]
```

### <a id="set-menus"> Set Menus
_PUT /config/\{server\}/@PLUGIN@~menus/_

Sets the additional menus entries. Any additional menu entries which
have been set before are overwritten.

The additional menu entries must be provided in the request body as a
[MenusInput](#menus-input) entity.

#### Request

```
  PUT /config/server/@PLUGIN@~menus/ HTTP/1.0
  Content-Type: application/json;charset=UTF-8

  {
    "menus": [
      {
        "name": "Gerrit",
        "items": [
          {
            "url": "http://code.google.com/p/gerrit/",
            "name": "Homepage"
          }
        ]
      }
    ]
  }
```

As response a list of [TopMenuEntryInfo](../../../Documentation/rest-api-config.html#top-menu-entry-info)
entities is returned that describe the additional menu entries.

#### Response

```
  HTTP/1.1 200 OK
  Content-Disposition: attachment
  Content-Type: application/json;charset=UTF-8

  )]}'
  [
    {
      "name": "Gerrit",
      "items": [
        {
          "url": "http://code.google.com/p/gerrit/",
          "name": "Homepage"
        }
      ]
    }
  ]
```

<a id="json-entities">JSON Entities
-----------------------------------

### <a id="menus-input"></a>MenusInput

The `MenusInput` entity contains information about additional menu entries.

* _menus_: List of [TopMenuEntryInfo](../../../Documentation/rest-api-config.html#top-menu-entry-info) entities.
