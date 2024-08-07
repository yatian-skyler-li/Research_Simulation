# A simulation of a researcher exploring an interactable scenario
The point of this simulation is for a user to navigate an environment and collect research on its inhabitants. We mainly focus on class building and JUnit test in this project.

## Language requirements
Java version 11, JUnit 4.

## Introduction of Classes
There are six classes available in this project under src\researchsim directory, including entities, scenario, map, logging, util, and display. 

A primary class in this simulation is the Scenario. A scenario is used to maintain a record of all inhabitants of an environment. The scenario also maintains a record of all movements and interactions between the scenario's inhabitants. Scenarios loaded into the simulation are kept track of using a singleton scenario manager.

A scenario's environment is made up of tiles. Graphically these tiles work together to form a 2 dimensional grid, but the scenario stores them in a single array. These tiles represent different types of terrain, for example: grassy, ocean, or sandy. Tiles can also have an inhabitant (entity) living within them. 

An entity is separated into two main types: flora (plants) and fauna (animals). A tile's characteristics dene what entities can inhabit that tile. Some animals, such as a fish, can only live in water based tiles while others, such as a dog, can only live in land based tiles.

A tile is located using a coordinate system (similar to the cartesian plane). This coordinate facilitates the conversion between a two dimensional view of the scenario and the tile array index.

To represent the interactions between entities, the scenario uses the Event classes, specically MoveEvent and CollectEvent. An Event maintains a record of the locations that an entity interacts with. A MoveEvent is classied as an entity leaving its current tile and inhabiting a new (unoccupied) tile.

An entity's characteristics are dened by its size. Larger entities are able to travel a further distance in a single movement. Accordiningly, the user gains a great out of research for studying larger entities than if they were small entities.

## JUnit Test
The JUnit tests that we provide in test\researchsim directory were used to test both correct and incorrect implementations of entities, scenario, map, ans logging classes.

## Credit
This project was originally designed by Dr.Thomas Christy, School of ITEE, University of Queensland. The scripts are independently developed by Skyler Li.
