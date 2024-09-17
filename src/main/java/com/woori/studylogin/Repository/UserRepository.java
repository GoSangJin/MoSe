package com.woori.studylogin.Repository;

import com.woori.studylogin.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    //Serivice에 문장을 참고해서 자동으로 메소드를 생성
    Optional<UserEntity> findByUsername(String username);
    Page<UserEntity> findByNameContainingAndBirthContainingAndAddressContaining(String name, String birth, String address, Pageable pageable);

    List<UserEntity> findByIsSuspendedTrue();
}
