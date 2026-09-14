# Mastermind

A console-based Mastermind game written in Java.

## Features

- Easy, Medium, Hard and Custom difficulties
- Configurable code length, options and maximum tries
- Exact-position and wrong-position matching
- Input validation
- Replay with the option to keep the same settings
- Separate Model, Controller and View layers

## Technologies

- Java 17
- Maven
- IntelliJ IDEA

## How to Run

Run the `dev.roi.Main` class.

## Game Rules

The player must guess the secret code within the allowed number of tries.

- `V` = Correct position
- `O` = Correct value, wrong position
- `X` = Value not in the code