package com.seungrae.board.member;

import com.seungrae.board.CustomUser;
import com.seungrae.board.jwt.JwtUtil;
import com.seungrae.board.dto.RegisterDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/register")
    public String register(){
        return "register.html";
    }

    @PostMapping("/registerAct")
    public String registerAct(
            @Valid @ModelAttribute RegisterDto req,
            BindingResult br,
            Model model
    ){
        if(br.hasErrors()){
            String error = br.getFieldError().getDefaultMessage();
            model.addAttribute("error", error);
            System.out.println(error);
            return "register.html";
        }
        if(memberRepository.existsByEmail(req.email())){
            model.addAttribute("error", "이미 사용 중인 이메일입니다.");
            return "register.html";
        }

        String encodePw = passwordEncoder.encode(req.password());

        Member member = new Member();
        member.setEmail(req.email());
        member.setUsername(req.username());
        member.setPassword(encodePw);
        member.setRole("ROLE_USER");
        memberRepository.save(member);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @PostMapping("loginAct")
    @ResponseBody
    public ResponseEntity<String> loginAct(@RequestBody Map<String, String> req, HttpServletResponse response){
        try{
            // 로그인 요청서
            var authToken = new UsernamePasswordAuthenticationToken(
                    req.get("username"), req.get("password")
            );
            // MyUserDetailsService에서 처리
            var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            var auth2 = SecurityContextHolder.getContext().getAuthentication();
            var jwt = JwtUtil.createToken(auth2);


            // 쿠키에 jwt 저장
            var cookie = new Cookie("jwt", jwt);
            cookie.setMaxAge(60*60);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok("ok");
        }
        catch (Exception e){
            return ResponseEntity.status(401).body("아이디 및 비밀번호를 확인하세요.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String mypage(Authentication auth, Model model){
        if(!auth.isAuthenticated()){
            return "error.html";
        }
        var user = (CustomUser) auth.getPrincipal();
        model.addAttribute("user", user);
        return "mypage.html";
    }


}
