package com.ur.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ur.game.ArtificialIntelligence.Algorithms;
import com.ur.network.ConnectionCatcher;
import com.ur.network.ConnectionPoint;
import com.ur.ur.URCommand;
import com.ur.ur.URCommander;
import com.ur.ur.URReport;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * For playing Tic Tac Toe in the console.
 */
public class Robot {

    private Board board;
    private Board prevBoard;
    private Scanner sc = new Scanner(System.in);
    private URCommander urCommander;

    private HashMap<Integer, Byte[]> winIndexMap = new HashMap<Integer, Byte[]>() {
        {
            put(0, new Byte[]{0, 2});
            put(1, new Byte[]{3, 5});
            put(2, new Byte[]{6, 8});
            put(3, new Byte[]{0, 8});
            put(4, new Byte[]{0, 6});
            put(5, new Byte[]{1, 7});
            put(6, new Byte[]{2, 8});
            put(7, new Byte[]{2, 6});
        }
    };

    /**
     * Construct Console.
     */
    private Robot(URCommander urCommander) {
        this.urCommander = urCommander;
        board = new Board();
    }

    /**
     * Begin the game.
     */
    private void play() throws IOException {
        System.out.println("Starting a new game.");
        urCommander.runCommand("WAIT_START_NEW_GAME");
        while (true) {
            printGameStatus();
            playMove();
            if (board.isGameOver()) {
                printWinner();
                if (!tryAgain()) {
                    break;
                }
            }
        }
    }

    /**
     * Handle the move to be played, either by the player or the AI.
     */
    private void playMove() throws IOException {
        if (board.getTurn() == Board.State.X) {
            getPlayerMove();
        } else {
            Algorithms.alphaBetaAdvanced(board);
        }
    }


    /**
     * Print out the board and the player who's turn it is.
     */
    private void printGameStatus() throws IOException {

        System.out.println("\n" + board + "\n");
        System.out.println(board.getTurn().name() + "'s turn.");

        robotDrawMove();
        prevBoard = board.getDeepCopy();

    }

    private void robotDrawMove() throws IOException {
        if (prevBoard == null) {
            urCommander.runCommand("GET_MARKER", new byte[]{0, -1, -1});
            urCommander.runCommand("DRAW_FIELD");
            urCommander.runCommand("PUT_MARKER", new byte[]{0, -1, -1});
            urCommander.runCommand("GET_MARKER", new byte[]{1, -1, -1});
            urCommander.runCommand("GO_READY_TO_DRAW_POS");
        } else {
            byte[] madeMove = findMadeMove(prevBoard.toArray(), board.toArray());
            if (madeMove != null) {
                urCommander.runCommand("MAKE_MOVE", new byte[]{madeMove[0], madeMove[1], -1});
                urCommander.runCommand("GO_READY_TO_DRAW_POS");
            }
        }
    }

    private byte[] findMadeMove(Board.State[][] prev, Board.State[][] curr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boolean hasDifferent = !prev[i][j].equals(curr[i][j]);
                if (hasDifferent) {
                    int index = (i * 3) + j;
                    byte figure = curr[i][j].equals(Board.State.X) ? (byte) 1 : 0;
                    return new byte[]{figure, (byte) index};
                }
            }
            System.out.println("");
        }
        return null;
    }


    /**
     * For reading in and interpreting the move that the user types into the console.
     */
    private void getPlayerMove() throws IOException {
        System.out.print("Index of move: ");
        URReport report = urCommander.runCommand("WAIT_USER_MOVE");
        int move = report.getFeedBack()[0] - 1;
        if (move < 0 || move >= Board.BOARD_WIDTH * Board.BOARD_WIDTH) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (Board.BOARD_WIDTH * Board.BOARD_WIDTH - 1) + ", inclusive.");
        } else if (!board.move(move)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    /**
     * Print out the winner of the game.
     */
    private void printWinner() throws IOException {
        Board.State winner = board.getWinner();
        System.out.println("\n" + board + "\n");

        robotDrawMove();

        if (winner == Board.State.Blank) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
            int winnPosIndex = board.getWinnerPosition();
            Byte[] winCords = winIndexMap.get(winnPosIndex);
            urCommander.runCommand("DRAW_WIN_LINE", new byte[]{winCords[0], winCords[1], -1});
        }

    }

    /**
     * Reset the game if the player wants to play again.
     *
     * @return true if the player wants to play again
     */
    private boolean tryAgain() throws IOException {
        board.reset();
        prevBoard = null;
        urCommander.runCommand("GO_READY_TO_DRAW_POS");
        urCommander.runCommand("PUT_MARKER", new byte[]{1, -1, -1});
        urCommander.runCommand("GO_HOME");
        urCommander.runCommand("WAIT_START_NEW_GAME");
        System.out.println("Started new game.");
        System.out.println("X's turn.");
        return true;
    }

    /**
     * Ask the player if they want to play again.
     *
     * @return true if the player wants to play again
     */
    private boolean promptTryAgain() {
        while (true) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            String response = sc.next();
            if (response.equalsIgnoreCase("y")) {
                return true;
            } else if (response.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }


    public static void main(String[] args) throws IOException {

        URReport report = null;
        ConnectionCatcher connectionCatcher = new ConnectionCatcher(3000);

        System.out.println("Waiting robot connection!");
        ConnectionPoint connectionPoint = connectionCatcher.waitConnection();

        System.out.println("Robot connected!");

        URCommander urCommander = new URCommander(connectionPoint);
        List<URCommand> supportedCommands = getSupportedCommandList();
        urCommander.uploadCommands(supportedCommands);

        Robot ticTacToe = new Robot(urCommander);
        ticTacToe.play();

    }

    private static List<URCommand> getSupportedCommandList() throws IOException {
        File file = new File("src/main/java/com/ur/supportedCommands.json");
        String testJson = FileUtils.readFileToString(file, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        URCommand[] commands = mapper.readValue(testJson, URCommand[].class);
        return Arrays.asList(commands);
    }

}
