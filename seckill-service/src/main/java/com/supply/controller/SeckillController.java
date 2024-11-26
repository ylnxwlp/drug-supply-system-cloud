package com.supply.controller;

import com.limit.annotation.RateLimit;
import com.supply.constant.MessageConstant;
import com.supply.domain.vo.FlashSaleDrugVO;
import com.supply.result.Result;
import com.supply.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seckill")
@Tag(name = "秒杀部分接口")
@Slf4j
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    @GetMapping("/getFlashSaleDrug")
    @Operation(summary = "获取正在抢购和即将开始抢购的药品信息接口")
    public Result<List<FlashSaleDrugVO>> getFlashSaleDrugs() {
        List<FlashSaleDrugVO> list = seckillService.getFlashSaleDrugs();
        if (list != null) {
            return Result.success(list);
        } else {
            return Result.error(MessageConstant.INTERNET_ERROR);
        }
    }

    @PostMapping("/flashSale")
    @Operation(summary = "秒杀接口")
    @RateLimit(keyPrefix = "seckill", maxRequests = 200)
    public Result<Object> flashSale(@RequestParam Long id) {
        seckillService.flashSale(id);
        return Result.success();
    }
}
