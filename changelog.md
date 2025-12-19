------------------------------------------------------
Wathe 1.3 - 1.21.1
------------------------------------------------------
- Changed the mod identifier from "trainmurdermystery" to "wathe" and renamed it to avoid confusion with the Harpy Express modpack and reflect the mod now allowing maps other than trains
  - Previous maps should still be compatible, as block, item and entity aliases were added to transition mod identifiers
- Added a new Map Effect API class
  - Stored on the Game Component, this new class allows modders to make custom effects for maps, with custom initialize
    and finalize logic independent of the game mode
  - Using the Last Voyage of the Harpy Express as an example, the wathe:harpy_express map effects (excluding the lobby
    one) give the players the keys to their room and their letters, as well as setting the time of day and all other
    ambient effects for the train
  - /wathe:start now requires a map effect identifier to start the game
  - For instance, /wathe:start wathe:murder wathe:harpy_express_day allows you to play the murder mystery game mode
    during the day
  - Added a generic simple map effect that can be used by custom non-train maps
- Added a /wathe:mapVariables command that allows changing variables for custom maps:
  - gameModeAndMapEffect: The default game mode and map effect the map will use (with auto-start or when using the train horn).
  - spawnPos: The spawn position and orientation players will be reset to once the game ends.
  - spectatorSpawnPos: The spawn position and orientation players will be set to when set as spectators at the start of a game.
  - readyArea: The lobby area which players need to be in to be selected for a game.
  - playAreaOffset: The offset players will be teleported by from the ready area into the play area.
  - playArea: The play area outside which players will be eliminated.
  - resetTemplateArea: The template that will be copied over the play area in order to reset the map.
  - resetPasteOffset: The offset at which the template should be pasted.
- Added knife skins for Patreon and YouTube supporters
  - Can be changed by right-clicking a knife in the inventory
  - Skins added: Ceremonial Dagger and Ice Pick
- Moved weights, bounds, auto-start and backfire commands to a new /wathe:gameSettings command
- Added a new role dividend setting to /wathe:gameSettings that allows setting custom killer and vigilante counts
- Lowered the default dividend for both killers and vigilantes to 5 instead of 6
- Players can now sleep at any time of the day
- Fixed double doors not jamming together
- Fixed Derringers not replenishing on kill
- Fixed Revolvers being able to be double fired
- Fixed the ready area being slightly too small
- Tweaked the API to allow more modability (PunIsIntendeds)
- Updated French translation (PadjokeJ)
- Updated German translation (Lukas0094)
- Updated Chinese translation (ZeroIcceBear)
- Added Vietnamese translation (Vuyn)

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.6 - 1.21.1
------------------------------------------------------
- Fixed psycho ambience not going away

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.5 - 1.21.1
------------------------------------------------------
- Fixed psycho mode not stopping properly

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.4 - 1.21.1
------------------------------------------------------
- Fixed killers sometimes not being able to sprint

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.3 - 1.21.1
------------------------------------------------------
- Fixed killers not having infinite sprint
- Fixed psychosis no longer showing random items but empty hands instead
- Made creative players using the bat no longer hit people like in vanilla and instead work like in survival / adventure
- Using the bat in creative will now toggle on and off psycho mode
- API: Using -1 as a max sprint length will now make sure sprinting is infinite

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.2.2 - 1.21.1
------------------------------------------------------
- Fixed killer shop not working

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.2.1 - 1.21.1
------------------------------------------------------
- Fixed a crash when starting the game

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.2 - 1.21.1
------------------------------------------------------
- Removed money earning and starting amount from any players not killer to avoid the ability to buy items when you shouldn't
  - Additionally, also put players who manage to purchase something from the shop when they shouldn't be able to in spectator
- API: Moved death reason Identifiers to GameConstants$DeathReasons (ekulxam)

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2.1 - 1.21.1
------------------------------------------------------
- Fixed internal server error when trying to join a server once a game is already running
- Players in adventure / survival that do not have a role (e.g. if they were not there at the start of the game / joined the server while a game is running) now get automatically put in spectator

------------------------------------------------------
The Last Voyage of the Harpy Express 1.2 - 1.21.1
------------------------------------------------------
- Completely rewrote the game logic to now use a new API GameMode class, allowing for moddable new game modes with custom default start times, minimum player counts, initialization and finalization methods, and client and server game loop ticking
- Changed the Role record into a class for better custom extendability and added new role variables to allow more precise role customization:
  - moodType: Allows to set whether the mood is functional, fake (like killers), or non-functional
  - maxSprintTime: The max sprint time that a role can sprint for
  - canSeeTime: Whether a role can see the time left before the end of the game
- Turned all area game constants into a new AreasWorldComponent, allowing for customizing the map areas with add-ons:
  - spawnPos: the spawn position, yaw, and pitch in the lobby for all players after a round ends
  - spectatorSpawnPos: the spawn position, yaw, and pitch of spectators at the start of a round
  - readyArea: the box a player needs to be in the lobby to count as ready
  - playAreaOffset: the offset between the ready area and play area the players will be teleported to
  - playArea: the entirety of the play area the player around bound to, if a player falls lower than the minimum Y of this area they will be killed
  - resetTemplateArea: the area the base template for the map reset is in
  - resetPasteArea: the area where the template should be pasted for the map reset
- Added a new "backfire" mechanic and command for supporters
  - Allows you to define the chance an innocent player shooting another innocent player will cause them to shoot themselves instead
  - 0 disables the mechanic, 1 guarantees it (0.5 for instance makes the chance 50%)
- Discovery mode now requires 1+ player to be boarded to start, and loose ends requires 2+
- /start now uses the identifier of the game mode you want to start and will suggest other game modes if they are registered / added via add-ons
- Made the /start command available for everyone, restricting only Discovery and Loose Ends game modes start commands to supporters
- Removed Sprinkler ambience as it would be unreliably playing with sound physics remastered and potentially cause performance issues
- Made killer shop entries modifiable so add-ons can add new items to it
- Made buttons unable to open jammed doors (TheDeafCreeper)
- Fixed assigning an empty key to a door crashing (ekulxam)
- Fixed various server / client potential issues (ekulxam)
- Added Japanese translation (dynamiteOpanty)
- Added German translation (Lunar0sh)
- Added French translation (ElTarquin)
- Added Polish translation (huiiksde)
- Added Swedish translation (ReallySuperOtter)
- Added Hungarian translation (MokusProf)
- Added Danish translation (MrSquidson)
- Added Dutch translation (0x0Dx)
- Updated Russian translation (TheBendy42 & swqGate)

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1.5 - 1.21.1
------------------------------------------------------
- Fixed a crash that could happen on server start
- Added a Turkish translation, thanks to rjjr35379-hue 🐀❤️

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1.4 - 1.21.1
------------------------------------------------------
- Fixed lobby not being visible in ultra performance mode
- Reduced ledge outline shape and collision shape when not standing on it
- Fixed train reset sometimes not happening
- Interacting with an ornament will now interact with the block behind it, thanks to LevAlTru 🐀❤️
- Turned items that can get hallucinated into an item tag, thanks to kaleidoscopikatt 🐀❤️
- Added a new API function to allow custom roles to use Killer features, thanks to agmass 🐀❤️
- Added a Chinese translation, thanks to ZeroIcceBear 🐀❤️
- Added a Spanish (Spain) translation, thanks to TyricSatyric 🐀❤️
- Added Ukrainian translation, thanks to MagnarIUK 🐀❤️
- Updated Russian translation, thanks to TheBendy42 🐀❤️

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1.3 - 1.21.1
------------------------------------------------------
- Fixed a game crash caused by attempting to render a player head of a player you've never seen in the tab/winner menu, thanks to TheDeafCreeper 🐀❤️
- Added Norwegian (Bokmål) language support, thanks to kaleidoscopikatt 🐀❤️

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1.2.1 - 1.21.1
------------------------------------------------------
- Hotfix to prevent a server crash by removing one of the new events for addons

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1.2 - 1.21.1
------------------------------------------------------
- Added a client config option to disable screen shake
  - Removed the /wathe:setVisual screenshake option as a result
- Disabled inventory while the game is fading in or out
- Added a collection of new events designed to make it easier for addons to implement custom role abilities, and to help prevent conflicts when multiple addons are installed, thanks to PunIsIntendeds 🐀❤️
- Added Portuguese language support, thanks to GabrielFl4 🐀❤️
- Added Russian language support, thanks to ktnlvr 🐀❤️

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1.1 - 1.21.1
------------------------------------------------------
- Added a config and ultra performance config option that disables the scenery and locks the render distance to 2 chunks
- Fixed the note clear button missing text

------------------------------------------------------
The Last Voyage of the Harpy Express 1.1 - 1.21.1
------------------------------------------------------
- Added Sodium and Iris compatibility thanks to PunIsIntendeds! 🐀❤️
- Removed the framerate limit lock
- Changed role and text announcements code to be more moddable (if you wish to do custom roles or such)
- Fixed Axiom editor respawning players, spectator OP players will no longer get reset
- Fixed a crash that could happen on crowded servers
- Creative players holding a bat will now turn psycho

------------------------------------------------------
The Last Voyage of the Harpy Express 1.0.3 - 1.21.1
------------------------------------------------------
- Updated Ratatouille

------------------------------------------------------
The Last Voyage of the Harpy Express 1.0.2 - 1.21.1
------------------------------------------------------
- Made eating and drinking sounds quieter

------------------------------------------------------
The Last Voyage of the Harpy Express 1.0.1 - 1.21.1
------------------------------------------------------
- Fixed a crash when pressing tab in a world with no game previously
- Added a command to enable or disable the role weight system

------------------------------------------------------
The Last Voyage of the Harpy Express 1.0 - 1.21.1
------------------------------------------------------
Initial release