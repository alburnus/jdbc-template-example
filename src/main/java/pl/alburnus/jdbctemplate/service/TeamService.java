package pl.alburnus.jdbctemplate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import pl.alburnus.jdbctemplate.entity.Team;
import pl.alburnus.jdbctemplate.vo.TeamVo;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Service
public class TeamService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamService.class);

    private static final String TEAM_ID_COLUMN = "teamId";
    private static final String TEAMMATE_ID_COLUMN = "teammateId";
    private static final String TEAM_NAME_COLUMN = "teamName";
    private static final String TEAMMATE_NAME_COLUMN = "teammateName";

    private final JdbcTemplate jdbcTemplate;

    public TeamService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Team> getAllTeam() {
        String sql = "SELECT " +
                "id AS teamId, " +
                "name AS teamName " +
                "FROM team";

        return jdbcTemplate.query(sql, (rs, rownum) -> Team.builder()
                .id(rs.getLong(TEAM_ID_COLUMN))
                .name(rs.getString(TEAM_NAME_COLUMN))
                .build()
        );
    }

    public List<Team> getAllTeamWithTeammate() {
        String sql = "SELECT " +
                "t.id AS teamId, " +
                "t.name AS teamName, " +
                "m.id AS teammateId, " +
                "m.name AS teammateName " +
                "FROM team t " +
                "LEFT JOIN teammate m ON m.team_id = t.id";

        return jdbcTemplate.query(sql, (ResultSetExtractor<List<Team>>) resultSet -> {
                    Map<Long, Team> results = new HashMap<>();
                    while (resultSet.next()) {
                        Long teamId = resultSet.getLong(TEAM_ID_COLUMN);
                        Long teammateId = resultSet.getLong(TEAMMATE_ID_COLUMN);
                        String teamName = resultSet.getString(TEAM_NAME_COLUMN);
                        String teammateName = resultSet.getString(TEAMMATE_NAME_COLUMN);

                        Team team = results.get(teamId);
                        if (Objects.isNull(team)) {
                            team = Team.builder()
                                    .id(teamId)
                                    .name(teamName)
                                    .build();
                        }

                        if (teammateId > 0) {
                            team.addTeammate(teammateId, teammateName);
                        }

                        results.put(teamId, team);
                    }

                    return new ArrayList<>(results.values());
                }
        );
    }

    public void create(TeamVo team) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO team (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, team.getName());
            return statement;
        }, holder);

        Integer teamId = (Integer) holder.getKeys().get("id");
        if (Objects.nonNull(team.getTeammates())) {
            team.getTeammates().forEach(teammateVo -> {
                jdbcTemplate.update("INSERT INTO teammate (name, team_id) VALUES (?, ?)", teammateVo.getName(), teamId);
            });
        }

        LOG.info("Key: {}", teamId);
    }
}
