package study.board.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.board.api.advice.exception.CEmailSigninFailedException;
import study.board.api.config.security.JwtTokenProvider;
import study.board.api.entity.User;
import study.board.api.model.response.CommonResult;
import study.board.api.model.response.SingleResult;
import study.board.api.repo.UserJpaRepo;
import study.board.api.service.ResponseService;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
@Api(tags = {"1. Sign"})
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSigninFailedException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "이름", required = true) @RequestParam String name) {
        userJpaRepo.save(
                User.builder()
                        .uid(id)
                        .password(passwordEncoder.encode(password))
                        .name(name)
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build());
        return responseService.getSuccessResult();
    }
}
