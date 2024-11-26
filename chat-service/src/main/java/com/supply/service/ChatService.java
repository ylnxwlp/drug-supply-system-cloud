package com.supply.service;

import com.supply.domain.dto.ChatInformationSelectDTO;
import com.supply.domain.vo.ChatInformationVO;
import com.supply.domain.vo.ChatQueuesVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {

    void createChatQueue(Long id);

    List<ChatQueuesVO> getChatQueues();

    List<ChatInformationVO> getChatHistoryInformation(Long id, Integer times);

    List<ChatInformationVO> selectChatInformationDuringAPeriod(Long id, ChatInformationSelectDTO chatInformationSelectDTO);

    List<ChatInformationVO> getChatInformation(Long id);

}
