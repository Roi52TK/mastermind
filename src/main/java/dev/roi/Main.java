package dev.roi;

import dev.roi.mastermind.controller.Controller;
import dev.roi.mastermind.view.console.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI();
        Controller controller = new Controller(consoleUI);
        consoleUI.setController(controller);
        consoleUI.run();
    }
}