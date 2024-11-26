package com.supply.controller;

import com.supply.domain.dto.PageQueryDTO;
import com.supply.domain.entity.IdentityAuthentication;
import com.supply.domain.vo.ReportInformationVO;
import com.supply.domain.vo.UserInformationVO;
import com.supply.domain.vo.VerificationInformationVO;
import com.supply.result.PageResult;
import com.supply.result.Result;
import com.supply.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "管理员部分接口")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/information")
    @Operation(summary = "个人信息回显接口")
    public Result<UserInformationVO> getInformation() {
        UserInformationVO userInformationVO = adminService.getInformation();
        return Result.success(userInformationVO);
    }

    @GetMapping("/verify/{type}")
    @Operation(summary = "认证信息查询接口")
    public Result<List<VerificationInformationVO>> getVerificationInformation(@PathVariable Long type) {
        List<VerificationInformationVO> list = adminService.getVerificationInformation(type);
        return Result.success(list);
    }

    @PostMapping("/check/{id}")
    @Operation(summary = "信息认证审核接口")
    public Result<Object> checkVerificationInformation(@PathVariable Long id, @RequestParam @Valid @Min(1) @Max(2) Long isAgree) {
        adminService.checkVerificationInformation(id, isAgree);
        return Result.success();
    }

    @GetMapping("/inform")
    @Operation(summary = "举报信息查询接口")
    public Result<List<ReportInformationVO>> getReportInformation() {
        List<ReportInformationVO> list = adminService.getReportInformation();
        return Result.success(list);
    }

    @PutMapping("/inform/{id}")
    @Operation(summary = "举报信息处理接口")
    public Result<Object> dealReport(@PathVariable Long id, @RequestParam @Valid @Min(1) @Max(2) Integer isIllegal, @RequestParam @Valid @Min(1) @Max(2) Integer isBlocked) {
        adminService.dealReport(id, isIllegal, isBlocked);
        return Result.success();
    }

    @GetMapping("/allUsers")
    @Operation(summary = "查询全部通过认证的用户信息接口")
    public Result<PageResult> getAllUsers(@Valid @RequestBody PageQueryDTO pageQueryDTO) {
        PageResult pageResult = adminService.getAllUsers(pageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping("/block/{id}")
    @Operation(summary = "封禁用户接口")
    public Result<Object> blockUser(@PathVariable Long id) {
        adminService.block(id);
        return Result.success();
    }

    @PutMapping("/lift/{id}")
    @Operation(summary = "解禁用户接口")
    public Result<Object> liftUser(@PathVariable Long id) {
        adminService.liftUser(id);
        return Result.success();
    }
}
