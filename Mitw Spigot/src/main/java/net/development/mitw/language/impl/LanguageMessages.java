package net.development.mitw.language.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

public class LanguageMessages {

	public String zh_tw_noSpam = ChatColor.RED + "&c請不要嘗試發送的同樣訊息";
	public String en_us_noSpam = ChatColor.RED + "please don't repeat the same message";

	public String zh_tw_muted = ChatColor.RED + "&c請不要說難聽的話,謝謝你";
	public String en_us_muted = ChatColor.RED + "Do Not toxic, thank you";

	public String zh_tw_inMute = "&c抱歉 你目前處於禁言狀態,你還需要約&f <sec> &c秒後才能說話";
	public String en_us_inMute = "&cSorry, you have been muted, please wait &f <sec> &cbefore type anything";

	public String zh_tw_cooldownChat = "&c請放慢你的說話速度,你還要&f <sec> &c秒後才能說話 ";
	public String en_us_cooldownChat = "&cPlease slow down your chat,you have to wait&f <sec> &cbefore you type anything";

	public String zh_tw_chatreport_usage = "&e使用方法: /chreport <不好聽的文字>";
	public String en_us_chatreport_usage = "&eUsage: /chreport <toxicword>";

	public String zh_tw_chatreport_incooldown = "&c抱歉,您還需要等待 &f<sec>&c 秒後才能再檢舉一次";
	public String en_us_chatreport_incooldown = "&cSorry, but you have to wait &f<sec>&c to report again";

	public String zh_tw_chatreport_success = "&a成功檢舉單字: &f<word>";
	public String en_us_chatreport_success = "&aSuccessful report the word: &fword";

	public List<String> zh_tw_nameMCVoteInfo = Arrays.asList(
			"§8§m---------------------------",
			"§e為Mitw投一票!",
			"§7- 現在你只要在 §bName§fMC §7投Mitw一票",
			"&7- 就可以在Mitw獲得多種獎賞喔!",
			" ",
			"§7網址: §ehttps://namemc.com/server/mitw.rip",
			"&7- 當你成功投了一票之後 只要打上該指令",
			"  §7- §e/vote ok",
			"§7- 就可以獲得獎賞喔!",
			"§8§m---------------------------§f");

	public List<String> en_us_nameMCVoteInfo = Arrays.asList(
			"§8§m---------------------------",
			"§eVote to Mitw!",
			"§7- Now if you have vote to mitw on §bName§fMC",
			"&7- Then you can get a lot gifts from mitw!!",
			" ",
			"§7Link: §ehttps://namemc.com/server/mitw.rip",
			"&7- when you have voted to mitw, then you can type this command",
			"  §7- §e/vote ok",
			"§7- after that you will get the gifts!",
			"§8§m---------------------------§f");
}
