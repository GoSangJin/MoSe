package com.woori.studylogin.Repository;

import com.woori.studylogin.Constant.RoleType;
import com.woori.studylogin.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // Service에 문장을 참고해서 자동으로 메소드를 생성
    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findByNameContaining(String name, Pageable pageable);
    Page<UserEntity> findByBirthContaining(String birth, Pageable pageable);
    Page<UserEntity> findByAddressContaining(String address, Pageable pageable);
    Page<UserEntity> findByNameContainingOrBirthContainingOrAddressContaining(String name, String birth, String address, Pageable pageable);

    List<UserEntity> findByIsSuspendedTrue();
    Page<UserEntity> findByRoleTypeAndNameContaining(RoleType roleType, String name, Pageable pageable);
    Page<UserEntity> findByRoleTypeAndBirthContaining(RoleType roleType, String birth, Pageable pageable);
    Page<UserEntity> findByRoleTypeAndAddressContaining(RoleType roleType, String address, Pageable pageable);


    // 페이지네이션을 지원하는 역할 필터링 메소드
    Page<UserEntity> findByRoleType(RoleType roleType, Pageable pageable);
}
