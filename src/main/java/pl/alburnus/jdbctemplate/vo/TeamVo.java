package pl.alburnus.jdbctemplate.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeamVo {

    private String name;

    private List<TeammateVo> teammates;
}
