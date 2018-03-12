#!/bin/bash  

# Load Settings
source ./settings.txt

# Launch GameServer
java MUD.Client.GameClient $RMI_REGISTRY_PORT $SERVER_ADDRESS
