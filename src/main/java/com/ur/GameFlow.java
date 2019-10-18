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


public class GameFlow {

    public static void main(String[] args) throws IOException, InterruptedException {

        URReport report = null;
        ConnectionCatcher connectionCatcher = new ConnectionCatcher(3000);
        ConnectionPoint connectionPoint = connectionCatcher.waitConnection();
        System.out.println("Robot connected!");

        URCommander urCommander = new URCommander(connectionPoint);
        List<URCommand> supportedCommands = getSupportedCommandList();
        urCommander.uploadCommands(supportedCommands);



    }

    private static List<URCommand> getSupportedCommandList() throws IOException {
        File file = new File("src/main/java/com/ur/supportedCommands.json");
        String testJson = FileUtils.readFileToString(file, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        URCommand[] commands = mapper.readValue(testJson, URCommand[].class);
        return Arrays.asList(commands);
    }

}

