# AngryBirds Project - Java LibGdx

A simple Angry Birds-inspired game created using Java with the LibGDX framework, featuring Box2D physics and custom fonts with FreeFontType generator.\
This project is being developed as part of our course project for the Advanced Programming Course(CSE201) and will help us to understand the usage of OOPS concepts in real life applications.

## Current Status
This project has been developed according to the second deadline which required Static Gui.\
Currently one can easily navigate between the game screen, the main menu, the level selector, settings, and the Levels.\
We have created Triggers for win and lose screen, which can we triggered by pressing W and L respectively, on the levels.

## Structure
- Core Module: Contains the code implementation of the application
- Assets Module: Contains the game assets as images, tiled maps, etc.

## Screens Structure Flow
- The MainMenuScreen is the initial screen of the application.
- The GameScreen is then followed which has options for selecting new/saved levels.
- The settings can be accessed through the GameScreen
- The level selector is followed allowing user to select the level.
- The level selected is then opened with the pigs birds and structure.
- Win and Lose screens can be triggered through these level screens.
  
## Dependencies 
Current status of the project requires the following dependencies - 
 - LibGDX - The main game development framework.
 - FreeTypeFontGenerator - For rendering custom fonts.
 - Box2D - Physics engine used for game object interactions.
   
## References
Resource and references used in this project:
 - LibGdx Documentation
 - YouTube - Mario Brothers tutorial for game mechanics and design inspiration
 - Stack Overflow - For troubleshooting and development support.
 - GameDev Stack Exchange - For discussions on game mechanics and physics.
   
## Setup

## Ensure Java version 17 present
https://www.java.com/download/ie_manual.jsp

## Install IDE
(IntelliJ) -- https://www.jetbrains.com/idea/ \
(Eclipse) -- https://eclipseide.org/

### How to run the game
git clone https://github.com/shivam23505/AP_Project.git \
cd AP_Project\
<br>In the terminal type the commands -- \
./gradlew build\
./gradlew run

## Contributors
Shivam Jhunjhunuwala 2023505\
Varun Golcha 2023584
