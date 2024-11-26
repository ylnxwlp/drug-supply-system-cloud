package com.supply.service;

import com.supply.domain.dto.UserInformationDTO;
import com.supply.domain.entity.User;
import com.supply.domain.vo.UserInformationVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface UserService {

    void register(UserInformationDTO userInformationDTO);

    void sendCode(String email, Long operationType);

    void resetPassword(UserInformationDTO userInformationDTO);

    void logout();

    void ReUploadIdentityFile(UserInformationDTO userInformationDTO);

    UserInformationVO getModificationInformation();

    void updateUserInformation(UserInformationDTO userInformationDTO);

    User getUserInformationById(Long id);

    void checkSuccessfully(Long id, LocalDateTime localDateTime, Integer workType);

    void checkFail(Long id, LocalDateTime localDateTime);

    void blockAccount(Long id, LocalDateTime localDateTime);

    void lift(Long id, LocalDateTime localDateTime);

    List<User> getAllUsers();
}
