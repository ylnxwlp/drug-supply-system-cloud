package com.supply.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatErrorInformation implements Serializable {

    private Long id;

    private Long queueId;

    private Long sendUserId;

    private Long receiveUserId;

    private String information;

    private String image;

}
