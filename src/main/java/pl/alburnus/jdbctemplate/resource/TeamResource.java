package pl.alburnus.jdbctemplate.resource;

import org.springframework.web.bind.annotation.*;
import pl.alburnus.jdbctemplate.entity.Team;
import pl.alburnus.jdbctemplate.service.TeamService;
import pl.alburnus.jdbctemplate.vo.TeamVo;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TeamResource {

    private final TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/team")
    public List<Team> getAllTeam() {
        return teamService.getAllTeam();
    }

    @GetMapping("/team/teammate")
    public List<Team> getAllTeamWithTeammate() {
        return teamService.getAllTeamWithTeammate();
    }

    @PostMapping("/team")
    public void createTeam(@RequestBody TeamVo team) {
        teamService.create(team);
    }
}
