package com.supply.service;

import com.supply.domain.dto.PageQueryDTO;
import com.supply.domain.entity.IdentityAuthentication;
import com.supply.domain.vo.ReportInformationVO;
import com.supply.domain.vo.UserInformationVO;
import com.supply.domain.vo.VerificationInformationVO;
import com.supply.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    UserInformationVO getInformation();

    List<VerificationInformationVO> getVerificationInformation(Long type);

    void checkVerificationInformation(Long id, Long isAgree);

    List<ReportInformationVO> getReportInformation();

    void dealReport(Long id, Integer isIllegal,Integer isBlocked);

    PageResult getAllUsers(PageQueryDTO pageQueryDTO);

    void block(Long id);

    void liftUser(Long id);

    void addVerificationMessage(IdentityAuthentication identityAuthentication);
}
