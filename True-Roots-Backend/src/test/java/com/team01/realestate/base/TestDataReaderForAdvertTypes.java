package com.team01.realestate.base;


import com.team01.realestate.entity.concretes.business.AdvertType;
import com.team01.realestate.payload.request.business.AdvertTypeRequest;
import com.team01.realestate.payload.response.business.AdvertTypeResponse;
import io.vertx.core.json.Json;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Testcontainers
public class TestDataReaderForAdvertTypes {

    private static final Path workingDir = Path.of("","src/test/resources");

    public static AdvertTypeRequest getAdvertTypeRequestPayload(){
        return jsonPayloadAvertTypeRequest("testDatas/advertTypeRequest.json");
    }

    public static AdvertTypeRequest getAdvertTypeRequestPayload2(){
        return jsonPayloadAvertTypeRequest("testDatas/advertTypeRequest2.json");
    }

    public static AdvertTypeRequest getAdvertTypeRequestPayload3(){
        return jsonPayloadAvertTypeRequest("testDatas/advertTypeRequest3.json");
    }

    public static AdvertTypeResponse getAdvertTypeResponsePayload(){
        return jsonPayloadAvertTypeResponse("testDatas/advertType.json");
    }

    public static AdvertType getAdvertTypePayload(){
        return jsonPayloadAvertType("testDatas/advertTypeRequest2.json");
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
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static AdvertTypeRequest jsonPayloadAvertTypeRequest(String filename){
        return Json.decodeValue(readJsonFile(filename), AdvertTypeRequest.class);
    }

    private static AdvertTypeResponse jsonPayloadAvertTypeResponse(String filename){
        return Json.decodeValue(readJsonFile(filename), AdvertTypeResponse.class);
    }

    private static AdvertType jsonPayloadAvertType(String filename){
        return Json.decodeValue(readJsonFile(filename), AdvertType.class);
    }



}
