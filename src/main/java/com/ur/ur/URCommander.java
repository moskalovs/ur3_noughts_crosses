package com.ur.ur;

import com.ur.exception.URCommanderException;
import com.ur.network.ConnectionPoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class URCommander {

    private Map<String, URCommand> supportedCommands;
    private final ConnectionPoint UR_CONNECTION_POINT;

    public URCommander(ConnectionPoint UR_CONNECTION_POINT) {
        this.UR_CONNECTION_POINT = UR_CONNECTION_POINT;
    }


    public void uploadCommands(List<URCommand> urCommandList) {
        supportedCommands = urCommandList.stream()
                .collect(Collectors.toMap(URCommand::getName, command -> command));
    }

    public URReport runCommand(String name) throws IOException {
        byte[] defaultPayload = {-1, -1, -1};
        return this.runCommand(name, defaultPayload);
    }

    public URReport runCommand(String name, byte[] payload) throws IOException {
        Optional<URCommand> command = Optional.ofNullable(supportedCommands.get(name));
        command.orElseThrow(() -> new URCommanderException("Command Not Found"));

        byte[] readyToSendCommand = assembleCommand(command.get(), payload);
        UR_CONNECTION_POINT.send(readyToSendCommand);

        URReport urReport = command.get().hasFeedback() ?
                new URReport(command.get(), UR_CONNECTION_POINT.waitData(4)) :
                new URReport(command.get());

        waitRobotReady();
        return urReport;
    }

    private byte[] assembleCommand(URCommand command, byte[] payload) {
        byte[] data = new byte[4];
        data[0] = command.getCode().byteValue();
        System.arraycopy(payload, 0, data, 1, 3);
        return data;
    }

    private void waitRobotReady() throws IOException {
        byte[] ready = UR_CONNECTION_POINT.waitData(4);
    }

}
