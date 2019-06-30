package net.development.mitw.tablist.tab;

import net.development.mitw.tablist.TablistManager;
import net.development.mitw.tablist.abstraction.EntityPlayerWrapper;
import net.development.mitw.tablist.util.StringUtil;
import net.development.mitw.tablist.util.TabIcon;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Tab
{
    private Player player;
    private String tabTitle;
    private String tabFooter;
    private List<TabRow> tabRows;
    private Random random;

    private Map<Integer, EntityPlayerWrapper> slots = new HashMap<>();

    public Tab(final Player player, final String tabTitle, final String tabFooter) {
        this.tabRows = new ArrayList<TabRow>();
        this.random = new Random();
        this.player = player;
        this.tabTitle = tabTitle;
        this.tabFooter = tabFooter;
    }

    public void updateTab() {
        for (final TabRow tabRow : this.tabRows) {
            if (tabRow instanceof MultipleRows) {
                ((MultipleRows) tabRow).updateRows();
            }
            if (tabRow instanceof DynamicTabRow) {
                ((DynamicTabRow)tabRow).updateRow();
            }
        }
    }

    private EntityPlayerWrapper[] tabRowList7() {
        final EntityPlayerWrapper[] tabRowList = new EntityPlayerWrapper[this.slots.size()];
        for (int i = 0; i < this.slots.size() / 3; ++i) {
            tabRowList[i * 3] = this.slots.get(i);
            tabRowList[i * 3 + 1] = this.slots.get(20 + i);
            tabRowList[i * 3 + 2] = this.slots.get(40 + i);
        }
        return tabRowList;
    }

    private EntityPlayerWrapper[] tabRowList() {
        return slots.values().toArray(new EntityPlayerWrapper[slots.size()]);
    }

    public void clearTab() {
        TablistManager.getInstance().getPacketUtil().clearRows(this.player, this.tabRowList());
        TablistManager.getInstance().getPacketUtil().setFooterAndHeader(this.player, "", "");
    }

    public void sendTab7() {
        TablistManager.getInstance().getPacketUtil().clearTab(this.player);
        TablistManager.getInstance().getPacketUtil().setFooterAndHeader(this.player, this.tabTitle, this.tabFooter);
        TablistManager.getInstance().getPacketUtil().sendRows(this.player, this.tabRowList7());
    }

    public void sendTab() {
        TablistManager.getInstance().getPacketUtil().clearTab(this.player);
        TablistManager.getInstance().getPacketUtil().setFooterAndHeader(this.player, this.tabTitle, this.tabFooter);
        TablistManager.getInstance().getPacketUtil().sendRows(this.player, this.tabRowList());
    }

    public List<TabRow> getTabRows() {
        return this.tabRows;
    }

    private Team getTeam(final Player player, final int index) {
        final String alphabeticalTeamName = new StringUtil().getAlphabeticalString(index) + this.random.nextInt(1000000);
        Team toReturn;
        if (player.getScoreboard().getTeam(alphabeticalTeamName) == null) {
            toReturn = player.getScoreboard().registerNewTeam(alphabeticalTeamName);
        }
        else {
            toReturn = player.getScoreboard().getTeam(alphabeticalTeamName);
        }
        return toReturn;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void addMultipleRow(MultipleRows multipleRows) {
        int index = this.slots.size();

        MultipleRowObj[] multipleRowObjs = new MultipleRowObj[multipleRows.getLength()];

        for (int i = 0; i < multipleRows.getLength(); i++) {
            final EntityPlayerWrapper rowPlayer = TablistManager.getInstance().getPacketUtil().newRow(new StringUtil().fillEndString(new StringUtil().randomColorString()), TabIcon.GREY);
            final Team rowTeam = this.getTeam(this.player, index);
            rowTeam.addEntry(rowPlayer.getName());
            MultipleRowObj multipleRowObj = new MultipleRowObj(rowPlayer, rowTeam, index + i);
            multipleRowObjs[i] = multipleRowObj;
            this.slots.put(index++, rowPlayer);
        }

        multipleRows.setMultipleRowObjs(multipleRowObjs);
        this.tabRows.add(multipleRows);
    }

    public void addTabRow(final StandardRow standardRow, final TabIcon tabIcon) {
        int index = this.slots.size();

        if (standardRow instanceof StaticTabRow) {
            final String[] rowString = new StringUtil().splitString(standardRow.getRowString());
            final EntityPlayerWrapper rowPlayer = TablistManager.getInstance().getPacketUtil().newRow(new StringUtil().fillEndString(rowString[1]), tabIcon);
            standardRow.setRowPlayer(rowPlayer);
            final Team rowTeam = this.getTeam(this.player, index);
            rowTeam.addEntry(rowPlayer.getName());
            if (rowString[0] != null) {
                rowTeam.setPrefix(rowString[0]);
            }
            if (rowString[2] != null) {
                rowTeam.setSuffix(rowString[2]);
            }
            standardRow.setRowTeam(rowTeam);
            this.slots.put(index, rowPlayer);
        }
        else {
            final EntityPlayerWrapper rowPlayer2 = TablistManager.getInstance().getPacketUtil().newRow(new StringUtil().fillEndString(standardRow.getRowString()), tabIcon);
            standardRow.setRowPlayer(rowPlayer2);
            final Team rowTeam2 = this.getTeam(this.player, index);
            rowTeam2.addEntry(rowPlayer2.getName());
            standardRow.setRowTeam(rowTeam2);
            this.slots.put(index, rowPlayer2);
        }
        this.tabRows.add((TabRow)standardRow);
    }

    public void addTabRow(final StandardRow standardRow) {
        this.addTabRow(standardRow, TabIcon.GREY);
    }
}