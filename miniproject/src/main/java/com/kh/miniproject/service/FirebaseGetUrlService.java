package com.kh.miniproject.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseGetUrlService {

    private final String firebaseCredentialsPath = "src/main/resources/firebase-service-account.json";
    private static final String BUCKET_NAME = "mini-project-d9c21.firebasestorage.app";
    private static final String FOLDER_PATH = "images/"; // Firebase의 경로 지정

    public List<String> getImageUrls() throws IOException {
        // Firebase Storage 초기화
        Storage storage = getStorage();

        // 특정 경로의 파일 가져오기
        Bucket bucket = storage.get(BUCKET_NAME);
        List<String> imageUrls = new ArrayList<>();

        for (Blob blob : bucket.list(Storage.BlobListOption.prefix(FOLDER_PATH)).iterateAll()) {
            // Blob URL 생성
            String url = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, blob.getName());
            imageUrls.add(url);
        }
        System.out.println("Image URL: " + imageUrls);
        return imageUrls;
    }
    private Storage getStorage() throws IOException {
        // Storage 서비스 가져오기
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(firebaseCredentialsPath)))
                .build()
                .getService();
    }
}