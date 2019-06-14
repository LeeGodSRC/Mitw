package net.development.mitw.tablist.tab;

import net.development.mitw.tablist.abstraction.EntityPlayerWrapper;
import org.bukkit.scoreboard.*;

public class DynamicTabRow implements TabRow, StandardRow
{
    private EntityPlayerWrapper rowPlayer;
    private String rowString;
    private Team rowTeam;

    public DynamicTabRow(final String rowString) {
        this.rowString = rowString;
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

    public void updateRow() {
        final DynamicRow dynamicRow = (DynamicRow)this;
        final String prefix = dynamicRow.getPrefix();
        if (prefix != null) {
            this.rowTeam.setPrefix(prefix);
        }
        final String suffix = dynamicRow.getSuffix();
        if (suffix != null) {
            this.rowTeam.setSuffix(suffix);
        }
    }

    @Override
    public String getRowString() {
        return this.rowString;
    }
}
