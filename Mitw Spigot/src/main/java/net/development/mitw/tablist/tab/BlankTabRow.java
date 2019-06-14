package net.development.mitw.tablist.tab;

import net.development.mitw.tablist.abstraction.EntityPlayerWrapper;
import net.development.mitw.tablist.util.StringUtil;
import org.bukkit.scoreboard.Team;

public class BlankTabRow implements TabRow, StandardRow
{
    private EntityPlayerWrapper rowPlayer;
    private Team rowTeam;

    @Override
    public String getRowString() {
        return new StringUtil().randomColorString();
    }

    @Override
    public void setRowPlayer(final EntityPlayerWrapper rowPlayer) {
        this.rowPlayer = rowPlayer;
    }

    @Override
    public void setRowTeam(final Team rowTeam) {
        this.rowTeam = rowTeam;
    }

    @Override
    public EntityPlayerWrapper getRowPlayer() {
        return this.rowPlayer;
    }
}
