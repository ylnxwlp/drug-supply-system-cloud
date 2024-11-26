package com.supply.service;

import com.supply.domain.vo.FlashSaleDrugVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeckillService {

    List<FlashSaleDrugVO> getFlashSaleDrugs();

    void flashSale(Long id);
}
