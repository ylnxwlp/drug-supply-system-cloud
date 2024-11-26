package com.supply.controller;

import com.supply.domain.dto.UserLoginDTO;
import com.supply.domain.vo.UserLoginVO;
import com.supply.result.Result;
import com.supply.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping
    public Result<UserLoginVO> authorization(@RequestBody @Valid UserLoginDTO userLoginDTO, HttpServletRequest request) {
        UserLoginVO userLoginVO = authorizationService.authorization(userLoginDTO,request);
        return Result.success(userLoginVO);
    }
}
