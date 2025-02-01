package com.team01.realestate.base;

import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.ImageUploadRequest;
import io.vertx.core.json.Json;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Testcontainers
public class TestDataReaderForImages {

    private static final Path workingDir = Path.of("","src/test/resources");

    public static AdvertRequest getAdvertRequestPayload() {
        return jsonPayloadAdvertRequest("testDatas/advertRequest.json");
    }

    public static ImageUploadRequest getImageRequestPayload() {
        return jsonPayloadImageRequest("testDatas/imagesRequest.json");
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

    private static AdvertRequest jsonPayloadAdvertRequest(String filename) {
        return Json.decodeValue(readJsonFile(filename), AdvertRequest.class);
    }

    private static ImageUploadRequest jsonPayloadImageRequest(String filename) {
        return Json.decodeValue(readJsonFile(filename), ImageUploadRequest.class);
    }



}
