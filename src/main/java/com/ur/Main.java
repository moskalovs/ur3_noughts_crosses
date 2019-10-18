package com.ur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ur.network.ConnectionCatcher;
import com.ur.network.ConnectionPoint;
import com.ur.ur.URCommand;
import com.ur.ur.URCommander;
import com.ur.ur.URReport;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        ConnectionCatcher connectionCatcher = new ConnectionCatcher(3000);
        ConnectionPoint connectionPoint = connectionCatcher.waitConnection();

        URCommander urCommander = new URCommander(connectionPoint);
        List<URCommand> supportedCommands = getSupportedCommandList();
        print(supportedCommands.toString());
        urCommander.uploadCommands(supportedCommands);

        while (true) {
            URReport report;

            print("Enter command.");
            String userCommand = sc.nextLine();
            if (userCommand.equals("exit")) break;

            print("Enter payload");
            String userPayloadString = sc.nextLine();
            if (!userPayloadString.equals("")) {
                byte[] userPayload = parseStringBytesToArray(userPayloadString);
                report = urCommander.runCommand(userCommand, userPayload);
            } else {
                report = urCommander.runCommand(userCommand);
            }

            print(report.toString());
        }

    }

    private static List<URCommand> getSupportedCommandList() throws IOException {
        File file = new File("src/main/java/com/ur/supportedCommands.json");
        String testJson = FileUtils.readFileToString(file, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        URCommand[] commands = mapper.readValue(testJson, URCommand[].class);
        return Arrays.asList(commands);
    }

    private static byte[] parseStringBytesToArray(String stringBytes) {
        String[] bytes = stringBytes.split(",");
        Byte[] bytesArray = Arrays.stream(bytes)
                .map(Byte::parseByte)
                .toArray(Byte[]::new);

        short j = 0;
        byte[] primitiveBytes = new byte[3];
        for (Byte b : bytesArray)
            primitiveBytes[j++] = b;

        return primitiveBytes;
    }

    private static void print(String message) {
        System.out.println(message);
    }

}
