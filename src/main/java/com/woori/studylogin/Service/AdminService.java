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
    public Map<String, Object> getUserList(String searchType, String searchValue, String roleType, int page) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 사이즈는 10으로 설정
        Page<UserEntity> userPage;

        if (!roleType.isEmpty()) {
            // 역할에 따라 사용자 필터링
            userPage = findUsersByRoleAndSearch(roleType, searchType, searchValue, pageable);
        } else {
            userPage = findUsers(searchType, searchValue, pageable);
        }

        Map<String, Integer> pageInfo = PaginationUtil.Pagination(userPage);

        // 반환할 정보
        return Map.of(
                "userDTOS", userPage,
                "pageInfo", pageInfo,
                "searchType", searchType,
                "searchValue", searchValue
        );
    }

    private Page<UserEntity> findUsersByRoleAndSearch(String roleType, String searchType, String searchValue, Pageable pageable) {
        // 역할에 따라 사용자 필터링 및 검색
        Page<UserEntity> userPage = userRepository.findByRoleType(RoleType.valueOf(roleType), pageable);

        if ("name".equals(searchType) && searchValue != null && !searchValue.isEmpty()) {
            return userRepository.findByRoleTypeAndNameContaining(RoleType.valueOf(roleType), searchValue, pageable);
        }
        if ("birth".equals(searchType) && searchValue != null && !searchValue.isEmpty()) {
            return userRepository.findByRoleTypeAndBirthContaining(RoleType.valueOf(roleType), searchValue, pageable);
        }
        if ("address".equals(searchType) && searchValue != null && !searchValue.isEmpty()) {
            return userRepository.findByRoleTypeAndAddressContaining(RoleType.valueOf(roleType), searchValue, pageable);
        }

        return userPage; // 기본적으로 역할만으로 필터링
    }

    private Page<UserEntity> findUsers(String searchType, String searchValue, Pageable pageable) {
        if ("name".equals(searchType) && searchValue != null && !searchValue.isEmpty()) {
            return userRepository.findByNameContaining(searchValue, pageable);
        }
        if ("birth".equals(searchType) && searchValue != null && !searchValue.isEmpty()) {
            return userRepository.findByBirthContaining(searchValue, pageable);
        }
        if ("address".equals(searchType) && searchValue != null && !searchValue.isEmpty()) {
            return userRepository.findByAddressContaining(searchValue, pageable);
        }
        // 기본값으로 모든 조건을 포함하는 경우
        return userRepository.findByNameContainingOrBirthContainingOrAddressContaining(searchValue, searchValue, searchValue, pageable);
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
