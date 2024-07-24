package com.malltail.erp.controller;

import com.malltail.erp.service.MemberService;
import com.malltail.erp.vo.MemberRequest;
import com.malltail.erp.vo.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    //로그인
//    @PostMapping("/login")
//    @ResponseBody
//    public MemberResponse login(HttpServletRequest request){
//        //1.회원정보 조회
//        String loginId = request.getParameter("loginId");
//        String password = request.getParameter("password");
//        MemberResponse member = memberService.login(loginId, password);
//
//        //2.세션에 회원 정보 저장 & 세션유지시간 설정
//        if(member != null){
//            HttpSession session = request.getSession();
//            session.setAttribute("loginMember", member);
//            session.setMaxInactiveInterval(60*30);
//        }
//
//        return member;
//    }

    //로그아웃
//    @PostMapping("/logout")
//    public String logout(HttpSession session){
//        session.invalidate();
//        return "redirect:/login.do";
//    }

    //로그인 페이지
    @GetMapping("/login.do")
    public String openLogin(@RequestParam(value = "error",required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            Model model){
        model.addAttribute("error",error);
        model.addAttribute("exception", exception);

       return "member/login";
    }

    //회원정보 저장(회원가입)
    @PostMapping("/members")
    @ResponseBody
    public Long saveMember(@RequestBody MemberRequest params){
        log.info("___params = " + params.toString());
        return memberService.saveMember(params);
    }

    //회원상세정보 조회
    @GetMapping("/members/{loginId}")
    @ResponseBody
    public MemberResponse findMemberByLoginId(@PathVariable(value = "loginId") String loginId){
        return memberService.findMemberByLoginId(loginId);
    }

    //회원정보 수정
    @PatchMapping("/members/{id}")
    @ResponseBody
    public Long updateMember(@PathVariable(value = "id") Long id, @RequestBody MemberRequest params){
        return memberService.updateMember(params);
    }

    //회원정보 삭제(회원 탈퇴)
    @DeleteMapping("/members/{id}")
    @ResponseBody
    public Long deleteMemberById(Long id){
        return memberService.deleteById(id);
    }

    //회원 수 카운팅(ID중복 체크)
    @GetMapping("/member-count")
    @ResponseBody
    public int countMemberByLoginId(@RequestParam(value = "loginId") String loginId){
        return memberService.countMemberByLonginId(loginId);
    }
}
