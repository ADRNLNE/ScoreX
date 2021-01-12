package samadev.scorex;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreX extends BukkitRunnable implements Listener {

    @Getter private static ScoreX inst;
    @Getter private JavaPlugin plugin;
    @Getter private BoardContents contents;
    @Getter private List<PlayerBoard> boards;

    public ScoreX(JavaPlugin plugin, BoardContents contents, long interval) {
        this.inst = this;
        this.plugin = plugin;
        this.contents = contents;
        this.boards = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        runTaskTimer(plugin, 0L, interval);
    }

    public PlayerBoard getBoard(Player player) {
        for (PlayerBoard board : boards) if (board.getPlayer().equals(player)) return board;
        return null;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if (event.getPlayer().getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        boards.add(new PlayerBoard(event.getPlayer()));
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        boards.remove(getBoard(event.getPlayer()));
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> getBoard(p).update());
    }
}

class PlayerBoard {

    @Getter private Player player;
    @Getter private List<String> lines;
    @Getter private Objective objective;

    public PlayerBoard(Player player) {
        this.player = player;
        this.lines = new ArrayList<>();
        this.objective = getScoreboard().registerNewObjective("ScoreX", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', ScoreX.getInst().getContents().getTitle(player)));
    }

    public void update() {
        String title = ChatColor.translateAlternateColorCodes('&', ScoreX.getInst().getContents().getTitle(player));
        List<String> lines = ScoreX.getInst().getContents().getLines(player).stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        Collections.reverse(lines);
        objective.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);
        if (getScoreboard().getEntries().size() != lines.size()) getScoreboard().getEntries().forEach(getScoreboard()::resetScores);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i), prefix = line, suffix = "", name = ChatColor.values()[i].toString() + ChatColor.RESET.toString();
            Team team = getScoreboard().getTeam(name);
            if (team == null) {
                team = getScoreboard().registerNewTeam(name);
                team.addEntry(name);
            }
            if (line.length() > 16) {
                prefix = line.substring(0, 16);
                if (prefix.endsWith("\u00a7")) {
                    prefix = prefix.substring(0, prefix.length() - 1);
                    suffix = "\u00a7" + suffix;
                }
                suffix = StringUtils.left(ChatColor.getLastColors(prefix) + suffix + line.substring(16), 16);
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            objective.getScore(team.getName()).setScore(i + 1);
        }
    }

    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }
}
