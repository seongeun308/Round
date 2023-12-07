package com.example.round.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("sendcode") // 이메일 인증코드 전송 API
//    public BaseResponse<String> sendCode(@RequestBody PostEmailRequest req) {
//        try {
//            if (req.getEmail().isEmpty()) {
//                return new BaseResponse<>(EMAIL_EMPTY_ERROR);
//            }
//            if (!isRegexEmail(req.getEmail())) { //이메일 형식(정규식) 검증
//                return new BaseResponse<>(EMAIL_FORMAT_ERROR);
//            }
//            authService.sendCode(req.getEmail());
//            return new BaseResponse<>("인증번호가 전송되었습니다.");
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }

    @PostMapping("/sendCode")
    public ResponseEntity sendCode(@RequestParam(name = "email") String email){
        authService.sendCode(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }



//    @PostMapping("/sendCode")
//    public ResponseEntity sendCode(@RequestBody AuthForm authForm){
//        if (authForm)
//        authService.sendCode(email);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


}
