package net.chrotos.wakatime;

import lombok.Builder;

@Builder
public class Heartbeat {
    private String entity;
    private String type;
    private String category;
    private float time;
    private String project;
}
