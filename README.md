# Quaint

A variety of small additions and resources, potentially for use in modpacks.

## Features

### Items

- Heart of the *(Element)*:
  - Items akin to the Heart of the Sea that are mostly without an official use.
    Useful in custom recipes. Some can be looted.
- Staff of the *(Weather Type)*:
  - Staves that change the weather to the described weather type.
  - Each crafted from their respective "heart" item.
  - Cooldowns can be configured in the server config.
- Mystery items:
  - Mystery Seeds, which gives a random seed type on consume.
  - Mystery Sapling, which gives a random sampling type on consume.
  - Mystery Box, which must be given a `[quaint:loot_table="..."]` component,
    and gives the described loot table on consume.

### Effects
- Doom:
  - Kills the user when the effect ends, can be cleared with Milk.
  - Can be brewed with the Dubious Decoction item.
- Clarity:
  - Clears all status effects.
  - Can be brewed with a Milk Bucket.
- Camouflaged:
  - Prevents mobs from targeting the user.
    - Can be overriden by the `quaint:sees_through_camouflage` entity type tag.
  - Removed if the user attacks something.
  - Can be configured by the `doGiveCamouflagedOnSpawn` game rule to be given on spawn.
    - Duration can be configured in the server config.

### Other
- A new command, `/smite`, which strikes an entity or position with a lightning bolt that doesn't spawn fire.
- A game rule, `keepExperience`, which, similar to `keepInventory`, keeps only experience upon spawn (experimental).

## Credit
The icon for the Mystery Box item is based on the "One Box" item icon from
the amazing [Enigmatic Legacy](https://github.com/Aizistral-Studios/Enigmatic-Legacy)
by Aizistral. I do not own Enigmatic Legacy or claim to.

## License

This mod is licensed under the MIT license.
