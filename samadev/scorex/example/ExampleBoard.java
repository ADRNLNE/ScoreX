package samadev.scorex.example;

import org.bukkit.entity.Player;
import samadev.scorex.BoardContents;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExampleBoard implements BoardContents {

    @Override
    public String getTitle(Player player) {
        return "&5&lExample";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7&m--------------------");
        lines.add("&b&lDate");
        lines.add(" " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));
        lines.add("");
        lines.add("&d&lTime");
        lines.add(" " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        lines.add("");
        lines.add("&e&lLocation");
        lines.add(" " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ());
        if (!player.isSneaking()) {
            lines.add("");
            lines.add("&c&lYou are sneaking!");
        }
        lines.add("&7&m--------------------");
        return lines;
    }
}
