package oridungjeol.duckhang.board.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.board.application.port.out.UploadFilePort;
import oridungjeol.duckhang.common.firebase.storage.FirebaseStorageService;

@Component
@RequiredArgsConstructor
public class FirebaseUploaderAdapter implements UploadFilePort {

    private final FirebaseStorageService firebaseStorageService;

    @Override
    public String upload(MultipartFile file) {
        return firebaseStorageService.upload(file);
    }
}
