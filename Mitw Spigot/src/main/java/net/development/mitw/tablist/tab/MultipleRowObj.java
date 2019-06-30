package net.development.mitw.tablist.tab;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.tablist.abstraction.EntityPlayerWrapper;
import org.bukkit.scoreboard.Team;

@Getter
@Setter
@AllArgsConstructor
public class MultipleRowObj {

    private final EntityPlayerWrapper entityPlayerWrapper;
    private final Team team;
    private final int slot;

}
