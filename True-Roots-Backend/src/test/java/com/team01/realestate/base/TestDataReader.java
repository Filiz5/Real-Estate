package com.team01.realestate.base;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.payload.request.user.UserRequest;
import io.vertx.core.json.Json;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;

@Testcontainers
public class TestDataReader {

    private static final Path workingDir = Path.of("", "src/test/resources");



    public static UserRequest getUserRequestPayload(){
        return jsonPayloadUserRequest(
                "testdatas/userRequest.json");
    }

    public static User getUserPayload(){
        return jsonPayloadUser(
                "testdatas/user.json");
    }

    /**
     * Reads the content of a JSON file and returns it as a string.
     *
     * @param filename the name of the JSON file to read
     * @return the content of the JSON file as a string
     * @throws IOException if an I/O error occurs while reading the file
     */
    private static String readJsonFile(String filename){
        Path file = workingDir.resolve(filename);
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static UserRequest jsonPayloadUserRequest(String filename) {
        return Json.decodeValue(readJsonFile(filename), UserRequest.class);
    }

    private static User jsonPayloadUser(String filename) {
        return Json.decodeValue(readJsonFile(filename), User.class);
    }

}
