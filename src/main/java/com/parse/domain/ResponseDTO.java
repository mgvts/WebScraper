package com.parse.domain;

import lombok.Data;

@Data
public class ResponseDTO {
    String title;
    String url;
    String type;
    ProtocolDTO protocolDTO;
}
