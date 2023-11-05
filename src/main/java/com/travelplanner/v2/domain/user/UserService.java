package com.travelplanner.v2.domain.user;

import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.domain.user.domain.UserEditor;
import com.travelplanner.v2.domain.user.dto.request.PasswordRequest;
import com.travelplanner.v2.domain.user.dto.request.ProfileImageUpdateRequest;
import com.travelplanner.v2.domain.user.dto.request.UserNicknameUpdateRequest;
import com.travelplanner.v2.domain.user.dto.response.ProfileResponse;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
import com.travelplanner.v2.global.util.RedisUtil;
import com.travelplanner.v2.global.util.S3Util;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class UserService {
    private final AuthUtil authUtil;
    private final S3Util s3Util;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private PasswordEncoder encoder;

    // 특정 유저의 프로필 찾기
    @Transactional
    public ProfileResponse getUserProfile(Long userId) throws ApiException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        return ProfileResponse.builder()
                .email(user.getEmail())
                .userNickname(user.getUserNickname())
                .profileImgUrl(user.getProfileImageUrl())
                .build();
    }

    @Transactional
    public void updateUserProfileImage(ProfileImageUpdateRequest request, MultipartFile multipartFile)
            throws Exception {
        Long userId = authUtil.getLoginUserIndex();
        User user = userRepository.findById(userId).get();
        String profileImageUrl = user.getProfileImageUrl();
        String keyname = s3Util.extractKeyNameFromProfileImageUrl(profileImageUrl);

        if (multipartFile.isEmpty() || request.getChangeProfileImage()) {
            UserEditor userEditor = UserEditor.builder()
                    .profileImageUrl("")
                    .build();

            user.edit(userEditor);
        }

        if (!multipartFile.isEmpty() || request.getChangeProfileImage()) {
            s3Util.deleteFile(keyname, "upload/profile/");

            String originalImageName = multipartFile.getOriginalFilename();
            String uniqueImageName = s3Util.generateUniqueImgName(originalImageName, userId);
            String localFilePath = System.getProperty("java.io.tmpdir") + "/" + uniqueImageName;
            multipartFile.transferTo(Paths.get(localFilePath));

            s3Util.uploadFile(uniqueImageName, localFilePath, "upload/profile/");
            String imgUrl = "https://travel-planner-buckets.s3.ap-northeast-2.amazonaws.com/upload/profile/";

            UserEditor userEditor = UserEditor.builder()
                    .profileImageUrl(imgUrl + uniqueImageName)
                    .build();
            user.edit(userEditor);

            // 로컬 임시 파일 삭제
            s3Util.deleteLocalFile(localFilePath);
        }
    }

    @Transactional
    public void updateUserNickname(UserNicknameUpdateRequest request) {
        Long userId = authUtil.getLoginUserIndex();
        User user = userRepository.findById(userId).get();

        UserEditor userEditor = UserEditor.builder()
                .password(request.getUserNickname())
                .build();
        user.edit(userEditor);
    }

    @Transactional
    public boolean checkPassword (PasswordRequest request) {
        Long userId = authUtil.getLoginUserIndex();
        User user = userRepository.findById(userId).get();
        String encodedPassword = user.getPassword();

        if (encoder.matches(request.getPassword(), encodedPassword)) {
            return true;
        }
        return false;
    }

    @Transactional
    public void updatePassword(PasswordRequest request) {
        Long userId = authUtil.getLoginUserIndex();
        User user = userRepository.findById(userId).get();

        UserEditor userEditor = UserEditor.builder()
                .password(request.getPassword())
                .build();
        user.edit(userEditor);
    }

    @Transactional
    public void deleteUser() {
        Long userId = authUtil.getLoginUserIndex();
        User user = userRepository.findById(userId).get();
        String keyname = s3Util.extractKeyNameFromProfileImageUrl(user.getProfileImageUrl());

        // 프로필 이미지 삭제하기
        s3Util.deleteFile(keyname, "upload/profile/");
        // 레디스 삭제
        redisUtil.deleteData(String.valueOf(userId));
        // 멤버 삭제
        userRepository.delete(user);
    }
}
