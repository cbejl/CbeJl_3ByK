# CbeJl_3ByK <sub>(eat_sound)</sub>
[![ru](https://img.shields.io/badge/lang-ru-blue.svg)](README_ru.md)

Easy mob-mute plugin for your server <sub>*(developed and tested on paper 1.21.7)*</sub>

---
## How to mute mob?

1. Rename nametag to one of ```mute_nametags``` [(from plugin config)](src/main/resources/config.yml)
2. Right-click the nametag on the mob
3. **PROFIT**

---
## How unmute mob?

1. Just right-click on mob with ```unmute_items``` in hand [(from plugin config)](src/main/resources/config.yml)
2. **PROFIT**

---

### More info about config

- ```mute_nametags``` - names for nametag to mute mob
- ```unmute_items``` - items for unmute mobs by right-click
- ```cancel_rename_event``` - should plugin cancel the name change of the mob when muting? (if true - mob don`t change name on mute)
- ```mute_blacklist``` - the list of mobs that cannot be muted by the plugin