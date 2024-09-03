package com.woori.studylogin.Service;

import com.woori.studylogin.DTO.UserDTO;
import com.woori.studylogin.Entity.UserEntity;
import com.woori.studylogin.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//log4j2, log는 필요하면 사용
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    
    //사용자 로그인처리(사용자아이디)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> read = userRepository.findByUsername(username);
        if(read.isPresent()) {
            log.info("{}조회 자료입니다.", read.toString());
            //아이디, 비밀번호, 등급을 보안인증에 전달
            return User.withUsername(read.get().getUsername())
                    .password(read.get().getPassword())
                    .roles(read.get().getRoleType().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
    }
    
    //회원등록(기존 사용자아이디가 존재하면 안된다.)
    public void register(UserDTO userDTO) {
        Optional<UserEntity> read = userRepository.findByUsername(userDTO.getUsername());
        if(read.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
    }
    //회원수정(수정할 사용자아이디가 존재해야 한다.)
    public void update(UserDTO userDTO) {
        Optional<UserEntity> read = userRepository.findByUsername(userDTO.getUsername());
        if(read.isEmpty()) {
            throw new IllegalStateException("수정할 회원이 없습니다.");
        }
        //userDTO를 받아서 저장하면 비밀번호(변경, 오류)
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        if(userDTO.getPassword() != null) { //비밀번호 변경이 있으면
            //새로운 비밀번호를 암호화해서 저장
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            //조회한 내용에서 저장된 비밀번호를 읽어서 다시 저장
            userEntity.setPassword(read.get().getPassword());
        }

        userRepository.save(userEntity);
    }
    //회원조회(사용자아이디)-alt-shift-Enter는 repository에 메소드 생성
    public UserDTO detail(String username) {
        Optional<UserEntity> read = userRepository.findByUsername(username);
        UserDTO userDTO = modelMapper.map(read, UserDTO.class);
        
        log.info("{}를 조회하였습니다.", userDTO.getName());
        return userDTO;
    }
    
}
//SecurityConfig에 login, logout, csrf 설정
