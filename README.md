# RpgGame
This was my Grade 12 computer science culminating project. I made it with the help of a friend who drew most of the graphics on her ipad.
<img width="995" alt="Screenshot 2023-11-24 at 11 13 55 PM" src="https://github.com/mattlack15/RpgGame/assets/53711531/07bd6885-e5fa-401a-8fb6-0ef1596c8511">

## Gameplay
The game is quite simple and revolves around three scenes/maps/worlds. The first one is where you spawn and you embark on a quest which has multiple different objectives.
The first objective gets you started by talking to an NPC named "Queen Heidi", then she explains the goal of the quest to you. Each world/map is made out of squares and you can move around and explore.
While there is limited built space, there are no borders so technically you can just walk into the blue void endlessly. There are portals in some places marked by dark squares and if you move
into one you will be teleported to a new world/map/environment. The world also has certain structures overlayed on top of the squares, these include trees and houses. Both squares and structures
may have hitboxes or may allow you to move through them.

## Design
As mentioned before the world is made of "blocks" and has "world objects" overlayed on it. Entities are things that can exist in the world and move around such as dropped items, or players.
The UI is implemented entirely using the Graphics object from java.awt and all drawn to the same panel (I have since finally put in the effort to learn swing). Different parts of
the UI are implemented using subclasses of the Renderer class, each handles a different part of the UI like objective, hotbar, blocks or entities. The world is composed of a
fixed size array of integers representing dynamic ids for different "blocks". Much of this is similar to the way the game Minecraft is implemented as I started out programming Minecraft plugins.
WorldMap is the class that stores the blocks and is serializable/savable. It also stores a list of WorldObjects like trees and houses. There are different registries of types throughout the project
such as blocks stored in the Blocks class, or WorldObjects, or Renderers. A pub-sub style event system is also implemented and used by quests. Quests are implemented in the quest package which
contains classes like GameQuest and Objective that make creating and defining quests quite easy (it had to be easy because I had a lot of objectives to make and not a lot of time left). They are handled
as such:
- Player starts quest (default quest is started upon joining the world)
- First objective is assigned to the player
- Player completes their objective and advances to the next
Objectives can be pretty much anything, and can be implemented by creating subclasses of the Objective class. There are currently three types of objectives implemented:
- DialogObjective: Talk to a certain NPC and complete a certain Dialog, both of which are specified in the constructor of the objective.
- FindObjective: Move to a certain area
- GetItemObjective: Pick up a certain amount of a certain type of item (specified in the constructor as an ItemType and integer amount)

## World editing
There are implementations for certain world editing classes in the project. They are not accessible at the moment in-game and were just used for creating the maps when I was
completing the project.
