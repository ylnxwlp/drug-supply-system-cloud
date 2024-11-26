package com.supply.service;

import com.supply.domain.dto.UserLoginDTO;
import com.supply.domain.vo.UserLoginVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {

    UserLoginVO authorization(UserLoginDTO userLoginDTO, HttpServletRequest request);
}
