package net.development.mitw.tablist.tab;

import net.development.mitw.tablist.abstraction.EntityPlayerWrapper;
import org.bukkit.scoreboard.Team;

public interface MultipleRows extends TabRow {

    @Override
    public default EntityPlayerWrapper getRowPlayer() {
        return null;
    }

    public void updateRows();

    public int getLength();

    public String[] getRows();

    public MultipleRowObj[] getMultipleRowObjs();

    public void setMultipleRowObjs(MultipleRowObj[] multipleRowObj);

    public MultipleRowObj getFromSlot(int slot);
}
