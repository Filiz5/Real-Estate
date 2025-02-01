package com.team01.realestate.base;

import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.payload.request.business.TourRequestRequest;
import com.team01.realestate.payload.request.user.UserRequest;
import io.vertx.core.json.Json;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Testcontainers
public class TestDataReaderForTourRequest {

    private static final Path workingDir = Path.of("","src/test/resources");

    public static UserRequest getUserRequestPayload(){
        return jsonPayloadUserRequest(
                "testdatas/userRequestForTourRequest.json");
    }

    public static TourRequestRequest getTourRequestRequestPayload(){
        return jsonPayloadTourRequestRequest(
                "testdatas/tourRequestRequest.json");
    }

    public static TourRequest getTourRequestResponsePayload(){
        return jsonPayloadTourRequestResponse(
                "testdatas/tourRequest.json");
    }

    private static String readJsonFile(String filename){
        Path file = workingDir.resolve(filename);
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TourRequestRequest jsonPayloadTourRequestRequest(String filename){
        return Json.decodeValue(readJsonFile(filename), TourRequestRequest.class);
    }

    private static TourRequest jsonPayloadTourRequestResponse(String filename){
        return Json.decodeValue(readJsonFile(filename), TourRequest.class);
    }

    private static UserRequest jsonPayloadUserRequest(String filename) {
        return Json.decodeValue(readJsonFile(filename), UserRequest.class);
    }

}
