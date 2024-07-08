package my.code.nftmarketplacepublic.services.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.jdbc.nftmarketplace2.configs.FirebaseAppConfig;
import com.jdbc.nftmarketplace2.services.FairBaseImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FairBaseImageServiceImpl implements FairBaseImageService {

    private final FirebaseAppConfig firebaseAppConfig;
    @Override
    public String saveUserProfileImage(MultipartFile multipartFile) throws IOException {
        FirebaseApp firebaseApp = firebaseAppConfig.firebaseApp();
        String bucketName = "nftmarketplace-f8ce1.appspot.com";
        Storage storage = StorageClient.getInstance(firebaseApp).bucket(bucketName).getStorage();

        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

        File pngFile = File.createTempFile("profile-image", ".png");
        ImageIO.write(bufferedImage, "png", pngFile);

        String fileName = pngFile.getName();
        String filePath = "profileImages/" + fileName;
        BlobId blobId = BlobId.of(bucketName, filePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
        Blob blob;

        try (FileInputStream fileInputStream = new FileInputStream(pngFile)) {
            blob = storage.create(blobInfo, fileInputStream);
        }
        catch (IOException ioException) {
         log.error(ioException.getMessage());
         throw new IOException(ioException.getMessage());
        }

        if (pngFile.exists()) {
            pngFile.delete();
        }

        String token = UUID.randomUUID().toString();
        return generateDownloadUrl(blob, token);
    }


    @Override
    public String saveNftImage(MultipartFile multipartFile) throws IOException {

        FirebaseApp firebaseApp = firebaseAppConfig.firebaseApp();

        String bucketName = "nftmarketplace-f8ce1.appspot.com";

        Storage storage = StorageClient.getInstance(firebaseApp).bucket(bucketName).getStorage();

        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

        File pngFile = File.createTempFile("nft-image", ".png");
        ImageIO.write(bufferedImage, "png", pngFile);

        String fileName = pngFile.getName();
        String filePath = "nfts/" + fileName;
        BlobId blobId = BlobId.of(bucketName, filePath);

        String token = UUID.randomUUID().toString();
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .setMetadata(Map.of("firebaseStorageDownloadTokens", token))
                .build();

        Blob blob;
        try (FileInputStream fileInputStream = new FileInputStream(pngFile)) {
            blob = storage.create(blobInfo, fileInputStream);
        }
        catch (IOException ioException) {
            log.error(ioException.getMessage());
            throw new IOException(ioException.getMessage());
        }

        if (pngFile.exists()) {
            pngFile.delete();
        }

        return generateDownloadUrl(blob, token);
    }

    private String generateDownloadUrl(Blob blob, String token) {
        return "https://firebasestorage.googleapis.com/v0/b/"
                + blob.getBucket()
                + "/o/"
                + blob.getName().replace("/", "%2F")
                + "?alt=media&token="
                + token;
    }
}
