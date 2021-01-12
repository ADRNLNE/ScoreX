package samadev.scorex;

import org.bukkit.entity.Player;

import java.util.List;

public interface BoardContents {

    String getTitle(Player player);

    List<String> getLines(Player player);

}
