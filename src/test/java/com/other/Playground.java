package com.other;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Playground {

    @Test
    public void bytesStringToArray() {

        Byte[] expectedArray = {1, 2, 3, 4};
        String stringBytes = "1,2,3,4";

        String[] bytes = stringBytes.split(",");
        Byte[] actualArray = Arrays.stream(bytes)
                .map(Byte::parseByte)
                .toArray(Byte[]::new);

        Assert.assertArrayEquals(expectedArray, actualArray);
    }

}
