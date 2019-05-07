package io.server.game.world.entity.mob.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tritonus.share.ArraySet;

import io.server.Config;
import io.server.content.ActivityLog;
import io.server.content.ActivityLogger;
import io.server.content.Gambling;
import io.server.content.Toolkit;
import io.server.content.achievement.AchievementKey;
import io.server.content.activity.Activity;
import io.server.content.activity.impl.barrows.BrotherData;
import io.server.content.activity.impl.battlerealm.BattleRealmNode;
import io.server.content.activity.record.PlayerRecord;
import io.server.content.clanchannel.channel.ClanChannel;
import io.server.content.clanchannel.channel.ClanChannelHandler;
import io.server.content.clanchannel.content.ClanViewer;
import io.server.content.combat.Skulling;
import io.server.content.dailyTasks.DailyTask;
import io.server.content.dialogue.ChatBoxItemDialogue;
import io.server.content.dialogue.Dialogue;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.OptionDialogue;
import io.server.content.dialogue.impl.GloryTeleport;
import io.server.content.donators.Donation;
import io.server.content.emote.EmoteUnlockable;
import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.LogInEvent;
import io.server.content.hiscores.PlayerHiscores;
import io.server.content.masterminer.AdventureGUI;
import io.server.content.masterminer.MasterMinerData;
import io.server.content.masterminer.MasterMinerGUI;
import io.server.content.masterminer.MasterMinerTaskHandler;
import io.server.content.mysterybox2.MysteryBoxManager;
import io.server.content.pet.PetData;
import io.server.content.pet.Pets;
import io.server.content.prestige.Prestige;
import io.server.content.quest.QuestManager;
import io.server.content.skill.impl.construction.House;
import io.server.content.skill.impl.farming.Farming;
import io.server.content.skill.impl.hunter.trap.impl.BirdSnare;
import io.server.content.skill.impl.hunter.trap.impl.BoxTrap;
import io.server.content.skill.impl.magic.RunePouch;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.magic.spell.SpellCasting;
import io.server.content.skill.impl.runecrafting.RunecraftPouch;
import io.server.content.skill.impl.slayer.Slayer;
import io.server.content.store.StoreItem;
import io.server.content.store.currency.CurrencyType;
import io.server.content.store.impl.PersonalStore;
import io.server.content.teleport.Teleport;
import io.server.content.tittle.PlayerTitle;
import io.server.content.upgrading.UpgradeData;
import io.server.content.worldevent.WorldEvent;
import io.server.game.event.impl.MovementEvent;
import io.server.game.plugin.PluginManager;
import io.server.game.task.impl.TeleblockTask;
import io.server.game.world.World;
import io.server.game.world.entity.EntityType;
import io.server.game.world.entity.combat.Combat;
import io.server.game.world.entity.combat.CombatConstants;
import io.server.game.world.entity.combat.effect.AntifireDetails;
import io.server.game.world.entity.combat.magic.CombatSpell;
import io.server.game.world.entity.combat.ranged.RangedAmmunition;
import io.server.game.world.entity.combat.ranged.RangedWeaponDefinition;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.player.special.CombatSpecial;
import io.server.game.world.entity.combat.weapon.WeaponInterface;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.Viewport;
import io.server.game.world.entity.mob.movement.waypoint.PickupWaypoint;
import io.server.game.world.entity.mob.movement.waypoint.Waypoint;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.dropchance.DropChanceHandler;
import io.server.game.world.entity.mob.player.appearance.Appearance;
import io.server.game.world.entity.mob.player.exchange.ExchangeSessionManager;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.game.world.entity.mob.player.relations.ChatMessage;
import io.server.game.world.entity.mob.player.relations.PlayerRelation;
import io.server.game.world.entity.mob.player.requests.RequestManager;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.mob.prayer.PrayerBook;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.bank.Bank;
import io.server.game.world.items.containers.bank.BankPin;
import io.server.game.world.items.containers.bank.BankVault;
import io.server.game.world.items.containers.bank.DonatorDeposit;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.items.containers.impl.LootingBag;
import io.server.game.world.items.containers.impl.LostUntradeables;
import io.server.game.world.items.containers.inventory.Inventory;
import io.server.game.world.items.containers.pricechecker.PriceChecker;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.game.world.region.RegionManager;
import io.server.game.world.region.dynamic.DynamicRegion;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.out.SendCameraReset;
import io.server.net.packet.out.SendCompIDRequest;
import io.server.net.packet.out.SendExpCounter;
import io.server.net.packet.out.SendLogout;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendMultiIcon;
import io.server.net.packet.out.SendPlayerDetails;
import io.server.net.packet.out.SendPlayerOption;
import io.server.net.packet.out.SendRunEnergy;
import io.server.net.packet.out.SendSpecialAmount;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendWidget;
import io.server.net.session.GameSession;
import io.server.util.MessageColor;
import io.server.util.MutableNumber;
import io.server.util.Stopwatch;
import io.server.util.Utility;

/**
 * This class represents a character controlled by a player.
 *
 * 
 * @author Michael | Chex | Adam_#6723
 */
public class Player extends Mob {

	/**
	 * Changes the NPC that's being displayed on the floating teleport button
	 * 
	 * @param npcId - The identifier for the NPC to display
	 */
	public void sendTeleportButtonNpc(int npcId) {
		send(new SendString("" + npcId, 45615));
	}
	
	private String compID;
	
	public String getCompID() { return compID; }
	
	public void setCompID(String ID) { compID = ID; }
	
	public final int random = Utility.random(100, 500);
	
	public int currentCut;
	
	public int currentCaught;
	
	public int currentMined;
	
	public DailyTask currentTask;
	
	public void setDailyTask(DailyTask task) { this.currentTask = task; }
	
	public DailyTask getDailyTask() { return currentTask; }
	
	public String ironmanGroupLeader;
	
	public List<String> ironManGroup = new ArrayList<String>(3);
	
	public List<String> getIronmanGroup() {
		return ironManGroup;
	}
	
	public void linkIronManGroup(boolean login) {
		//TODO
		if (!this.hasIronmanGroup())
			return;
		
		String name = this.ironmanGroupLeader;
		
		Player owner = World.getPlayerByName(name);
		
		if (owner == null)
			owner = PlayerSerializer.loadPlayer(name);
	
		if (login)
		    owner.ironManGroup.add(this.getUsername());
		else
			owner.ironManGroup.remove(this.getUsername());
		
		this.ironManGroup = owner.ironManGroup;
		
		this.sendIronmanGroupMessage("(Ironman Group): "+this.getUsername()+" has just "+(login ? "logged in" : "logged out"));
	}
	
	public void sendIronmanGroupMessage(String msg) {
		
		Player iron = null;
		
		for (String name : ironManGroup) {
			
			if (name == null)
			    continue;
			
			iron = World.getPlayerByName(name);
			
			if (iron != null)
			    iron.sendMessage(msg);
		}
	}
	
	public boolean hasIronmanGroup() {
		return this.ironmanGroupLeader != null;
	}
	
	public PersonalStore personalStore;
	
	public void setPersonalStore(PersonalStore store) {
		this.personalStore = store;
	}
	
	public StoreItem[] personalStoreTempItems;
	
	public long personalStoreTempEarnings;
	
	public String registeredMac, lastMac;

	/**
	 * Will make the floating teleport button appear on the player's screen
	 */
	public void sendTeleportButton() {
		send(new SendString("1", 45600));
	}

	/**
	 * Will hide the floating teleport button on the player's screen
	 */
	public void hideTeleportButton() {
		send(new SendString("0", 45600));
	}

	public void hideTeleportButton1() {
		send(new SendString("0", 46600));
	}

	public void hideTeleportButton2() {
		send(new SendString("0", 47600));
	}

	public void hideTeleportButton3() {
		send(new SendString("0", 48600));
	}


	/**
	 * Killstreak
	 */
	public int killStreak = 0, killCount = 0, deathCount = 0;
	
	public int getKillCount() {
		return killCount;
	}

	
	public int getDeathCount() {
		return deathCount;
	}

	public int getKillStreak() {
		return killStreak;
	}

	private boolean upgradeSession;
	
	public boolean getUpgradeSession() {
		return upgradeSession;
	}
	
	public void setUpgradeInSesson(boolean upgradeSession) {
		this.upgradeSession = upgradeSession;
	}
	
	private UpgradeData upgradeSelected;
	
	public UpgradeData getUpgradeSelected() {
		return upgradeSelected;
	}

	public void setUpgradeSelected(UpgradeData upgradeSelected) {
		this.upgradeSelected = upgradeSelected;
	}


	private static final Logger logger = LogManager.getLogger();
	private int memberId = -1;
	public final Viewport viewport = new Viewport(this);
	public boolean debug;
	public Npc pet;
	private CombatSpell autocast;
	private CombatSpell singleCast;
	public Appearance appearance = Config.DEFAULT_APPEARANCE;
	public Stopwatch rejuvenation = Stopwatch.start();
	
	public PlayerRight right = PlayerRight.PLAYER;
	
	public void setRight(PlayerRight right) {
		this.right = right;
	}


	public PlayerTitle playerTitle = PlayerTitle.empty();
	public Spellbook spellbook = Spellbook.MODERN;
	public ChatBoxItemDialogue chatBoxItemDialogue;
	private Optional<ChatMessage> chatMessage = Optional.empty();
	public PrayerBook quickPrayers = new PrayerBook();
	public HashSet<Prayer> unlockedPrayers = new HashSet<>();
	public RangedWeaponDefinition rangedDefinition;
	public RangedAmmunition rangedAmmo;
	private AntifireDetails antifireDetails;
	private WeaponInterface weapon = WeaponInterface.UNARMED;
	private CombatSpecial combatSpecial;
	public int[] achievedSkills = new int[7];
	public double[] achievedExp = new double[7];
	public Optional<Dialogue> dialogue = Optional.empty();
	public Optional<OptionDialogue> optionDialogue = Optional.empty();
    public final MysteryBoxManager mysteryBox = new MysteryBoxManager(this);
	public Optional<Consumer<String>> enterInputListener = Optional.empty();
	public boolean[] barrowKills = new boolean[BrotherData.values().length];
	public final PlayerRelation relations = new PlayerRelation(this);
	public final Donation donation = new Donation(this);
	public final LostUntradeables lostUntradeables = new LostUntradeables(this);
	public final Gambling gambling = new Gambling(this);
	public BrotherData hiddenBrother;
	public int barrowsKillCount;
	
	public int getbarrowsKillCount() {
		return barrowsKillCount;
	}
	
	public void setbarrowsKillCount(int barrowsKillCount) {
		this.barrowsKillCount = barrowsKillCount;
	}
	public int sequence;
	public int playTime;
	public int sessionPlayTime;
	public int shop;
	public int headIcon;
	public int valueIcon = -1;
	public int pestPoints;
	public int pkPoints;
	public BattleRealmNode battleRealmNode;
	public long staminaExpireTime;
	public int zombiePoints;
	public int bloodPoints;
	public int getpkPoints() {
		return pkPoints;
	}

	public void setpkPoints(int pkPoints) {
		this.pkPoints = pkPoints;
	}

	public int skillingPoints;
	public int bossPoints;
	
	public int refferalpoint;
	
	public int getReferralPoints() {
		return refferalpoint;
	}

	public void setRefferalPoints(int refferalpoint) {
		this.refferalpoint = refferalpoint;
	}
	public int getRaidPoints() {
		return raidpoints;
	}
	public int raidpoints;
	
	public int triviaPoints;
	
	public int allvsonepoint;

	public int getAllVsOnePoints() {
		return allvsonepoint;
	}

	public void setAllVsOnePoints(int allvsonepoint) {
		this.allvsonepoint = allvsonepoint;
	}

	/**
	 * generates a random location for the player within the last man standing
	 * lobby.
	 **/
	static Random rand = new Random();
	public static final Position DEFAULT_POSITION = new Position((2972 + rand.nextInt(1)), 2784 + rand.nextInt(1));
	public static final int SkillLevel = 0;

	public int getTriviaPoints() {
		return triviaPoints;
	}

	public void SetTriviaPoints(int triviaPoints) {
		this.triviaPoints = triviaPoints;
	}

	public int getBossPoints() {
		return bossPoints;
	}

	public void setBossPoints(int bossPoints) {
		this.bossPoints = bossPoints;
	}

	public int getpestPoints() {
		return pestPoints;
	}
	public int getzombiePoints() {
		return zombiePoints;
	}
	public int getbloodPoints() {
		return bloodPoints;
	}
	public void setpestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}
	
	public void setzombiePoints(int zombiePoints) {
		this.zombiePoints = zombiePoints;
	}
	
	public void setbloodPoints(int bloodPoints) {
		this.bloodPoints = bloodPoints;
	}

	public int getskillingPoints() {
		return skillingPoints;
	}

	public void setskillingPoints(int skillingPoints) {
		this.skillingPoints = skillingPoints;
	}

	public int getkolodionPoints() {
		return kolodionPoints;
	}

	public void setkolodionPoints(int kolodionPoints) {
		this.kolodionPoints = kolodionPoints;
	}

	public int getChimeraPoints() {
		return chimeraPoints;
	}

	public void setChimeraPoints(int chimeraPoints) {
		this.chimeraPoints = chimeraPoints;
	}

	/**
	 * LMS
	 **/
	public int lmsCoffer = 0;

	public int getLmsCoffer() {
		return lmsCoffer;
	}

	public void setLmsCoffer(int lmsCoffer) {
		this.lmsCoffer = lmsCoffer;
	}

	public int votePoints;
	public int totalVotes;
	public int totalRefferals;
	
	public int getTotalRefferals() {
		return totalRefferals;
	}
	public void setTotalRefferals(int totalRefferals) {
		this.totalRefferals = totalRefferals;
	}
	public int kolodionPoints;
	public int chimeraPoints;
	public int ringOfRecoil = 40;
	public int profileView;
	public int ROW = 5;
	public int wilderness;
	public int runEnergy;
	public int energyRate;
	public int royalty;
	public int skillmenu;
	public int royaltyLevel = 1;
	public int skillmenuLevel = 1;
	public int glovesTier;
	public int smallPouch;
	public int mediumPouch;
	public int largePouch;
	public int giantPouch;
	public final RunecraftPouch runecraftPouch = new RunecraftPouch(this);
	public double experienceRate = Config.COMBAT_MODIFICATION;
	public long usernameLong;
	public boolean idle;
	public boolean resting;
	public boolean newPlayer;
	public boolean needsStarter;
	public boolean venged;
	private boolean specialActivated;
	public boolean warriorGuidTask;
	public boolean isBot;
	private String username = "";
	private String password = "";
	public String uuid;
	public String lastHost;
	public String created = Utility.getDate();
	public String lastClan = "";
	public ClanChannel clanChannel;
	public String clan = "";
	public String clanTag = "";
	public String clanTagColor = "";
	public final AtomicBoolean saved = new AtomicBoolean(false);
	public Stopwatch yellDelay = Stopwatch.start();
	public Stopwatch godwarsDelay = Stopwatch.start();
	public Stopwatch pieceDelay = Stopwatch.start();
	public Stopwatch flowerDelay = Stopwatch.start();
	public Stopwatch buttonDelay = Stopwatch.start();
	public Stopwatch itemDelay = Stopwatch.start();
	public Stopwatch foodDelay = Stopwatch.start();
	public Stopwatch RejuvDelay = Stopwatch.start();
	public Stopwatch boxClick = Stopwatch.start();
	public Stopwatch takeobj = Stopwatch.start();
	public Stopwatch revstele = Stopwatch.start();
	public Stopwatch takeAntiFireshieldDelay = Stopwatch.start();
	public Stopwatch takeHammersDelay = Stopwatch.start();
	public Stopwatch takeLogsDelay = Stopwatch.start();
	public Stopwatch karambwanDelay = Stopwatch.start();
	public Stopwatch potionDelay = Stopwatch.start();
	public Stopwatch restoreDelay = Stopwatch.start();
	public Stopwatch diceDelay = Stopwatch.start();
	public Stopwatch BankerPetDelay = Stopwatch.start();
	public Stopwatch aggressionTimer = Stopwatch.start();
	public Stopwatch databaseRequest = Stopwatch.start();
	public Stopwatch referaltime = Stopwatch.start();

	public Set<PetData> petInsurance = new ArraySet<>();
	public Set<PetData> lostPets = new ArraySet<>();
	public final ClanViewer clanViewer = new ClanViewer(this);
	public final PlayerRecord gameRecord = new PlayerRecord(this);
	public final ExchangeSessionManager exchangeSession = new ExchangeSessionManager(this);
	public final PlayerAssistant playerAssistant = new PlayerAssistant(this);
	public final InterfaceManager interfaceManager = new InterfaceManager(this);
	public final RequestManager requestManager = new RequestManager(this);
	public final Settings settings = new Settings(this);
	private final AccountSecurity security = new AccountSecurity(this);
	public final Inventory inventory = new Inventory(this);
	public final Bank bank = new Bank(this);
	public final BankVault bankVault = new BankVault(this);
	public final BankPin bankPin = new BankPin(this);
	public final RunePouch runePouch = new RunePouch(this);
	public final LootingBag lootingBag = new LootingBag(this);
	public final Equipment equipment = new Equipment(this);
	public final Prestige prestige = new Prestige(this);
	public final PriceChecker priceChecker = new PriceChecker(this);
	public final DonatorDeposit donatorDeposit = new DonatorDeposit(this);
	public final Map<Integer, PersonalStore> viewing_shops = new HashMap<>();
	public DialogueFactory dialogueFactory = new DialogueFactory(this);
	public final House house = new House(this);
	public Slayer slayer = new Slayer(this);
	public Skulling skulling = new Skulling(this);
	public QuestManager quest = new QuestManager(this);
	public SpellCasting spellCasting = new SpellCasting(this);
	public Combat<Player> combat = new Combat<>(this);
	public final ActivityLogger activityLogger = new ActivityLogger(this);
	private final MutableNumber poisonImmunity = new MutableNumber();
	private final MutableNumber venomImmunity = new MutableNumber();
	private final MutableNumber specialPercentage = new MutableNumber();
	public Deque<String> lastKilled = new ArrayDeque<>(3);
	public List<EmoteUnlockable> emoteUnlockable = new LinkedList<>();
	public List<Teleport> favoriteTeleport = new ArrayList<>();
	public final Set<String> hostList = new HashSet<>();
	public MasterMinerData masterMinerData = new MasterMinerData();
	public MasterMinerTaskHandler masterMinerTask = new MasterMinerTaskHandler();
	public final MasterMinerGUI masterMiner = new MasterMinerGUI(this);
	public AdventureGUI adventure = new AdventureGUI(this);
	private Farming farming = new Farming(this);
	public Toolkit toolkit = new Toolkit(this);
	
	private DynamicRegion dynamicRegion;
	
	public DynamicRegion getDynamicRegion() {
		return dynamicRegion;
	}
	
	public void setDynamicRegion(DynamicRegion dynamicRegion) {
		this.dynamicRegion = dynamicRegion;
	}

	public HashMap<ActivityLog, Integer> loggedActivities = new HashMap<ActivityLog, Integer>(
			ActivityLog.values().length) {
		/**
				 * 
				 */
				private static final long serialVersionUID = 4523609128540628838L;

		{
			for (final ActivityLog achievement : ActivityLog.values())
				put(achievement, 0);
		}
	};

	public HashMap<AchievementKey, Integer> playerAchievements = new HashMap<AchievementKey, Integer>(
			AchievementKey.values().length) {
		/**
				 * 
				 */
				private static final long serialVersionUID = -2320938821896448958L;

		{
			for (final AchievementKey achievement : AchievementKey.values())
				put(achievement, 0);
		}
	};

	public float blowpipeScales;
	public Item blowpipeDarts;

	public int tridentSeasCharges;
	public int tridentSwampCharges;

	public int serpentineHelmCharges;
	public int tanzaniteHelmCharges;
	public int magmaHelmCharges;

	public long staffOfDeadSpecial;
	private Optional<GameSession> session = Optional.empty();
	public int dragonfireCharges;
	public long dragonfireUsed;

	public Player(String username) {
		super(Config.LUMBRIDGE, false);
		this.username = username;
		this.usernameLong = Utility.nameToLong(username);
	}
	
	public Player(String username, int unused) {
		super(Config.LUMBRIDGE, false);
	}

	public Player(String username, Position position) {
		super(Config.DEFAULT_POSITION);
		this.setPosition(position);
		this.setUsername(username);
		this.isBot = true;
	}

	@Override
	public void setPosition(Position position) {
//        Region.removeMobOnTile(width(), getPosition());
		super.setPosition(position);
//        Region.setMobOnTile(width(), position);
	}

	public void chat(ChatMessage chatMessage) {
		if (!chatMessage.isValid()) {
			return;
		}
		this.chatMessage = Optional.of(chatMessage);
		this.updateFlags.add(UpdateFlag.CHAT);
	}

	public void setUsername(String username) {
		this.username = username;
		this.usernameLong = Utility.nameToLong(username);
	}

	public void send(OutgoingPacket packet) {
		send(packet, false);
	}

	public void send(OutgoingPacket encoder, boolean queue) {
		if (isBot) {
			return;
		}
		encoder.execute(this, queue);
	}

	private void login() {
		positionChange = true;
		regionChange = true;

		onStep();

		skills.login();

		mobAnimation.reset();

		inventory.refresh();

		equipment.login();

		settings.login();

		relations.onLogin();

		sendInitialPackets();

		playerAssistant.login();

		security.login();

		getFarming().doConfig();

		/**
		 * Sends the teleport button for the hoverable NPC Event teleport system. Only
		 * call upon this method, when an event has scheduled, it is currently turned on
		 * for testing purposes, for now.
		 */

		// sendTeleportButton();

		// joinclan(Player);
		
		this.configureStore();
		
		linkIronManGroup(true);
	}

	public void configureStore() {
		this.setPersonalStore(new PersonalStore(this.getName(), Optional.empty(), this.right.getCrown(),
				this.getName() + "'s Store", "No caption set", CurrencyType.COINS));
		this.personalStore.loadData(this);
		
	}

	
	  public static void joinclan (Player player) {
	  
	  
	  ClanChannelHandler.connect(player, "Wr3ckedyou");

	 }
	 
	
	public void sendEventInfo() {
		
		for (WorldEvent event : World.getWorldEventHandler().getActiveEvents())
			sendMessage(Utility.highlightGreenText(event.getType().getName()) + " - Ends in: " + Utility.highlightText(event.getFormattedSeconds()) + "!");
		
	}
	
	private void sendInitialPackets() {
		playerAssistant.welcomeScreen();
		
		send(new SendRunEnergy());
		send(new SendPlayerDetails());
		send(new SendCameraReset());
		send(new SendExpCounter(skills.getExpCounter()));
		message(String.format("Welcome to RebelionX. ", Config.SERVER_NAME + ""));
		//message(String.format("There are currently %s players online.", World.getPlayerCount()));
		//message(String.format("@red@Player Tip -@bla@ Do ::guide for an awesome money making guide! Get rich quick!!"));

		sendEventInfo();
		/***
		 * personal store
		 */
		
		
		Toolkit.TOOLS.forEach(t -> toolkit.fill(t.getId()));
	}

	private final boolean canLogout() {
		if (getUpgradeSession()) {
			send(new SendMessage("You can not logout whilst in upgrade session"));
			return false;
		}
		
		if (getCombat().inCombat()) {
			send(new SendMessage("You can not logout whilst in combat!"));
			return false;
		}

		if (!getCombat().hasPassed(CombatConstants.COMBAT_LOGOUT_COOLDOWN)) {
			send(new SendMessage(
					"You must wait "
							+ TimeUnit.SECONDS.convert(CombatConstants.COMBAT_LOGOUT_COOLDOWN - combat.elapsedTime(),
									TimeUnit.MILLISECONDS)
							+ " seconds before you can logout as you were recently in combat."));
			return false;
		}

		if (!interfaceManager.isMainClear()) {
			send(new SendMessage("Please close what you are doing before logging out!"));
			return false;
		}

		return !Activity.evaluate(this, it -> !it.canLogout(this));
	}

	private int getRights() {
		// TODO Auto-generated method stub
		return 0;
	}

	private Object getSkill() {
		// TODO Auto-generated method stub
		return null;
	}

	public final Set<PlayerOption> contextMenus = new HashSet<>();

	public final void logout() {
		logout(false);
	}

	public final void logout(boolean force) {
		if (!canLogout() && !force) {
			return;
		}
		linkIronManGroup(false);
		send(new SendLogout());
		setVisible(false);
		World.queueLogout(this);
		
		boolean debugMessage = false;
		int[] playerXP = new int[skills.getSkills().length];
		for (int i = 0; i < skills.getSkills().length; i++) {
			playerXP[i] = skills.get(i).getRoundedExperience();
		}

		if (this.right.equals(PlayerRight.IRONMAN)) 
		com.everythingrs.hiscores.Hiscores.update("b3deigj8ibbchxz79osx20529oi0eum5sto3lqto1e6kjwz5mi4v4dq6y1rq5hk8a1anbdwjyvi"
				,  "Ironman Mode", this.getUsername(),
				right.ordinal(), playerXP, debugMessage);
		else if (this.right.equals(PlayerRight.ULTIMATE_IRONMAN)) 
		com.everythingrs.hiscores.Hiscores.update("b3deigj8ibbchxz79osx20529oi0eum5sto3lqto1e6kjwz5mi4v4dq6y1rq5hk8a1anbdwjyvi"
				,  "Ultimate Ironman Mode", this.getUsername(),
				right.ordinal(), playerXP, debugMessage);
		else if (this.right.equals(PlayerRight.HARDCORE_IRONMAN)) 
		com.everythingrs.hiscores.Hiscores.update("b3deigj8ibbchxz79osx20529oi0eum5sto3lqto1e6kjwz5mi4v4dq6y1rq5hk8a1anbdwjyvi"
				,  "Hardcore Ironman Mode", this.getUsername(),
				right.ordinal(), playerXP, debugMessage);
		else
			com.everythingrs.hiscores.Hiscores.update("b3deigj8ibbchxz79osx20529oi0eum5sto3lqto1e6kjwz5mi4v4dq6y1rq5hk8a1anbdwjyvi"
					,  "Normal Mode", this.getUsername(),
					right.ordinal(), playerXP, debugMessage);
	}

	public void loadRegion() {
		//World.getRegions();
		Region[] surrounding = RegionManager.getSurroundingRegions(getPosition());

		for (Region region : surrounding) {
			region.sendGroundItems(this);
			region.sendGameObjects(this);

			// Npc Face
			for (Npc npc : region.getNpcs(getHeight())) {
				if (!npc.getCombat().inCombat())
					npc.face(npc.faceDirection);
			}
		}

		Activity.forActivity(this, minigame -> minigame.onRegionChange(this));

		if (debug && PlayerRight.isDeveloper(this)) {
			send(new SendMessage("[REGION] Loaded new region.", MessageColor.DEVELOPER));
		}
	}

	public void pickup(Item item, Position position) {
		Waypoint waypoint = new PickupWaypoint(this, item, position);
		if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
			resetWaypoint();
			action.clearNonWalkableActions();
			movement.reset();
			getCombat().reset();
			World.schedule(cachedWaypoint = waypoint);
		}
	}

	public void forClan(Consumer<ClanChannel> action) {
		if (clanChannel != null)
			action.accept(clanChannel);
	}

	@Override
	public void register() {
		if (!isRegistered() && !World.getPlayers().contains(this)) {
			setRegistered(World.getPlayers().add(this));
			setPosition(getPosition());

			login();

			logger.info("[REGISTERED]: " + Utility.formatName(getName()) + " [" + lastHost + "]");
			EventDispatcher.execute(this, new LogInEvent());
		}
		getFarming().load();
		getFarming().doConfig();
		
		send(new SendCompIDRequest());
		
	}
	
	//logout

	@Override
	public void unregister() {
		if (!isRegistered()) {
			return;
		}

		if (!World.getPlayers().contains(this)) {
			return;
		}

		if (getUpgradeSession()) {
			return;
		}
		
		send(new SendLogout());
		Activity.forActivity(this, minigame -> minigame.onLogout(this));
		
		relations.updateLists(false);
		house.leave();
		getFarming().save();
		Pets.onLogout(this);
		ClanChannelHandler.disconnect(this, true);
		new PlayerHiscores(this).execute();
		World.cancelTask(this, true);
		World.getPlayers().remove((Player) destroy());
		logger.info(String.format("[UNREGISTERED]: %s [%s]", getName(), lastHost));
		
	}
	@Override
	public void addToRegion(Region region) {
		region.addPlayer(this);
		aggressionTimer.reset();
	}

	@Override
	public void removeFromRegion(Region region) {
		region.removePlayer(this);
	}

	@Override
	public void sequence() {
		if (!idle) {
			playTime++;
			sessionPlayTime++;
		}
		
		if (this.getDynamicRegion() != null && this.getDynamicRegion().getHandler() != null)
			this.getDynamicRegion().getHandler().process(this);
		
		action.sequence();
		playerAssistant.sequence();
		sequence++;
	}

	@Override
	public void onStep() {
		PluginManager.getDataBus().publish(this, new MovementEvent(getPlayer().getPosition().copy()));
		send(new SendMultiIcon(Area.inMulti(this) ? 1 : -1));

		if (RebelionXMode) {
			send(new SendPlayerOption(PlayerOption.ATTACK, true));
			send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
		}
		if (Area.inBattleRealm(this)) {
			send(new SendPlayerOption(PlayerOption.ATTACK, true));
			send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
		} else if (Area.inWilderness(this)) {
			int modY = getPosition().getY() > 6400 ? getPosition().getY() - 6400 : getPosition().getY();
			wilderness = (((modY - 3521) / 8) + 1);
			send(new SendString("Level " + wilderness, 23327));
			if (interfaceManager.getWalkable() != 23400) {
				interfaceManager.openWalkable(23400);
			}

			if (!this.RebelionXMode) {
				send(new SendPlayerOption(PlayerOption.ATTACK, true));
				send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
			}
			// duel arena lobby
		} else if (Area.inDuelArenaLobby(this)) {
			send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false));
			send(new SendPlayerOption(PlayerOption.ATTACK, false, true));

		} else if (Area.inDuelArena(this) || Area.inDuelObsticleArena(this)) {
			send(new SendPlayerOption(PlayerOption.ATTACK, true));
			send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
			if (interfaceManager.getWalkable() != 201)
				interfaceManager.openWalkable(201);

			// event arena
		} else if (Area.inEventArena(this)) {
			send(new SendPlayerOption(PlayerOption.ATTACK, true));
			send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
			send(new SendString("Fun PK", 23327));
			if (interfaceManager.getWalkable() != 23400)
				interfaceManager.openWalkable(23400);

			// donator
		} else if (Area.inDonatorZone(this) && !PlayerRight.isDonator(this)) {
			move(Config.DEFAULT_POSITION);
			send(new SendMessage("You're not suppose to be here! Hacker much??"));

			// clear
		} else if (!inActivity()) {
			if (!RebelionXMode) {
				send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
			}
			send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
			send(new SendPlayerOption(PlayerOption.FOLLOW, false));
			send(new SendPlayerOption(PlayerOption.TRADE_REQUEST, false));
			send(new SendPlayerOption(PlayerOption.VIEW_PROFILE, false));
			   if (interfaceManager.getWalkable() > 0) {
	                interfaceManager.openWalkable(-1);
	            }

			if (!interfaceManager.isClear())
				interfaceManager.close();
			if (wilderness > 0)
				wilderness = 0;
		}
	}

	@Override
	public Combat<Player> getCombat() {
		return combat;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CombatStrategy<Player> getStrategy() {
		return playerAssistant.getStrategy();
	}

	@Override
	public void appendDeath() {
		World.schedule(new PlayerDeath(this));
	}

	@Override
	public String getName() {
		return Utility.formatName(username);
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public boolean isValid() {
		return (isBot || session != null) && super.isValid();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || !(obj instanceof Player)) {
			return false;
		}

		Player other = (Player) obj;
		return Objects.equals(getIndex(), other.getIndex()) && Objects.equals(username, other.username)
				&& Objects.equals(password, other.password) && Objects.equals(isBot, other.isBot);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIndex(), username);
	}

	@Override
	public String toString() {
		return String.format("Player[index=%d member_id=%d username=%s pos=%s bot=%b]", getIndex(), getMemberId(),
				getUsername(), getPosition(), isBot);
	}

	public void message(String... messages) {
		for (String message : messages) {
			send(new SendMessage(message));
		}
	}

	public boolean isAutoRetaliate() {
		return settings.autoRetaliate;
	}

	public boolean isSpecialActivated() {
		return specialActivated;
	}

	public void setSpecialActivated(boolean activated) {
		this.specialActivated = activated;
	}

	public void setCombatSpecial(CombatSpecial special) {
		this.combatSpecial = special;
	}

	public boolean isSingleCast() {
		return singleCast != null;
	}

	CombatSpell getSingleCastSpell() {
		return singleCast;
	}

	public void setSingleCast(CombatSpell singleCast) {
		this.singleCast = singleCast;
	}

	public boolean isAutocast() {
		return autocast != null;
	}

	CombatSpell getAutocastSpell() {
		return autocast;
	}

	public void setAutocast(CombatSpell autocast) {
		this.autocast = autocast;
	}

	public MutableNumber getSpecialPercentage() {
		return specialPercentage;
	}

	public final AtomicInteger teleblockTimer = new AtomicInteger(0);
	
	public double storedPrayerPoints;
	public String lastIP;
	
	public Player partyLeader;

	public Player allForOnePartner;

	public long lastPartnerRequest;

	public BoxTrap[] boxes = new BoxTrap[5];

	public BirdSnare[] snares = new BirdSnare[5];
	
	/* GLOBAL-BM-4
	 * Raids upgrade
	 */
	
	public List<Player> raidPartners = new ArrayList<Player>();
	
	public void addRaidPartner(Player player) {
		this.raidPartners.add(player);
	}
	
	public void removeRaidPartner(Player player) {
		this.raidPartners.remove(player);
	}
	
	public List<Player> getRaidPartners() { return this.raidPartners; }
	
	public Player getAllForOnePartner() {
		return allForOnePartner;
	}
	
	public void setAllForOnePartner(Player partner) {
		this.allForOnePartner = partner;
	}
	
	public boolean hasAllForOnePartner() {
		return allForOnePartner != null;
	}
	
	public void setAllForOnePartner(Player player, Player other) {
		player.partyLeader = player;
		other.partyLeader = player;
		player.setAllForOnePartner(other);
		other.setAllForOnePartner(player);
	}
	
	public void sendPartnerMessage(Player player, Player other, String message) {
		if (player.allForOnePartner == null || message == null)
			return;
		
		player.sendMessage(message);
		other.sendMessage(message);
		
	}
	
	public void sendPartyMessage(String message, boolean inclusive) {
		
		if (inclusive)
			sendMessage(message);
		
		for (Player p : raidPartners)
			p.sendMessage(message);
		
	}
	
	/**
	 * 
	 * @param player
	 * @param other
	 * @param force
	 * 
	 * Forces both parties to an instance.
	 * 
	 */
	public void createDuoInstance(Player player, Player other) {
		if (player.allForOnePartner == null) {
			player.message("You do not have a partner, therefore we could not create the instance!");
			return;
		}
		player.playerAssistant.instance();
		other.playerAssistant.instance();
	}

	public void teleblock(int time) {
		if (time <= 0 || (teleblockTimer.get() > 0)) {
			return;
		}

		teleblockTimer.set(time);
		send(new SendMessage("A teleblock spell has been casted on you!"));
		send(new SendWidget(SendWidget.WidgetType.TELEBLOCK, (int) ((double) teleblockTimer.get() / 1000D * 600D)));
		World.schedule(new TeleblockTask(this));
	}

	public boolean isTeleblocked() {
		return teleblockTimer.get() > 0;
	}

	public CombatSpecial getCombatSpecial() {
		return combatSpecial;
	}

	public WeaponInterface getWeapon() {
		return weapon;
	}

	public void setWeapon(WeaponInterface weapon) {
		this.weapon = weapon;
	}

	public Optional<AntifireDetails> getAntifireDetails() {
		return Optional.ofNullable(antifireDetails);
	}

	public void setAntifireDetail(AntifireDetails antifireDetails) {
		this.antifireDetails = antifireDetails;
	}

	public final MutableNumber getPoisonImmunity() {
		return poisonImmunity;
	}

	public final MutableNumber getVenomImmunity() {
		return venomImmunity;
	}

	public Optional<ChatMessage> getChatMessage() {
		return chatMessage;
	}

	public String getUsername() {
		return username;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberId() {
		return memberId;
	}

	public Optional<GameSession> getSession() {
		return session;
	}

	public void setSession(GameSession session) {
		this.session = Optional.of(session);
		this.lastHost = session.getHost();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public Object getPreviousTeleports() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPreviousTeleports(int[] fix) {
		// TODO Auto-generated method stub

	}

	public Object getPacketSender() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPreviousTeleportsIndex(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getTeleportInterfaceData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTeleportInterfaceData(Object object) {
		// TODO Auto-generated method stub

	}

	public Farming getFarming() {
		return farming;
	}
	
	public void sendMessage(String string) {
		this.send(new SendMessage(string));
	}
	
	public void handleGloryTeleport(Player p, int itemId) {
		
			if (itemId == 1704) {
				this.sendMessage("You have ran out of charges!");
				return;
			}
			this.dialogueFactory.sendDialogue(new GloryTeleport(p, itemId, false));
			return;
	}
	
	
	public int getAwardedBossPoints() {
		if (PlayerRight.isKing(this)) 
			return 4;
		else if (PlayerRight.isExtreme(this) || PlayerRight.isElite(this)) 
			return 3;
		else if (PlayerRight.isDonator(this) || PlayerRight.isSuper(this))
			return 2;
         return 1;
	}

	public void sendNewPlayerVariables() {
		this.runEnergy = 100;
		this.send(new SendRunEnergy());
		CombatSpecial.restore(this, 100);
		this.send(new SendSpecialAmount());
		
	}

	public void add(int i) {
		// TODO Auto-generated method stub
		
	}

	public void setRaidPoints(int i) {
		// TODO Auto-generated method stub
		
	}

}
