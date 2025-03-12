package com.enigmapulse.thunderclap.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatMessage {
    private String sender;
    private String recipient;
    private String content;

}
