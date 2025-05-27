package oridungjeol.duckhang.common.firebase.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    /**
     * firebase storage에 이미지를 업로드합니다.
     *
     * @param file
     * @return 업로드된 링크
     */
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            return String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    bucket.getName(),
                    encodedFileName
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    private String generateUniqueFileName(String originalName) {
        String extension = originalName.substring(originalName.lastIndexOf("."));
        return "uploads/" + UUID.randomUUID() + extension;
    }
}
