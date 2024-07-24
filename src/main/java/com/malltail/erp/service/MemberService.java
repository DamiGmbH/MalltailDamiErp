package com.malltail.erp.service;

import com.malltail.erp.mapper.primary.MemberMapper;
import com.malltail.erp.vo.MemberRequest;
import com.malltail.erp.vo.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Component
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 정보 저장(회원가입)
     * @param params - 회원 정보
     * @return PK
     */
    @Transactional
    public Long saveMember(MemberRequest params){
        //params.encodingPassword(passwordEncoder);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        params.encodingPassword(passwordEncoder);
        memberMapper.save(params);
        return params.getId();
    }

    /**
     * 회원 상세정보 조회
     * @param loginId - PK
     * @return 회원 상세정보
     */
    public MemberResponse findMemberByLoginId(String loginId){
        return memberMapper.findByLoginId(loginId);
    }

    /**
     * 회원 정보 수정
     * @param params - 회원정보
     * @return PK
     */
    @Transactional
    public Long updateMember(MemberRequest params){
        params.encodingPassword(passwordEncoder);
        memberMapper.update(params);
        return params.getId();
    }

    /**
     * 회원정보 삭제(회원 탈퇴)
     * @param id - PK
     * @return PK
     */
    @Transactional
    public Long deleteById(Long id){
        memberMapper.deleteById(id);
        return id;
    }

    /**
     * 회원 수 카운팅(ID 중복체크)
     * @param loginId - PK
     * @return 회원 수
     */
    public int countMemberByLonginId(String loginId){
        return memberMapper.countByLoginId(loginId);
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        MemberResponse memberResponse = memberMapper.findByLoginId(loginId);

        if(memberResponse == null){
            throw new UsernameNotFoundException("UserInfo NotFound");
        }

        return User.builder()
                .username(memberResponse.getName())
                .password(memberResponse.getPassword())
                .roles(memberResponse.getRoletype())
                //.roles("USER")
                .build();

    }
}
