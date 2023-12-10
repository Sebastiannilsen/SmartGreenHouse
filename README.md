# Smart Greenhouse

## Overview

This project comprises a network of sensor nodes, a central server, and control panels. The server acts as a communication hub, managing data flow between the sensor nodes and the control panels.

## Components

1. **Sensor Nodes:** These nodes collect environmental data (temperature, humidity, etc.) and control actuators based on commands from the control panel.
2. **Server:** It handles incoming connections from both sensor nodes and the control panel, forwarding messages between them.
3. **Control Panel:** A user interface for monitoring sensor data and controlling the actuators.

## Setup and Running

### Server

- Ensure you have Java installed.
- Compile and run the `SensorServer.java` file.
- The server will start and listen on the specified port.

### Sensor Nodes

- Each node is a separate instance of the `SensorNode` class.
- Run the SensorCommunicationHandler.java file to simulate multiple sensor nodes.

### Control Panel

- Run the `ControlPanel.java` file.
- It connects to the server and displays real-time data from sensor nodes.

## Communication Protocol

The communication between the server, sensor nodes, and control panel uses a JSON-based protocol. Detailed protocol documentation can be found in `protocol.md`.

## Features

- Real-time monitoring of sensor data.
- Remote control of actuators.
- Scalable architecture for multiple sensor nodes.

## Dependencies

- JSON library for parsing and formatting messages.
- Network libraries for socket communication.

## Future Work

- Implementing security measures for data transmission.
- Enhancing the control panel with more features and a better user interface.


