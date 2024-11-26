package com.supply.controller;

import com.supply.domain.dto.UserInformationDTO;
import com.supply.domain.entity.User;
import com.supply.domain.vo.UserInformationVO;
import com.supply.result.Result;
import com.supply.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册接口")
    public Result<Object> register(@Valid @RequestBody UserInformationDTO userInformationDTO) {
        userService.register(userInformationDTO);
        return Result.success();
    }

    @PostMapping("/verifyCode/send")
    @Operation(summary = "验证码发送接口")
    public Result<Object> sendCode(@RequestParam String email, @RequestParam Long operationType) {
        userService.sendCode(email, operationType);
        return Result.success();
    }

    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码接口")
    public Result<Object> resetPassword(@RequestBody @Valid UserInformationDTO userInformationDTO) {
        userService.resetPassword(userInformationDTO);
        return Result.success();
    }

    @PostMapping("/logout")
    @Operation(summary = "登出接口")
    public Result<Object> logout() {
        userService.logout();
        return Result.success();
    }

    @PostMapping("/reUpload")
    @Operation(summary = "重新上传身份文件接口")
    public Result<Object> ReUploadIdentityFile(@Valid @RequestBody UserInformationDTO userInformationDTO) {
        userService.ReUploadIdentityFile(userInformationDTO);
        return Result.success();
    }

    @GetMapping("/modification/information")
    @Operation(summary = "编辑处个人信息回显接口")
    public Result<UserInformationVO> getModificationInformation() {
        UserInformationVO userInformationVO = userService.getModificationInformation();
        return Result.success(userInformationVO);
    }

    @GetMapping("/modification")
    @Operation(summary = "修改个人信息接口")
    public Result<Object> updateInformation(@Valid @RequestBody UserInformationDTO userInformationDTO) {
        userService.updateUserInformation(userInformationDTO);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取用户信息接口")
    public Result<User> getUserInformationById(@PathVariable Long id){
        User user = userService.getUserInformationById(id);
        return Result.success(user);
    }

    @PostMapping("/checkSuccessfully")
    @Operation(summary = "用户信息审核通过接口")
    public Result<Object> checkSuccessfully(@RequestParam Long id, @RequestParam LocalDateTime localDateTime,@RequestParam Integer workType){
        userService.checkSuccessfully(id,localDateTime,workType);
        return Result.success();
    }

    @PutMapping("/checkFail")
    @Operation(summary = "用户信息审核失败接口")
    public Result<Object> checkFail(@RequestParam Long id, @RequestParam LocalDateTime localDateTime){
        userService.checkFail(id,localDateTime);
        return Result.success();
    }

    @PutMapping("/blockAccount")
    @Operation(summary = "封禁用户接口")
    public Result<Object> blockAccount(@RequestParam Long id, @RequestParam LocalDateTime localDateTime){
        userService.blockAccount(id,localDateTime);
        return Result.success();
    }

    @PutMapping("/lift")
    @Operation(summary = "解禁用户接口")
    public Result<Object> lift(@RequestParam Long id, @RequestParam LocalDateTime localDateTime){
        userService.lift(id,localDateTime);
        return Result.success();
    }

    @GetMapping("/getAll")
    @Operation(summary = "获取所有用户接口")
    public Result<List<User>> getAllUsers(){
        List<User> list = userService.getAllUsers();
        return Result.success(list);
    }
}
