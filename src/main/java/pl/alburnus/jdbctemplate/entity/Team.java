package pl.alburnus.jdbctemplate.entity;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class Team {

    private long id;

    private String name;

    private List<Teammate> teammates = new ArrayList<>();

    public void addTeammate(long teammateId, String teammateName) {
        this.teammates.add(Teammate.builder()
                .id(teammateId)
                .name(teammateName)
                .teamId(this.id)
                .build()
        );
    }
}