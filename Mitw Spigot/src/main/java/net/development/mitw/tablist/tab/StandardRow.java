package net.development.mitw.tablist.tab;

import net.development.mitw.tablist.abstraction.EntityPlayerWrapper;
import org.bukkit.scoreboard.Team;

public interface StandardRow
{
    void setRowTeam(final Team p0);

    String getRowString();

    void setRowPlayer(final EntityPlayerWrapper p0);
}
