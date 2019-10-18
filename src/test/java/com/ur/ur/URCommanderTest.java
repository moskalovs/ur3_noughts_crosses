package com.ur.ur;

import com.ur.exception.URCommanderException;
import com.ur.network.ConnectionPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class URCommanderTest {


    @Mock
    private ConnectionPoint connectionMock;

    private URCommander urCommander;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        urCommander = new URCommander(connectionMock);
        List<URCommand> urCommands = getTestCommands();
        urCommander.uploadCommands(urCommands);
    }

    private List<URCommand> getTestCommands() {
        return Arrays.asList(
                new URCommand("EXIST_COMMAND", 44),
                new URCommand("PUT_OBJECT", 22));
    }

    @Test
    void shouldTrowException_ThenCommandWillNotFound() throws IOException {
        Assertions.assertThrows(URCommanderException.class, () -> {
            urCommander.runCommand("MOVE_TO_START_POS");
        });
    }

    @Test
    void shouldSendCorrectData() throws IOException {
        byte commandCode = 44;
        byte[] payload = {1, 2, 3, 4};
        byte[] expectedCommand = {commandCode, 1, 2, 3, 4};

        urCommander.runCommand("EXIST_COMMAND", payload);
        verify(connectionMock).send(expectedCommand);
    }


}