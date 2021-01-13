package samadev.scorex.example;

import org.bukkit.plugin.java.JavaPlugin;
import samadev.scorex.ScoreX;

public class ExampleMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // 1L means the scoreboard updates every tick (minecraft servers run at 20 ticks per second)
        new ScoreX(this, new ExampleBoard(), 1L);
    }
}
