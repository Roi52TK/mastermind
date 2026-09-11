package dev.roi.mastermind.controller;

import dev.roi.mastermind.common.GameActionError;

public interface GameUI {

    void onGameWon();

    void onGameLost();

    void onInvalidInteraction(GameActionError error);
}
