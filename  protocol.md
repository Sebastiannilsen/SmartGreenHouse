# Project Communication Protocol Documentation

## Overview

This document outlines the communication protocol used in our sensor network project. The project consists of a server,
multiple sensor nodes, and multiple control panels. The server acts as a central hub, managing communication between the
control panels and the sensor nodes.

---

## Communication Participants / Terminology

- **Server:** Central hub that handles incoming connections and forwards messages between sensor nodes and the control
  panel.
- **Sensor Node:** Client that sends sensor data to the server and receives actuator commands.
- **Control Panel:** Client that receives sensor data from the server and sends actuator commands.

---

## Transport and Port
- **Transport:** TCP (Transmission Control Protocol)
- **Port Number:** 12345 (Can be changed in the main method of the different classes)

---

## Architecture
![Diagram of the architecture](/src/main/resources/structure.png)


---

## Protocol Type
- **Connection-oriented:** The protocol uses TCP, ensuring a reliable connection.
- **Statefulness:** The protocol is stateful as it maintains the state of each sensor node and actuator.

---

## Communication Details

### 1. Establishing Connections

#### Sensor Node Connection

- Sensor nodes identify themselves with a message containing their unique ID.
- It sends a message `SENSOR_NODE`, followed by a message containing its unique ID.

#### Control Panel Connection

- The control panel identifies itself without needing a unique ID.
- It sends a message `CONTROL_PANEL`.

### 2. Sensor Data Transmission

#### From Sensor Node to Server

- Sensor nodes periodically send their sensor data.
- Format:
  ```json
  {
      "id": [Sensor Node ID],
      "sensors": [
          {
              "id": [Sensor ID],
              "type": [Sensor Type],
              "value": [Sensor Value]
          },
          // More sensors
      ],
      "actuators": [
          {
              "id": [Actuator ID],
              "type": [Actuator Type],
              "isOn": [Actuator State]
          },
          // More actuators
      ]
      
  }
  ```

### 3. Actuator Command Transmission

#### From Control Panel to Server

- The control panel sends actuator commands to the server.
- Format:
  ```json
  {
      "id": [Sensor Node ID],
      "type": [ACTUATOR_TYPE],
      "value" : [Actuator Value],
      "actuatorId" : [Actuator ID]
  }
  ```

#### From Server to Sensor Node

- The server forwards actuator commands to the respective sensor node.
- The format is identical to the one used by the control panel.

---

## Error Handling
- **Unexpected Message Format:** Such messages are logged and ignored.
- **Connection Issues:** Attempt reconnection; log errors.

---

## Connection Lifecycle

1. **Sensor nodes and the control panel connect to the server.**
2. **Sensor nodes send their data periodically to the server.**
3. **The server forwards this data to the control panel.**
4. **The control panel sends actuator commands to the server.**
5. **The server forwards these commands to the respective sensor nodes.**

---

## Reliability Mechanisms
- TCP's inherent mechanisms for ensuring message delivery and order.



