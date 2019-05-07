package io.server.content.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import plugin.command.impl.donator.DonatorBankCommand;
import plugin.command.impl.donator.DonatorYellCommand;
import plugin.command.impl.donator.EZCommandZone;
import plugin.command.impl.donator.ExtremeDonatorZone;
import plugin.command.impl.donator.NewDZoneCommandFour;
import plugin.command.impl.donator.NewDZoneCommandOne;
import plugin.command.impl.donator.NewDZoneCommandThree;
import plugin.command.impl.donator.NewDZoneCommandTwo;
import plugin.command.impl.donator.PortalsZoneCommand;
import plugin.command.impl.donator.SponsorStoreCommand;
import plugin.command.impl.moderator.BanCommand;
import plugin.command.impl.moderator.Disable15DR;
import plugin.command.impl.moderator.Disable30DR;
import plugin.command.impl.moderator.DisableAVO;
import plugin.command.impl.moderator.DisableDoubleDrops;
import plugin.command.impl.moderator.DisableDoubleXP;
import plugin.command.impl.moderator.DisablePKP;
import plugin.command.impl.moderator.DoubleAVO;
import plugin.command.impl.moderator.Enable15DR;
import plugin.command.impl.moderator.Enable30DR;
import plugin.command.impl.moderator.EnableDoubleDrops;
import plugin.command.impl.moderator.EnableDoubleXP;
import plugin.command.impl.moderator.EnablePKP;
import plugin.command.impl.moderator.HpEventCommand;
import plugin.command.impl.moderator.IconTestCommand;
import plugin.command.impl.moderator.InvisibleCommand;
import plugin.command.impl.moderator.IpbanCommand;
import plugin.command.impl.moderator.IpmuteCommand;
import plugin.command.impl.moderator.JailCommand;
import plugin.command.impl.moderator.KickCommand;
import plugin.command.impl.moderator.ManagementCommand;
import plugin.command.impl.moderator.MasterCommand;
import plugin.command.impl.moderator.MuteCommand;
import plugin.command.impl.moderator.PrivateZoneCommand;
import plugin.command.impl.moderator.QuickHelpCommand;
import plugin.command.impl.moderator.RemindVote;
import plugin.command.impl.moderator.SpawnCustomCommand;
import plugin.command.impl.moderator.TeletoCommand;
import plugin.command.impl.moderator.TeletomeCommand;
import plugin.command.impl.moderator.Unbancommand;
import plugin.command.impl.moderator.UnjailCommand;
import plugin.command.impl.moderator.UnmuteCommand;
import plugin.command.impl.owner.AllToMeCommand;
import plugin.command.impl.owner.AnimationCommand;
import plugin.command.impl.owner.ArenaSpawnCommand;
import plugin.command.impl.owner.BroadcastCommand;
import plugin.command.impl.owner.ChimeraSpawnCommand;
import plugin.command.impl.owner.DefaultBankCommand;
import plugin.command.impl.owner.DeveloperInstanceCommand;
import plugin.command.impl.owner.DownCommand;
import plugin.command.impl.owner.EventBossInterfaceHide;
import plugin.command.impl.owner.EventBossInterfaceShow;
import plugin.command.impl.owner.FindCommand;
import plugin.command.impl.owner.GalvekSpawnCommand;
import plugin.command.impl.owner.GiveAllCommand;
import plugin.command.impl.owner.GiveRankCommand;
import plugin.command.impl.owner.GodCommand;
import plugin.command.impl.owner.GraphicCommand;
import plugin.command.impl.owner.InterfaceCommand;
import plugin.command.impl.owner.ItemCommand;
import plugin.command.impl.owner.JusticarSpawnCommand;
import plugin.command.impl.owner.KickAllCommand;
import plugin.command.impl.owner.KillCommand;
import plugin.command.impl.owner.NpcCommand;
import plugin.command.impl.owner.ObjectCommand;
import plugin.command.impl.owner.PetCommand;
import plugin.command.impl.owner.PnpcCommand;
import plugin.command.impl.owner.RandomEvent;
import plugin.command.impl.owner.ReloadCommand;
import plugin.command.impl.owner.ReloadDrops;
import plugin.command.impl.owner.RemovePlayerTask;
import plugin.command.impl.owner.SaveAll;
import plugin.command.impl.owner.SkotizoSpawnCommand;
import plugin.command.impl.owner.SpawnPorazdirCommand;
import plugin.command.impl.owner.SpecCommand;
import plugin.command.impl.owner.StartEventCommand;
import plugin.command.impl.owner.TeleCommand;
import plugin.command.impl.owner.TeleCommand1;
import plugin.command.impl.owner.UpCommand;
import plugin.command.impl.owner.UpdateCommand;
import plugin.command.impl.owner.posCommand;
import plugin.command.impl.player.AdvancedZone;
import plugin.command.impl.player.AlienCommand;
import plugin.command.impl.player.AncientsCommand;
import plugin.command.impl.player.AnswerTriviaCommand;
import plugin.command.impl.player.ArenaZoneCommand;
import plugin.command.impl.player.Avo1Command;
import plugin.command.impl.player.Avo2Command;
import plugin.command.impl.player.Avo3Command;
import plugin.command.impl.player.Avo4Command;
import plugin.command.impl.player.BarrowsCommand;
import plugin.command.impl.player.BarrowsFix;
import plugin.command.impl.player.CashOutCommand;
import plugin.command.impl.player.ChangePassword;
import plugin.command.impl.player.CheckBank;
import plugin.command.impl.player.CheckPlayers;
import plugin.command.impl.player.ChimeraCommand;
import plugin.command.impl.player.ClaimDonationCommand;
import plugin.command.impl.player.ClaimVote;
import plugin.command.impl.player.Commands;
import plugin.command.impl.player.DiscordCommand;
import plugin.command.impl.player.DonateCommand;
import plugin.command.impl.player.DropInterfaceCommand;
import plugin.command.impl.player.DropSimulatorCommand;
import plugin.command.impl.player.DropsCommand;
import plugin.command.impl.player.DuelArenaCommand;
import plugin.command.impl.player.DuoZombieRaidCommand;
import plugin.command.impl.player.DustiesCommand;
import plugin.command.impl.player.EastsCommand;
import plugin.command.impl.player.EmptyInventoryCommand;
import plugin.command.impl.player.EventsCommand;
import plugin.command.impl.player.ForumCommand;
import plugin.command.impl.player.GalvekCommands;
import plugin.command.impl.player.GambleCommand;
import plugin.command.impl.player.GdzCommand;
import plugin.command.impl.player.Helpcommand;
import plugin.command.impl.player.HomeCommand;
import plugin.command.impl.player.InfernoCommand;
import plugin.command.impl.player.KeyCommand;
import plugin.command.impl.player.MageBankCommand;
import plugin.command.impl.player.MaxStatusCommand;
import plugin.command.impl.player.MediumZone;
import plugin.command.impl.player.OsrsDonationCommand;
import plugin.command.impl.player.PlayerCountCommand;
import plugin.command.impl.player.PlayerGuideCommand;
import plugin.command.impl.player.PouchCommand;
import plugin.command.impl.player.Raids3Command;
import plugin.command.impl.player.Raids4Command;
import plugin.command.impl.player.Raids5Command;
import plugin.command.impl.player.RevenantCaveCommand;
import plugin.command.impl.player.RewardsListCommand;
import plugin.command.impl.player.ShopCommand;
import plugin.command.impl.player.SkillAreaCommand;
import plugin.command.impl.player.SkullCommand;
import plugin.command.impl.player.SlayerTaskCommand;
import plugin.command.impl.player.SoloRaids1Command;
import plugin.command.impl.player.SoloRaids2Command;
import plugin.command.impl.player.SoloRaids3Command;
import plugin.command.impl.player.SoloRaids4Command;
import plugin.command.impl.player.SoloRaids5Command;
import plugin.command.impl.player.StaffCommand;
import plugin.command.impl.player.StuckCommand;
import plugin.command.impl.player.TarnCommand;
import plugin.command.impl.player.ThreadCommand;
import plugin.command.impl.player.TrainZoneCommand;
import plugin.command.impl.player.VaultCommand;
import plugin.command.impl.player.VoteCommand;
import plugin.command.impl.player.Wests;
import plugin.command.impl.player.YouTubeCommand;
import plugin.command.impl.player.ZombieRaidCommand;
import plugin.command.impl.player.ZulrahCommand;

/**
 * Stores Commands
 * 
 * @author Nerik#8690
 *
 */
public class CommandManager {
	

	public static final Map<String[], Command> PLUGIN = new HashMap<>();
	public static final Map<String, Command> PLUGIN_INPUT = new HashMap<>();

	static {
		
		/*
		 * @Player Commands
		 * Join ::discord for huge giveaways! and to be kept upto date regarding updates!
		 */
		
		PLUGIN.putIfAbsent(new String[] { "osrsdonation", "donateosrs", "osrs", "07" }, new OsrsDonationCommand());
		PLUGIN.putIfAbsent(new String[] { "events" }, new EventsCommand());
		PLUGIN.putIfAbsent(new String[] { "pos" }, new PosCommand());
		PLUGIN.putIfAbsent(new String[] { "thread" }, new ThreadCommand());
		PLUGIN.putIfAbsent(new String[] { "ancients" }, new AncientsCommand());
		PLUGIN.putIfAbsent(new String[] { "maxhit" }, new MaxStatusCommand());
		PLUGIN.putIfAbsent(new String[] { "changepassword", "changepass" }, new ChangePassword());
		PLUGIN.putIfAbsent(new String[] { "easts" }, new EastsCommand());
		PLUGIN.putIfAbsent(new String[] { "alien" }, new AlienCommand());
		PLUGIN.putIfAbsent(new String[] { "commands", "command" }, new Commands());
		PLUGIN.putIfAbsent(new String[] { "home", "hom" }, new HomeCommand());
		PLUGIN.putIfAbsent(new String[] { "train", "training" }, new TrainZoneCommand());
		PLUGIN.putIfAbsent(new String[] { "chimera", "chimeraevent" }, new ChimeraCommand());
		PLUGIN.putIfAbsent(new String[] { "medium", "mz", "mediumzone" }, new MediumZone());
	//	PLUGIN.putIfAbsent(new String[] { "advanced", "adv", "advancedzone" }, new AdvancedZone());
		PLUGIN.putIfAbsent(new String[] { "pouch" }, new PouchCommand());
		PLUGIN.putIfAbsent(new String[] { "shops", "shop", }, new ShopCommand());
		PLUGIN.putIfAbsent(new String[] { "drops", "drop", "droplist", "droptable" }, new DropsCommand());
		PLUGIN.putIfAbsent(new String[] { "simulate", "simulator" }, new DropSimulatorCommand());
		PLUGIN.putIfAbsent(new String[] { "vote" }, new VoteCommand());
		PLUGIN.putIfAbsent(new String[] { "skull" }, new SkullCommand());
		PLUGIN.putIfAbsent(new String[] { "duel", "duelarena" }, new DuelArenaCommand());
		PLUGIN.putIfAbsent(new String[] { "barrows" }, new BarrowsCommand());
		PLUGIN.putIfAbsent(new String[] { "inferno", "Inferno" }, new InfernoCommand());
		PLUGIN.putIfAbsent(new String[] { "skill", "skillingarea" }, new SkillAreaCommand());
		PLUGIN.putIfAbsent(new String[] { "claim", "donated" }, new ClaimDonationCommand());
		PLUGIN.putIfAbsent(new String[] { "donate", "store", "Store" }, new DonateCommand());
		PLUGIN.putIfAbsent(new String[] { "voted", "claimvote", "reward" }, new ClaimVote());
		PLUGIN.putIfAbsent(new String[] { "vault", "vaultamount" }, new VaultCommand());
		PLUGIN.putIfAbsent(new String[] { "drops", "drop" }, new DropInterfaceCommand());
		PLUGIN.putIfAbsent(new String[] { "gamble", "gamblezone" }, new GambleCommand());
		PLUGIN.putIfAbsent(new String[] { "arena", "arenazone" }, new ArenaZoneCommand());
		PLUGIN.putIfAbsent(new String[] { "zul", "zulrah" }, new ZulrahCommand());
		PLUGIN.putIfAbsent(new String[] { "cashout" }, new CashOutCommand());
		PLUGIN.putIfAbsent(new String[] { "help" }, new Helpcommand());
		PLUGIN.putIfAbsent(new String[] { "tarn", "mutant", "mutantTarn" }, new TarnCommand());
		PLUGIN.putIfAbsent(new String[] { "answer", "trivia" }, new AnswerTriviaCommand());
		PLUGIN.putIfAbsent(new String[] { "staff", "staffonline" }, new StaffCommand());
		PLUGIN.putIfAbsent(new String[] { "forums", "forum" }, new ForumCommand());
		PLUGIN.putIfAbsent(new String[] { "discord" }, new DiscordCommand());
		PLUGIN.putIfAbsent(new String[] { "stuck" }, new StuckCommand());
		PLUGIN.putIfAbsent(new String[] { "wests", "west" }, new Wests());
		PLUGIN.putIfAbsent(new String[] { "mb", "magebank" }, new MageBankCommand());
		PLUGIN.putIfAbsent(new String[] { "gd", "gdz" }, new GdzCommand());
		PLUGIN.putIfAbsent(new String[] { "slayertask", "task" }, new SlayerTaskCommand());
		PLUGIN.putIfAbsent(new String[] { "players", "online" }, new PlayerCountCommand());
		PLUGIN.putIfAbsent(new String[] { "empty", "emptyinventory" }, new EmptyInventoryCommand());
		PLUGIN.putIfAbsent(new String[] { "dusties", "dusti" }, new DustiesCommand());
		PLUGIN.putIfAbsent(new String[] { "key", "security" }, new KeyCommand());
		PLUGIN.putIfAbsent(new String[] { "galvek", "Galvek" }, new GalvekCommands());
		PLUGIN.putIfAbsent(new String[] { "es", "ed" }, new EZCommandZone());
		PLUGIN.putIfAbsent(new String[] { "barrowsfix", "bugfix" }, new BarrowsFix());
		PLUGIN.putIfAbsent(new String[] { "revs", "revcave" }, new RevenantCaveCommand());
		PLUGIN.putIfAbsent(new String[] { "moneyguide", "guide", "giveaway"}, new YouTubeCommand());
		PLUGIN.putIfAbsent(new String[] { "soloraids1"}, new SoloRaids1Command());
		PLUGIN.putIfAbsent(new String[] { "soloraids2"}, new SoloRaids2Command());
	//	PLUGIN.putIfAbsent(new String[] { "soloraids3"}, new SoloRaids3Command());
	//	PLUGIN.putIfAbsent(new String[] { "soloraids4"}, new SoloRaids4Command());
	//	PLUGIN.putIfAbsent(new String[] { "soloraids5"}, new SoloRaids5Command());
		PLUGIN.putIfAbsent(new String[] { "rewards"}, new RewardsListCommand());
	//	PLUGIN.putIfAbsent(new String[] { "raids3"}, new Raids3Command());
	//	PLUGIN.putIfAbsent(new String[] { "raids4"}, new Raids4Command());
	//	PLUGIN.putIfAbsent(new String[] { "raids5"}, new Raids5Command());
		PLUGIN.putIfAbsent(new String[] { "avov1", "avo1"}, new Avo1Command());
		PLUGIN.putIfAbsent(new String[] { "avov2", "avo2"}, new Avo2Command());
		PLUGIN.putIfAbsent(new String[] { "avov3", "avo3"}, new Avo3Command());
		PLUGIN.putIfAbsent(new String[] { "avov4", "avo4"}, new Avo4Command());
	//	PLUGIN.putIfAbsent(new String[] { "zombiesolo", "zs", "zombieraidsolo"}, new ZombieRaidCommand());
	//	PLUGIN.putIfAbsent(new String[] { "duozombie", "duozo", "duozombieraid"}, new DuoZombieRaidCommand());


		/*
		 * @Donator Command
		 */
		PLUGIN.putIfAbsent(new String[] { "sponsor", "sponsorstore" }, new SponsorStoreCommand());
		PLUGIN.putIfAbsent(new String[] { "bank", "banks" }, new DonatorBankCommand());
		PLUGIN.putIfAbsent(new String[] { "donatorzone1", "dzone1" }, new NewDZoneCommandOne());
	//	PLUGIN.putIfAbsent(new String[] { "donatorzone2", "dzone2" }, new NewDZoneCommandTwo());
	//	PLUGIN.putIfAbsent(new String[] { "donatorzone3", "dzone3" }, new NewDZoneCommandThree());
	//	PLUGIN.putIfAbsent(new String[] { "donatorzone4", "dzone4" }, new NewDZoneCommandFour());
		PLUGIN.putIfAbsent(new String[] { "portals", "portalzone" }, new PortalsZoneCommand());
		PLUGIN.putIfAbsent(new String[] { "yell", "shout" }, new DonatorYellCommand());
		PLUGIN.putIfAbsent(new String[] { "ez", "extremezone","ezone" }, new ExtremeDonatorZone());

		/**
		 * @Moderator/Helper Commands
		 */
		
		PLUGIN.putIfAbsent(new String[] { "check", "checkplayer", "investigate"}, new CheckPlayers());
		PLUGIN.putIfAbsent(new String[] { "checkbank", "playerbank"}, new CheckBank());
		PLUGIN.putIfAbsent(new String[] { "mute", "mutee" }, new MuteCommand());
		PLUGIN.putIfAbsent(new String[] { "unmute", "unmutee" }, new UnmuteCommand());
		PLUGIN.putIfAbsent(new String[] { "jail", "jaill" }, new JailCommand());
		PLUGIN.putIfAbsent(new String[] { "unjail", "unjaill" }, new UnjailCommand());
		PLUGIN.putIfAbsent(new String[] { "privatezone", "staffzone" }, new PrivateZoneCommand());
		PLUGIN.putIfAbsent(new String[] { "teleto", "t2", "tele2" }, new TeletoCommand());
		PLUGIN.putIfAbsent(new String[] { "teletome", "t2m", "tele2me" }, new TeletomeCommand());
		PLUGIN.putIfAbsent(new String[] { "bann", "ban", "ruin" }, new BanCommand());
		PLUGIN.putIfAbsent(new String[] { "unban", "unbann" }, new Unbancommand());
		PLUGIN.putIfAbsent(new String[] { "kick", "kickk" }, new KickCommand());
		PLUGIN.putIfAbsent(new String[] { "ipban", "ip" }, new IpbanCommand());
		PLUGIN.putIfAbsent(new String[] { "ipmute", "imute" }, new IpmuteCommand());
		PLUGIN.putIfAbsent(new String[] { "remindvote", "allvote" }, new RemindVote());
		PLUGIN.putIfAbsent(new String[] { "quickhelp", "offerhelp" }, new QuickHelpCommand());
		PLUGIN.putIfAbsent(new String[] { "invis", "hide" }, new InvisibleCommand());
		PLUGIN.putIfAbsent(new String[] { "panel1", "panel2" }, new ManagementCommand());
		PLUGIN.putIfAbsent(new String[] { "hp", "hp1" }, new HpEventCommand());

		PLUGIN.putIfAbsent(new String[] { "test", "create" }, new StartEventCommand());
		PLUGIN.putIfAbsent(new String[] { "kickall", "kickeveryone" }, new KickAllCommand());
		PLUGIN.putIfAbsent(new String[] { "item" }, new ItemCommand());
		PLUGIN.putIfAbsent(new String[] { "master" }, new MasterCommand());
		PLUGIN.putIfAbsent(new String[] { "instance" }, new DeveloperInstanceCommand());
		PLUGIN.putIfAbsent(new String[] { "customs" }, new SpawnCustomCommand());
		PLUGIN.putIfAbsent(new String[] { "defaultbank" }, new DefaultBankCommand());
		PLUGIN.putIfAbsent(new String[] { "master", "max" }, new MasterCommand());
		PLUGIN.putIfAbsent(new String[] { "find", "finditem" }, new FindCommand());
		PLUGIN.putIfAbsent(new String[] { "anim", "animation" }, new AnimationCommand());
		PLUGIN.putIfAbsent(new String[] { "allpets", "allpet" }, new PetCommand());
		PLUGIN.putIfAbsent(new String[] { "gfx", "graphic" }, new GraphicCommand());
		PLUGIN.putIfAbsent(new String[] { "spec", "special" }, new SpecCommand());
		PLUGIN.putIfAbsent(new String[] { "pnpc", "transform" }, new PnpcCommand());
		PLUGIN.putIfAbsent(new String[] { "up", "high" }, new UpCommand());
		PLUGIN.putIfAbsent(new String[] { "down", "low" }, new DownCommand());
		PLUGIN.putIfAbsent(new String[] { "god", "beast" }, new GodCommand());
		PLUGIN.putIfAbsent(new String[] { "interface", "int" }, new InterfaceCommand());
		PLUGIN.putIfAbsent(new String[] { "npc", "spawnnpc" }, new NpcCommand());
		PLUGIN.putIfAbsent(new String[] { "openguide" }, new PlayerGuideCommand());
		PLUGIN.putIfAbsent(new String[] { "obj", "object" }, new ObjectCommand());
		PLUGIN.putIfAbsent(new String[] { "arenas", "spawnarena" }, new ArenaSpawnCommand());
		PLUGIN.putIfAbsent(new String[] { "galveks", "spawngalvek" }, new GalvekSpawnCommand());
		PLUGIN.putIfAbsent(new String[] { "justicars", "spawnjusticar" }, new JusticarSpawnCommand());
		PLUGIN.putIfAbsent(new String[] { "chimeras", "spawnchimera" }, new ChimeraSpawnCommand());
		PLUGIN.putIfAbsent(new String[] { "skotizos", "spanwskotizo" }, new SkotizoSpawnCommand());
		PLUGIN.putIfAbsent(new String[] { "show", "showboss" }, new EventBossInterfaceShow());
		PLUGIN.putIfAbsent(new String[] { "hidebosses", "hideboss" }, new EventBossInterfaceHide());
		PLUGIN.putIfAbsent(new String[] { "giveall", "give" }, new GiveAllCommand());
		PLUGIN.putIfAbsent(new String[] { "update", "udpateserver" }, new UpdateCommand());
		PLUGIN.putIfAbsent(new String[] { "removetask", "slayer" }, new RemovePlayerTask());
		PLUGIN.putIfAbsent(new String[] { "randomevent", "randomeven" }, new RandomEvent());
		PLUGIN.putIfAbsent(new String[] { "teleall", "all2me" }, new AllToMeCommand());
		PLUGIN.putIfAbsent(new String[] { "broadcast", "announcement" }, new BroadcastCommand());
		PLUGIN.putIfAbsent(new String[] { "kill", "killplayer" }, new KillCommand());
		PLUGIN.putIfAbsent(new String[] { "tele" }, new TeleCommand());
		PLUGIN.putIfAbsent(new String[] { "tele1" }, new TeleCommand1());
		PLUGIN.putIfAbsent(new String[] { "pos" }, new posCommand());

		PLUGIN.putIfAbsent(new String[] { "giverank" }, new GiveRankCommand());
		PLUGIN.putIfAbsent(new String[] { "Saveall", "saveall" }, new SaveAll());
		PLUGIN.putIfAbsent(new String[] { "reloadall", "reloadall" }, new ReloadCommand());
		PLUGIN.putIfAbsent(new String[] { "reloaddrops", "reloaddrop" }, new ReloadDrops());
		
		PLUGIN.putIfAbsent(new String[] { "testicon" }, new IconTestCommand());
		PLUGIN.putIfAbsent(new String[] { "enablepkp" }, new EnablePKP());
		PLUGIN.putIfAbsent(new String[] { "disablepkp" }, new DisablePKP());
		PLUGIN.putIfAbsent(new String[] { "enabledrops" }, new EnableDoubleDrops());
		PLUGIN.putIfAbsent(new String[] { "disabledrops" }, new DisableDoubleDrops());
		PLUGIN.putIfAbsent(new String[] { "doublexp" }, new EnableDoubleXP());
		PLUGIN.putIfAbsent(new String[] { "disablexp", "disablexp" }, new DisableDoubleXP());
		PLUGIN.putIfAbsent(new String[] { "enable15dr", "15dr" }, new Enable15DR());
		PLUGIN.putIfAbsent(new String[] { "disable15dr", "disable15dr" }, new Disable15DR());
		PLUGIN.putIfAbsent(new String[] { "enable30dr", "30dr" }, new Enable30DR());
		PLUGIN.putIfAbsent(new String[] { "disable30dr", "disable30dr" }, new Disable30DR());
		PLUGIN.putIfAbsent(new String[] { "enableavo" }, new DoubleAVO());
		PLUGIN.putIfAbsent(new String[] { "disableavo" }, new DisableAVO());
		PLUGIN.putIfAbsent(new String[] { "porazdir", "spawnporazdir" }, new SpawnPorazdirCommand());

		
		for (Entry<String[], Command> map : PLUGIN.entrySet()) {
			for (int i = 0; i < map.getKey().length; i++) {
				PLUGIN_INPUT.putIfAbsent(map.getKey()[i], map.getValue());
			}
		}

		System.out.println("[Commands] Loaded " + PLUGIN.size() + " Commands");
	}
}
