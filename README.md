# Millie's Server Additions
Very cool 👍

<hr>

## Why?
I made this mod initially for my community Minecraft server, but after a while decided to put some more work into it and share it with the world.

### How long will this be kept updated?
As long as the two Minecraft servers I moderate live. So pretty long I guess.

### Will this mod support other mod loaders or versions?
No. This mod is Fabric-only, and it will remain like so.<br>
It will not be ported to older versions either, and will remain updated for latest Minecraft updates only.

<hr>

# Features
### ***CONFIGURATION***
The entire mod is entirely configurable with exceptions to one command (`/discard`) and one feature (Display Armor Stands).<br>
Other than that, you can pretty much customize it however the hell you want. Config file can be found in `config\servertweaks\` directory.

### ***COMMANDS***
**For The Players**
<details>
<summary>/afk [Enabled By Default]</summary>

Allows a player to safely stay in one place while they afk.<br>
The player won't be able to move or be moved, and won't be able to damage or be damaged.<br>
<br>
Also, in config it's turned on by default that any hostile mobs that are unnamed, aren't in any vehicle and aren't being ridden<br>
will be despawned upon leaving AFK around the player.

</details>

<details>
<summary>/scale [Disabled By Default]</summary>

A fun addition for players to be able to scale themselves in-game to e.g. their irl height. (Metric system only)<br>
Min-max height values can be changed in config.<br>

Movement speed and block/entity reach do not get modified.

</details>

**For The Server Staff**
<details>
<summary>/smpperms [Disabled By Default]</summary>

Only accessible by operators.<br>
Lets you set a player's permission level without having to just yeet full operator on them.<br>
- `default` - Regular player permissions
- `moderator` - Can use all commands that a command block can
- `administrator` - Can use all commands (except `/smpperms`), can not stop the server
- `limited_operator` (owner but fair) - Operator permission level but limited to only a few commands essential (or so) for moderation
- `operator` (owner) - Regular operator permissions

</details>

<details>
<summary>/fly [Enabled By Default]</summary>

Allows creative flight in any survival-like gamemode. Can be applied to other players.

</details>

<details>
<summary>/damagetoggle [Disabled By Default]</summary>

Allows staff to disable individual damage types on the server. Resets after a server restart.

</details>

<details>
<summary>/discard [Not Configurable]</summary>

Allows to discard entities (No loot or EXP), removing blocks without them dropping anything and clearing inventories of entities and players.

</details>

<details>
<summary>/fillextras [Disabled By Default]</summary>

/fill on steroids - works just like /fill but has options such as replaceOnly, destroyOnly, silkDestroy, silkDestroyOnly, fortuneDestroy and fortuneDestroyOnly<br>
allowing staff to clear out or fill blocks within an area in ways that `/fill` can't.

</details>

<details>
<summary>/banish [Disabled By Default]</summary>

A more fun way to "ban" people by sending them into the f#cking shadow realm.<br>
A lot of safeguards and anti-abuse mechanics are in place so players can not escape in any way.<br>

Banished players can still be killed if dealt enough damage in one hit (2^18hp or more)<br>
this is so `/kill` still works if you ever need to use it in that dimension.

</details>

### ***GAMEPLAY***
<details>
<summary>Player Abilities [Disabled By Default]</summary>

An experimental feature, similar to Origins but server-side and not entirely the same as origins.<br>
Players can pick presets of species via `/abilities pickPreset <preset>` that will grant them special abilities, debuffs and such.<br>
Server moderators, admins, owners are also given tools to manage everyone's abilities within `/abilities` command.<br>
***<br>More on [PlayerAbilities.md](https://github.com/JustMili0/MilliesServerAdditions/blob/master/.Informative/Features/PlayerAbilities.md)***

</details>

<details>
<summary>Obfuscated Invisible Player Names [Enabled By Default]</summary>

Names of players that are invisible will be obfuscated in chat in death messages, whether they kill someone or die themselves.

</details>

<details>
<summary>Display Armor Stands [Not Configurable]</summary>

Naming an Armor Stand "display" gives it arms automatically, posed for holding items and gear on display.

</details>


<details>
<summary>Right-Click Harvest [Enabled By Default]</summary>

Harvest full-grown crops by just right-clicking them. Bigger harvest area when using hoes. Fortune does apply when harvesting.

</details>

<details>
<summary>AI-Be-Gone [Enabled By Default]</summary>

Naming Villagers or Tamable mobs "NoAI" will shut off their AI,<br>
which is pretty useful if you want to reduce lag in trading halls or have a lot of pets that just sit around and do nothing.

</details>

<details>
<summary>Anvil Repair [Enabled By Default]</summary>

Shift-right-click a damaged or chipped anvil with an Iron Ingot or Iron Block to repair it a stage.<br>
Iron Blocks have a much higher success chance than Ingots, and repair is always guaranteed by your 3rd attempt.

</details>

<details>
<summary>Not Too Expensive [Enabled By Default]</summary>

Remove anvil's "Too Expensive" limit, with the highest EXP cost being 39 levels.

</details>

<details>
<summary>Enchanted Book Duplication [Enabled By Default]</summary>

Shift-right-click an Enchantment Table with an Enchanted Book in your offhand and a regular Book in your main hand to duplicate the enchanted book, at the cost of some experience levels.

</details>

<details>
<summary>Enchantment Mixing [Disabled By Default]</summary>

Allows previously incompatible enchantments (different Protection types, Mending + Infinity, etc.) to be combined on the same item.

</details>

<details>
<summary>Boosted Enchantment Levels [Disabled By Default]</summary>

Some enchantments can go past their vanilla max level via an anvil (e.g. Sharpness VI, Unbreaking V, Frost Walker V). Fully customizable per-enchantment through tags.<br>
***<br>More on [BoostedEnchantmentLevels.md](https://github.com/JustMili0/MilliesServerAdditions/blob/master/.Informative/Features/BoostedEnchantmentLevels.md)***

</details>

<details>
<summary>Not Too Fast [Enabled By Default]</summary>

(Disabled `limit.....Speed` keys mean it's enabled)<br>
Individually remove speed limits for regular movement, elytra flight and vehicle movement to not be stopped by "Player moved too fast!" warnings.<br>
This is useful for player cannons for transport.

All three speed limits (elytra, on-ground movement, vehicle movement) are removed by default.

</details>

<details>
<summary>Faster Riptide Charge [Enabled By Default]</summary>

Makes the time required to hold down right-click to use a riptide trident only 3 ticks instead of 10.

</details>

<details>
<summary>Pistons Pushing Limits [Vanilla By Default]</summary>

Allows modifying how many blocks a single piston can push.

</details>

<hr>

# Fixes & Workarounds
This mod may also feature some small fixes and workarounds to annoying problems for servers and such.

<details>
<summary>MC-271325 Bug Workaround </summary>

Works around MC-271325 to prevent spam of;
```
[22:35:52] [Netty Epoll Server IO #5/ERROR]: Error sending packet clientbound/minecraft:disconnect
io.netty.handler.codec.EncoderException: Sending unknown packet 'clientbound/minecraft:disconnect'
[...]
```
in dedicated server console.<br>
This will be removed when Mojang fixes it themselves.

</details>

<hr>