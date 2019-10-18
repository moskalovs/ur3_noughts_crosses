package com.jacson;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ur.ur.URCommand;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class examples {

    @Test
    public void jsonArrayToListExample() throws IOException {

        File file = new File("src/test/java/com/jacson/test.json");
        String testJson = FileUtils.readFileToString(file, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        URCommand[] commands = mapper.readValue(testJson, URCommand[].class);
        Arrays.stream(commands).forEach(System.out::println);
    }

}
