package pl.alburnus.jdbctemplate.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Teammate {

    private long id;

    private String name;

    private Long teamId;
}