# Multi-User Dungeon

Multiplayer real-time text-based virtual world game written in **Java** using **RMI API**.

## Running instructions

* Open `settings.txt` file and change rmi registry and server ports to your desired ones, also change server address to your server address, if you don’t know the address don’t worry just follow the next steps.
* Use unix based shell and type `./startServer.sh` now all the files will be compiled and server will start running, you will see message “*Server is successfully running on hostname: **xxxxxxx***”, this is the server address you should be using in `settings.txt` file, so if you haven’t changed it yet, change it now before you start the client.
* Again, using unix based shell type `./startClient.sh` now the client will start running

***Note**: you don’t need to start rmiregistry manually, because server is handling it automatically i.e. it tries to create it if it wasn’t created yet, otherwise it will just use existing one.*

## Features

* User can move in any direction in MUD by writing `move [north, south, east, west]`
* User can see other users by typing `see`
* User can pick up items in the mud by typing command `pick` followed by item name
* User can create MUD at runtime
* Users are able to create any number of MUDs
* When player launches game client, player sees all the servers with number of online
players in each server, also player sees number of total online player and max number of allowed online players
<img src="/Preview/welcome.png" width="600" alt="Welcome screen"></img>
* Account system. When user selects server, user is asked to enter username and password. If that username is not already used, new account is created, otherwise password is checked. If password is incorrect error message is displayed. If password is correct, but someone is already logged in into account, error message is displayed
<img src="/Preview/login.png" width="600" alt="Login screen"></img>
* User can see inventory by typing `inventory`
* User can see how many players are online on current server by typing `online`
* Real time updates. Client is polling server to ask if there are any new events and event manager is responsible for storing all the new events for all the players. Because of this update cycle implementation server doesn’t need to open new RMI connection for each client to send the updates.
  * User connects to server which you already playing in
  <img src="/Preview/real_time_updates_1.png" width="600" alt="Real time updates"></img>
  * Another user picks item near you
  <img src="/Preview/real_time_updates_2.png" width="600" alt="Real time updates"></img>
  * Another user moves to your current location from other location
  <img src="/Preview/real_time_updates_3.png" width="600" alt="Real time updates"></img>
* User can see all available commands by typing `help`
<img src="/Preview/commands.png" width="600" alt="Available commands"></img>
* If user terminal/computer crashes, user is automacillay removed from the server
* Persistent storage for the accounts, so all the accounts are serialized and saved in (`Storage/accounts`) file. It means that current location, inventory items and other details are saved
* Persistent storage for the muds, so all the muds are serialized and saved in (`Storage/muds`) file
* Server tracks connects/disconnects in real time
<img src="/Preview/server.png" width="600" alt="Server"></img>
* Client is as lean as possible, i.e. almost everything is handled on the server side. This lets add new updates to the game without needing to update the client.
