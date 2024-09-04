package com.woori.studylogin.Service;

import com.woori.studylogin.Constant.RoleType;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.UserRepository;
import com.woori.studylogin.Util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    // 사용자 목록 조회 및 페이지네이션 처리
    public Map<String, Object> getUserList(String searchName, String searchBirth, String searchAddress, int page) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 사이즈는 10으로 설정
        Page<UserEntity> userPage = userRepository.findByNameContainingAndBirthContainingAndAddressContaining(searchName, searchBirth, searchAddress, pageable);

        Map<String, Integer> pageInfo = PaginationUtil.Pagination(userPage);

        // 반환할 정보
        return Map.of(
                "userDTOS", userPage,
                "pageInfo", pageInfo,
                "searchName", searchName,
                "searchBirth", searchBirth,
                "searchAddress", searchAddress
        );
    }

    public void updateUserRole(Integer userId, RoleType roleType) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoleType(roleType); // 사용자 역할 업데이트
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
