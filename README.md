# AngryBirds Project - Java LibGdx

A simple Angry Birds-inspired game created using Java with the LibGDX framework, featuring Box2D physics and custom fonts with FreeFontType generator.\
This project is being developed as part of our course project for the Advanced Programming Course(CSE201) and will help us to understand the usage of OOPS concepts in real life applications.

## Structure
- Core Module: Contains the code implementation of the application
- Assets Module: Contains the game assets as images, tiled maps, etc.

## Screens Structure Flow
- The MainMenuScreen is the initial screen of the application.
- The GameScreen is then followed which has options for selecting new/saved levels.
- The settings can be accessed through the GameScreen
- The level selector is followed allowing user to select the level.
- The level selected is then opened with the pigs birds and structure.
- Press P to open the Pause Menu.
- Win and Lose screens can be triggered through these level screens.
- Levels can be saved through the pause menu.
- Saved levels can be loaded from the load game on game screen.

## Design Patterns
### Template Pattern
Template Pattern has been used in the blocks and pigs class independently by creating a fixture def from the parent class.\
This allows us to get different variables and density as required.\
Also health and damage have been set differently as required by the different blocks and pigs.

### Proxy Pattern
This pattern has been used while serialisation and saving the levels.\
The saved class is called upon when loading the game and contains different attributes which are used when the load game is called.\

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
