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
  - Removed the /tmm:setVisual screenshake option as a result
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