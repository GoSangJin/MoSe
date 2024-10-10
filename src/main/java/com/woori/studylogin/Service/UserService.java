package com.woori.studylogin.Service;

import com.woori.studylogin.Constant.RoleType;
import com.woori.studylogin.DTO.UserDTO;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

        log.info("{} 조회 자료입니다.", userEntity);
        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoleType().name())
                .build();
    }

    public void register(UserDTO userDTO) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setRoleType(RoleType.user); // 권한을 ROLE_USER로 설정

        userRepository.save(userEntity);
    }

    public void update(UserDTO userDTO) {
        UserEntity userEntity = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new IllegalStateException("수정할 회원이 없습니다."));

        if (userDTO.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(userEntity);
    }

    public UserDTO detail(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));

        return modelMapper.map(userEntity, UserDTO.class);
    }

    public UserDTO findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("can not found user"));
        return modelMapper.map(userEntity, UserDTO.class);
    }

    // 비밀번호 검증 메서드
    public boolean verifyPassword(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));

        // 저장된 비밀번호와 입력된 비밀번호 비교
        return passwordEncoder.matches(password, userEntity.getPassword());
    }

    // 비밀번호 업데이트 메서드
    public void updatePassword(String username, String newPassword) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));

        userEntity.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 해싱
        userRepository.save(userEntity);
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void liftSuspensions() {
        List<UserEntity> suspendedUsers = userRepository.findByIsSuspendedTrue();
        LocalDate today = LocalDate.now();

        for (UserEntity user : suspendedUsers) {
            LocalDate endDate = user.getSuspensionEndDate();
            if (endDate != null && endDate.isBefore(today)) {
                user.setSuspended(false);
                user.setSuspensionEndDate(null);
                userRepository.save(user);
            }
        }
    }

    public String getRemainingSuspensionPeriod(UserEntity user) {
        if (user.isSuspended()) {
            LocalDate now = LocalDate.now();
            LocalDate suspensionEndDate = user.getSuspensionEndDate();
            long daysRemaining = ChronoUnit.DAYS.between(now, suspensionEndDate);
            return "남은 기간: " + daysRemaining + "일";
        }
        return "";
    }

    public String findUsernameByEmailAndBirthAndName(String email, String birth, String name) {
        UserEntity userEntity = userRepository.findByEmailAndBirthAndName(email, birth, name);
        return userEntity != null ? userEntity.getUsername() : null;
    }
}
