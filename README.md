## ws-clipboard-android
## Overview
This project serves as a demo to connect an Android app to WebSocket server to send/receive messages. 
It's part of an HTTP clipboard I'm building, it this project is the part responsible for sending text
snippets from the Android clipboard to a compatible server.

## App Server
In order for this application to work, you'll need to run a [server on your PC](https://github.com/chuks008/ws-clipboard-server):
Clone this project as well, and follow the instructions to start the server.

## Building the project
Before running the project, go to your gradle.properties file and fill in the value for
```aidl
AIRCLIP_PORT=<YOUR_SERVER_PORT>
```