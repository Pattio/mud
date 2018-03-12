#!/bin/bash  

# Compile all the files
make

# Load Settings
source ./settings.txt

# Launch GameServer
java MUD.Server.GameServer $RMI_REGISTRY_PORT $SERVER_PORT
