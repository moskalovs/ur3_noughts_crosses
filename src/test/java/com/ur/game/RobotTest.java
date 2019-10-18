package com.ur.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ur.game.Board.*;

class RobotTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findMadeMove() {

        State[][] prev = {
                {State.Blank, State.X, State.X},
                {State.O, State.Blank, State.O},
                {State.Blank, State.X, State.X},
        };


        State[][] current = {
                {State.Blank, State.X, State.X},
                {State.O, State.X, State.O},
                {State.O, State.X, State.X},
        };


        findMadeMove(prev, current);

    }

    private byte[] findMadeMove(State[][] prev, State[][] curr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boolean hasDifferent = !prev[i][j].equals(curr[i][j]);
                if (hasDifferent) {
                    int index = (i * 3) + j;
                    byte figure = curr[i][j].equals(State.X) ? (byte) 1 : 0;
                    return new byte[]{(byte) index, figure};
                }
            }
            System.out.println("");
        }
        return new byte[]{-1, -1};
    }


}