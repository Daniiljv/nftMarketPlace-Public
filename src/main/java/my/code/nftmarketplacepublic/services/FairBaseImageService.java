package my.code.nftmarketplacepublic.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FairBaseImageService {
    String saveNftImage(MultipartFile multipartFile) throws IOException;
    String saveUserProfileImage(MultipartFile multipartFile) throws IOException;

}
