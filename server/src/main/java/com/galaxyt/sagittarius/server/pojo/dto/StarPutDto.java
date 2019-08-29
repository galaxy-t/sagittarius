package com.galaxyt.sagittarius.server.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StarPutDto {

    private Integer namespaceId;

    private List<StarDto> stars;

}
