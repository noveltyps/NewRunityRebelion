package io.server.game.world.entity.mob.player.persist;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tritonus.share.ArraySet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

import io.server.content.ActivityLog;
import io.server.content.achievement.AchievementKey;
import io.server.content.activity.impl.barrows.BrotherData;
import io.server.content.activity.impl.duelarena.DuelRule;
import io.server.content.clanchannel.ClanRepository;
import io.server.content.clanchannel.channel.ClanChannel;
import io.server.content.clanchannel.content.ClanMemberComporator;
import io.server.content.emote.EmoteUnlockable;
import io.server.content.masterminer.MasterMinerTaskHandler;
import io.server.content.masterminer.MobData;
import io.server.content.pet.PetData;
import io.server.content.prestige.PrestigePerk;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.slayer.SlayerTask;
import io.server.content.skill.impl.slayer.SlayerUnlockable;
import io.server.content.teleport.Teleport;
import io.server.content.tittle.PlayerTitle;
import io.server.game.service.HighscoreService;
import io.server.game.service.PostgreService;
import io.server.game.world.entity.combat.attack.FightType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.appearance.Appearance;
import io.server.game.world.entity.mob.player.relations.PrivacyChatMode;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.mob.prayer.PrayerBook;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.position.Position;
import io.server.net.codec.login.LoginResponse;
import io.server.util.GsonUtils;
import io.server.util.Utility;

public final class PlayerPersistDB implements PlayerPersistable {

	private static final Logger logger = LogManager.getLogger();

	private static final Gson GSON = GsonUtils.JSON_ALLOW_NULL;
	private static final PlayerJSONProperty[] PROPERTIES = {

			new PlayerJSONProperty("username") {
				@Override
				void read(Player player, JsonElement property) {
					player.setUsername(property.getAsString());
				}

				@Override
				Object write(Player player) {
					return player.getUsername();
				}
			},

			new PlayerJSONProperty("skilling-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.skillingPoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.skillingPoints;
				}
			},

			new PlayerJSONProperty("kill-streak") {
				@Override
				void read(Player player, JsonElement property) {
					player.killStreak = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.killStreak;
				}
			},
			
			new PlayerJSONProperty("kill-count") {
				@Override
				void read(Player player, JsonElement property) {
					player.killCount = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.killCount;
				}
			},
			
			new PlayerJSONProperty("death-count") {
				@Override
				void read(Player player, JsonElement property) {
					player.deathCount = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.deathCount;
				}
			},
			
			new PlayerJSONProperty("boss-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.bossPoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.bossPoints;
				}
			},
			new PlayerJSONProperty("trivia-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.triviaPoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.triviaPoints;
				}
			},

			new PlayerJSONProperty("password") {
				@Override
				void read(Player player, JsonElement property) {
					player.setPassword(property.getAsString());
				}

				@Override
				Object write(Player player) {
					return player.getPassword();
				}
			},

			new PlayerJSONProperty("player-rights") {
				@Override
				void read(Player player, JsonElement property) {
					player.right = GSON.fromJson(property, PlayerRight.class);
				}

				@Override
				Object write(Player player) {
					return player.right;
				}
			},

			new PlayerJSONProperty("position") {
				@Override
				void read(Player player, JsonElement property) {
					final Position position = GSON.fromJson(property, Position.class);
					final int height = position.getHeight() < 0 ? 0 : position.getHeight() % 4;
					player.setPosition(Position.create(position.getX(), position.getY(), height));
				}

				@Override
				Object write(Player player) {
					return player.getPosition();
				}
			},

			new PlayerJSONProperty("created") {
				@Override
				void read(Player player, JsonElement property) {
					player.created = property.getAsString();
				}

				@Override
				Object write(Player player) {
					if (player.created == null)
						return Utility.getDate();
					return player.created;
				}
			},

			new PlayerJSONProperty("play-time") {
				@Override
				void read(Player player, JsonElement property) {
					player.playTime = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.playTime;
				}
			},

			new PlayerJSONProperty("money-spent") {
				@Override
				void read(Player player, JsonElement property) {
					player.donation.setSpent(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.donation.getSpent();
				}
			},

			new PlayerJSONProperty("donation-credits") {
				@Override
				void read(Player player, JsonElement property) {
					player.donation.setCredits(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.donation.getCredits();
				}
			},

			new PlayerJSONProperty("pest-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.pestPoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.pestPoints;
				}
			},

			new PlayerJSONProperty("player-title") {
				@Override
				void read(Player player, JsonElement property) {
					player.playerTitle = GSON.fromJson(property, PlayerTitle.class);
				}

				@Override
				Object write(Player player) {
					return player.playerTitle;
				}
			},

			new PlayerJSONProperty("clan") {
				@Override
				void read(Player player, JsonElement property) {
					ClanChannel channel = ClanRepository.getChannel(property.getAsString());
					if (channel != null) {
						player.clanChannel = channel;
					}
				}

				@Override
				Object write(Player player) {
					return player.clanChannel == null ? "" : player.clanChannel.getOwner();
				}
			},

			new PlayerJSONProperty("last-clan") {
				@Override
				void read(Player player, JsonElement property) {
					player.lastClan = property.getAsString();
				}

				@Override
				Object write(Player player) {
					return player.lastClan;
				}
			},

			new PlayerJSONProperty("clan-tag") {
				@Override
				void read(Player player, JsonElement property) {
					player.clanTag = property.getAsString();
				}

				@Override
				Object write(Player player) {
					return player.clanTag;
				}
			},

			new PlayerJSONProperty("clan-tag-color") {
				@Override
				void read(Player player, JsonElement property) {
					player.clanTagColor = property.getAsString();
				}

				@Override
				Object write(Player player) {
					return player.clanTagColor;
				}
			},

			new PlayerJSONProperty("clan-sort-type") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.clanMemberComporator = ClanMemberComporator.valueOf(property.getAsString());
				}

				@Override
				Object write(Player player) {
					return player.settings.clanMemberComporator.name();
				}
			},

			new PlayerJSONProperty("bank-placeholder") {
				@Override
				void read(Player player, JsonElement property) {
					player.bank.placeHolder = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.bank.placeHolder;
				}
			},

			new PlayerJSONProperty("bank-pin") {
				@Override
				void read(Player player, JsonElement property) {
					player.bankPin.pin = property.getAsString();
				}

				@Override
				Object write(Player player) {
					return player.bankPin.pin;
				}
			},

			new PlayerJSONProperty("bank-vault") {
				@Override
				void read(Player player, JsonElement property) {
					player.bankVault.container = property.getAsLong();
				}

				@Override
				Object write(Player player) {
					return player.bankVault.container;
				}
			},

			new PlayerJSONProperty("royalty") {
				@Override
				void read(Player player, JsonElement property) {
					player.royalty = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.royalty;
				}
			},

			new PlayerJSONProperty("royalty-level") {
				@Override
				void read(Player player, JsonElement property) {
					player.royaltyLevel = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.royaltyLevel;
				}
			},

			new PlayerJSONProperty("pet") {
				@Override
				void read(Player player, JsonElement property) {
					int pet = property.getAsInt();
					if (pet != -1) {
						player.pet = new Npc(pet, player.getPosition());
					}
				}

				@Override
				Object write(Player player) {
					return player.pet == null ? -1 : player.pet.id;
				}
			},

			new PlayerJSONProperty("pet-insurnce") {
				@Override
				void read(Player player, JsonElement property) {
					player.petInsurance = GSON.fromJson(property, new TypeToken<ArraySet<PetData>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.petInsurance;
				}
			},

			new PlayerJSONProperty("lost-pets") {
				@Override
				void read(Player player, JsonElement property) {
					player.lostPets = GSON.fromJson(property, new TypeToken<ArraySet<PetData>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.lostPets;
				}
			},

			new PlayerJSONProperty("new-player") {
				@Override
				void read(Player player, JsonElement property) {
					player.newPlayer = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.newPlayer;
				}
			},

			new PlayerJSONProperty("needs-starter") {
				@Override
				void read(Player player, JsonElement property) {
					player.needsStarter = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.needsStarter;
				}
			},

			new PlayerJSONProperty("player-index") {
				@Override
				void read(Player player, JsonElement property) {
					player.setIndex(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.getIndex();
				}
			},

			new PlayerJSONProperty("small-pouch") {
				@Override
				void read(Player player, JsonElement property) {
					player.smallPouch = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.smallPouch;
				}
			},

			new PlayerJSONProperty("medium-pouch") {
				@Override
				void read(Player player, JsonElement property) {
					player.mediumPouch = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.mediumPouch;
				}
			},

			new PlayerJSONProperty("large-pouch") {
				@Override
				void read(Player player, JsonElement property) {
					player.largePouch = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.largePouch;
				}
			},

			new PlayerJSONProperty("giant-pouch") {
				@Override
				void read(Player player, JsonElement property) {
					player.giantPouch = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.giantPouch;
				}
			},

			new PlayerJSONProperty("rune-pouch") {
				@Override
				void read(Player player, JsonElement property) {
					player.runePouch.runes = GSON.fromJson(property, new TypeToken<LinkedList<Item>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.runePouch.runes;
				}
			},

			new PlayerJSONProperty("run-toggled") {
				@Override
				void read(Player player, JsonElement property) {
					player.movement.setRunningToggled(property.getAsBoolean());
				}

				@Override
				Object write(Player player) {
					return player.movement.isRunningToggled();
				}
			},

			new PlayerJSONProperty("run-energy") {
				@Override
				void read(Player player, JsonElement property) {
					player.runEnergy = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.runEnergy;
				}
			},

			new PlayerJSONProperty("energy-rate") {
				@Override
				void read(Player player, JsonElement property) {
					player.energyRate = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.energyRate;
				}
			},

			new PlayerJSONProperty("special-amount") {
				@Override
				void read(Player player, JsonElement property) {
					player.getSpecialPercentage().set(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.getSpecialPercentage().get();
				}
			},

			new PlayerJSONProperty("recoil-charge") {
				@Override
				void read(Player player, JsonElement property) {
					player.ringOfRecoil = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.ringOfRecoil;
				}
			},

			new PlayerJSONProperty("dragonfire-shield-charge") {
				@Override
				void read(Player player, JsonElement property) {
					player.dragonfireCharges = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.dragonfireCharges;
				}
			},

			new PlayerJSONProperty("dragonfire-shield-cooldown") {
				@Override
				void read(Player player, JsonElement property) {
					player.dragonfireUsed = property.getAsLong();
				}

				@Override
				Object write(Player player) {
					return player.dragonfireUsed;
				}
			},

			new PlayerJSONProperty("blowpipe-darts") {
				@Override
				void read(Player player, JsonElement property) {
					player.blowpipeDarts = GSON.fromJson(property, Item.class);
				}

				@Override
				Object write(Player player) {
					return player.blowpipeDarts;
				}
			},

			new PlayerJSONProperty("blowpipe-scales") {
				@Override
				void read(Player player, JsonElement property) {
					player.blowpipeScales = property.getAsFloat();
				}

				@Override
				Object write(Player player) {
					return player.blowpipeScales;
				}
			},

			new PlayerJSONProperty("serpentine-helm") {
				@Override
				void read(Player player, JsonElement property) {
					player.serpentineHelmCharges = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.serpentineHelmCharges;
				}
			},

			new PlayerJSONProperty("tanzanite-helm") {
				@Override
				void read(Player player, JsonElement property) {
					player.tanzaniteHelmCharges = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.tanzaniteHelmCharges;
				}
			},

			new PlayerJSONProperty("magma-helm") {
				@Override
				void read(Player player, JsonElement property) {
					player.magmaHelmCharges = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.magmaHelmCharges;
				}
			},

			new PlayerJSONProperty("trident-seas-charges") {
				@Override
				void read(Player player, JsonElement property) {
					player.tridentSeasCharges = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.tridentSeasCharges;
				}
			},

			new PlayerJSONProperty("trident-swamp-charges") {
				@Override
				void read(Player player, JsonElement property) {
					player.tridentSwampCharges = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.tridentSwampCharges;
				}
			},

			new PlayerJSONProperty("fight-type") {
				@Override
				void read(Player player, JsonElement property) {
					player.getCombat().setFightType(GSON.fromJson(property, FightType.class));
				}

				@Override
				Object write(Player player) {
					return player.getCombat().getFightType();
				}
			},

			new PlayerJSONProperty("client-width") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.clientWidth = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.settings.clientWidth;
				}
			},

			new PlayerJSONProperty("client-height") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.clientHeight = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.settings.clientHeight;
				}
			},

			new PlayerJSONProperty("spell-book") {
				@Override
				void read(Player player, JsonElement property) {
					player.spellbook = GSON.fromJson(property, Spellbook.class);
				}

				@Override
				Object write(Player player) {
					return player.spellbook;
				}
			},

			new PlayerJSONProperty("brightness") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.setBrightness(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.settings.brightness;
				}
			},

			new PlayerJSONProperty("zoom") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.setZoom(property.getAsInt(), true);
				}

				@Override
				Object write(Player player) {
					return player.settings.zoom;
				}
			},

			new PlayerJSONProperty("private-chat") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.privateChat = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.privateChat;
				}
			},

			new PlayerJSONProperty("chat-effects") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.chatEffects = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.chatEffects;
				}
			},

			new PlayerJSONProperty("accept-aid") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.acceptAid = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.acceptAid;
				}
			},

			new PlayerJSONProperty("mouse-clicks") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.mouse = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.mouse;
				}
			},

			new PlayerJSONProperty("auto-retaliate") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.autoRetaliate = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.autoRetaliate;
				}
			},

			new PlayerJSONProperty("profanity-filter") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.profanityFilter = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.profanityFilter;
				}
			},

			new PlayerJSONProperty("camera-movement") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.cameraMovement = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.cameraMovement;
				}
			},

			new PlayerJSONProperty("experience-lock") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.lockExperience = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.lockExperience;
				}
			},

			new PlayerJSONProperty("welcome-screen") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.welcomeScreen = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.welcomeScreen;
				}
			},

			new PlayerJSONProperty("trivia-bot") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.triviaBot = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.triviaBot;
				}
			},

			new PlayerJSONProperty("drop-notification") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.dropNotification = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.dropNotification;
				}
			},

			new PlayerJSONProperty("yell-notification") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.yell = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.yell;
				}
			},

			new PlayerJSONProperty("untradeable-notification") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.untradeableNotification = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.untradeableNotification;
				}
			},

			new PlayerJSONProperty("esc-close") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.ESC_CLOSE = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.ESC_CLOSE;
				}
			},

			new PlayerJSONProperty("gloves-tier") {
				@Override
				void read(Player player, JsonElement property) {
					player.glovesTier = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.glovesTier;
				}
			},

			new PlayerJSONProperty("experience-rate") {
				@Override
				void read(Player player, JsonElement property) {
					player.experienceRate = property.getAsDouble();
				}

				@Override
				Object write(Player player) {
					return player.experienceRate;
				}
			},

			new PlayerJSONProperty("experience-counter") {
				@Override
				void read(Player player, JsonElement property) {
					player.skills.experienceCounter = property.getAsDouble();
				}

				@Override
				Object write(Player player) {
					return player.skills.experienceCounter;
				}
			},

			new PlayerJSONProperty("hidden-brother") {
				@Override
				void read(Player player, JsonElement property) {
					player.hiddenBrother = GSON.fromJson(property, BrotherData.class);
				}

				@Override
				Object write(Player player) {
					return player.hiddenBrother;
				}
			},

			new PlayerJSONProperty("brothers-kill-count") {
				@Override
				void read(Player player, JsonElement property) {
					player.barrowsKillCount = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.barrowsKillCount;
				}
			},

			new PlayerJSONProperty("brothers-dead") {
				@Override
				void read(Player player, JsonElement property) {
					player.barrowKills = GSON.fromJson(property, boolean[].class);
				}

				@Override
				Object write(Player player) {
					return player.barrowKills;
				}
			},

			new PlayerJSONProperty("vote-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.votePoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.votePoints;
				}
			},

			new PlayerJSONProperty("total-votes") {
				@Override
				void read(Player player, JsonElement property) {
					player.totalVotes = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.totalVotes;
				}
			},

			new PlayerJSONProperty("pk-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.pkPoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.pkPoints;
				}
			},

			new PlayerJSONProperty("kolodion-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.kolodionPoints = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.kolodionPoints;
				}
			},

			new PlayerJSONProperty("poison-immunity") {
				@Override
				void read(Player player, JsonElement property) {
					player.getPoisonImmunity().set(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.getPoisonImmunity().get();
				}
			},

			new PlayerJSONProperty("venom-immunity") {
				@Override
				void read(Player player, JsonElement property) {
					player.getVenomImmunity().set(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.getVenomImmunity().get();
				}
			},

			new PlayerJSONProperty("skull-timer") {
				@Override
				void read(Player player, JsonElement property) {
					player.skulling.getSkullRemoveTask().setSkullTime(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.skulling.getSkullRemoveTask().getSkullTime();
				}
			},

			new PlayerJSONProperty("teleblock-timer") {
				@Override
				void read(Player player, JsonElement property) {
					player.teleblockTimer.set(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.teleblockTimer.get();
				}
			},

			new PlayerJSONProperty("prestige-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.prestige.setPrestigePoint(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.prestige.getPrestigePoint();
				}
			},

			new PlayerJSONProperty("prestige-total") {
				@Override
				void read(Player player, JsonElement property) {
					player.prestige.totalPrestige = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.prestige.totalPrestige;
				}
			},

			new PlayerJSONProperty("prestige-color") {
				@Override
				void read(Player player, JsonElement property) {
					player.settings.prestigeColors = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.settings.prestigeColors;
				}
			},

			new PlayerJSONProperty("prestiges") {
				@Override
				void read(Player player, JsonElement property) {
					player.prestige.prestige = GSON.fromJson(property, int[].class);
				}

				@Override
				Object write(Player player) {
					return player.prestige.prestige;
				}
			},

			new PlayerJSONProperty("active-perks") {
				@Override
				void read(Player player, JsonElement property) {
					player.prestige.activePerks = GSON.fromJson(property, new TypeToken<Set<PrestigePerk>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.prestige.activePerks;
				}
			},

			new PlayerJSONProperty("quest-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.quest.setPoints(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.quest.getPoints();
				}
			},

			new PlayerJSONProperty("quest-completed") {
				@Override
				void read(Player player, JsonElement property) {
					player.quest.setCompleted(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.quest.getCompleted();
				}
			},

			new PlayerJSONProperty("quest-stage") {
				@Override
				void read(Player player, JsonElement property) {
					int[] stages = GSON.fromJson(property, int[].class);
					System.arraycopy(stages, 0, player.quest.stage, 0, stages.length);
				}

				@Override
				Object write(Player player) {
					return player.quest.stage;
				}
			},

			new PlayerJSONProperty("achievement") {
				@Override
				void read(Player player, JsonElement property) {
					player.playerAchievements = GSON.fromJson(property,
							new TypeToken<HashMap<AchievementKey, Integer>>() {
							}.getType());
				}

				@Override
				Object write(Player player) {
					return GSON.toJsonTree(player.playerAchievements,
							new TypeToken<HashMap<AchievementKey, Integer>>() {
							}.getType());
				}
			},

			new PlayerJSONProperty("last-killed") {
				@Override
				void read(Player player, JsonElement property) {
					player.lastKilled = GSON.fromJson(property, new TypeToken<Deque<String>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.lastKilled;
				}
			},

			new PlayerJSONProperty("favorite-teleport") {
				@Override
				void read(Player player, JsonElement property) {
					player.favoriteTeleport = GSON.fromJson(property, new TypeToken<List<Teleport>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.favoriteTeleport;
				}
			},

			new PlayerJSONProperty("unlocked-emotes") {
				@Override
				void read(Player player, JsonElement property) {
					player.emoteUnlockable = GSON.fromJson(property, new TypeToken<List<EmoteUnlockable>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.emoteUnlockable;
				}
			},

			new PlayerJSONProperty("public-chat-mode") {
				@Override
				void read(Player player, JsonElement property) {
					player.relations.setPublicChatMode(
							PrivacyChatMode.valueOf(GSON.fromJson(property, String.class).toUpperCase()), false);
				}

				@Override
				Object write(Player player) {
					return player.relations.getPublicChatMode().name();
				}
			},

			new PlayerJSONProperty("private-chat-mode") {
				@Override
				void read(Player player, JsonElement property) {
					player.relations.setPrivateChatMode(
							PrivacyChatMode.valueOf(GSON.fromJson(property, String.class).toUpperCase()), true);
				}

				@Override
				Object write(Player player) {
					return player.relations.getPrivateChatMode().name();
				}
			},

			new PlayerJSONProperty("clan-chat-mode") {
				@Override
				void read(Player player, JsonElement property) {
					player.relations.setClanChatMode(
							PrivacyChatMode.valueOf(GSON.fromJson(property, String.class).toUpperCase()), false);
				}

				@Override
				Object write(Player player) {
					return player.relations.getClanChatMode().name();
				}
			},

			new PlayerJSONProperty("trade-chat-mode") {
				@Override
				void read(Player player, JsonElement property) {
					player.relations.setTradeChatMode(
							PrivacyChatMode.valueOf(GSON.fromJson(property, String.class).toUpperCase()), false);
				}

				@Override
				Object write(Player player) {
					return player.relations.getTradeChatMode().name();
				}
			},

			new PlayerJSONProperty("friend-list") {
				@Override
				void read(Player player, JsonElement property) {
					player.relations.getFriendList().addAll(GSON.fromJson(property, new TypeToken<List<Long>>() {
					}.getType()));
				}

				@Override
				Object write(Player player) {
					return player.relations.getFriendList();
				}
			},

			new PlayerJSONProperty("ignore-list") {
				@Override
				void read(Player player, JsonElement property) {
					player.relations.getIgnoreList().addAll(GSON.fromJson(property, new TypeToken<List<Long>>() {
					}.getType()));
				}

				@Override
				Object write(Player player) {
					return player.relations.getIgnoreList();
				}
			},

			new PlayerJSONProperty("appearance") {
				@Override
				void read(Player player, JsonElement property) {
					player.appearance = GSON.fromJson(property, Appearance.class);
				}

				@Override
				Object write(Player player) {
					return player.appearance;
				}
			},

			new PlayerJSONProperty("activity-logger") {
				@Override
				void read(Player player, JsonElement property) {
					player.loggedActivities = GSON.fromJson(property, new TypeToken<HashMap<ActivityLog, Integer>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return GSON.toJsonTree(player.loggedActivities, new TypeToken<HashMap<ActivityLog, Integer>>() {
					}.getType());
				}
			},

			new PlayerJSONProperty("quick-prayers") {
				@Override
				void read(Player player, JsonElement property) {
					player.quickPrayers = GSON.fromJson(property, PrayerBook.class);
				}

				@Override
				Object write(Player player) {
					return player.quickPrayers;
				}
			},

			new PlayerJSONProperty("locked-prayers") {
				@Override
				void read(Player player, JsonElement property) {
					player.unlockedPrayers = GSON.fromJson(property, new TypeToken<Set<Prayer>>() {
					}.getType());
				}

				@Override
				Object write(Player player) {
					return player.unlockedPrayers;
				}
			},

			new PlayerJSONProperty("slayer-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setPoints(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getPoints();
				}
			},

			new PlayerJSONProperty("slayer-task") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setTask(GSON.fromJson(property, SlayerTask.class));
				}

				@Override
				Object write(Player player) {
					return player.slayer.getTask();
				}
			},

			new PlayerJSONProperty("slayer-assigned") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setAssigned(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getAssigned();
				}
			},

			new PlayerJSONProperty("slayer-amount") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setAmount(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getAmount();
				}
			},

			new PlayerJSONProperty("slayer-total-assigned") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setTotalAssigned(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getTotalAssigned();
				}
			},

			new PlayerJSONProperty("slayer-total-completed") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setTotalCompleted(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getTotalCompleted();
				}
			},

			new PlayerJSONProperty("slayer-total-cancelled") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setTotalCancelled(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getTotalCancelled();
				}
			},

			new PlayerJSONProperty("slayer-total-points") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setTotalPoints(property.getAsInt());
				}

				@Override
				Object write(Player player) {
					return player.slayer.getTotalPoints();
				}
			},

			new PlayerJSONProperty("slayer-blocked") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setBlocked(GSON.fromJson(property, new TypeToken<List<SlayerTask>>() {
					}.getType()));
				}

				@Override
				Object write(Player player) {
					return player.slayer.getBlocked();
				}
			},

			new PlayerJSONProperty("slayer-unlocked") {
				@Override
				void read(Player player, JsonElement property) {
					player.slayer.setUnlocked(GSON.fromJson(property, new TypeToken<Set<SlayerUnlockable>>() {
					}.getType()));
				}

				@Override
				Object write(Player player) {
					return player.slayer.getUnlocked();
				}
			},

			new PlayerJSONProperty("achieved-skills") {
				@Override
				void read(Player player, JsonElement property) {
					player.achievedSkills = GSON.fromJson(property, int[].class);
				}

				@Override
				Object write(Player player) {
					return player.achievedSkills;
				}
			},

			new PlayerJSONProperty("achieved-exp") {
				@Override
				void read(Player player, JsonElement property) {
					player.achievedExp = GSON.fromJson(property, double[].class);
				}

				@Override
				Object write(Player player) {
					return player.achievedExp;
				}
			},

			new PlayerJSONProperty("tab-amounts") {
				@Override
				void read(Player player, JsonElement property) {
					player.bank.tabAmounts = GSON.fromJson(property, int[].class);
				}

				@Override
				Object write(Player player) {
					return player.bank.tabAmounts;
				}
			},

			new PlayerJSONProperty("looting-bag") {
				@Override
				void read(Player player, JsonElement property) {
					player.lootingBag.set(GSON.fromJson(property, Item[].class));
				}

				@Override
				Object write(Player player) {
					return player.lootingBag.getItems();
				}
			},

			new PlayerJSONProperty("lost-untradables") {
				@Override
				void read(Player player, JsonElement property) {
					player.lostUntradeables.set(GSON.fromJson(property, Item[].class));
				}

				@Override
				Object write(Player player) {
					return player.lostUntradeables.getItems();
				}
			},

			new PlayerJSONProperty("duel-rules") {
				@Override
				void read(Player player, JsonElement property) {
					if (property.isJsonNull()) {
						return;
					}
					Type ruleSet = new TypeToken<EnumSet<DuelRule>>() {
					}.getType();

					player.attributes.put("duel_rules", GSON.fromJson(property, ruleSet));
				}

				@Override
				Object write(Player player) {
					if (player.attributes.has("duel_rules")) {
						EnumSet<DuelRule> flags = player.attributes.get("duel_rules");
						return flags;
					}
					return null;
				}
			},

			new PlayerJSONProperty("gold-per-sec") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerData.goldPerSec = property.getAsLong();
				}

				@Override
				Object write(Player player) {
					return player.masterMinerData.goldPerSec;
				}
			},

			new PlayerJSONProperty("time-last-checked-ms") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerData.timeLastCheckedMs = property.getAsLong();
				}

				@Override
				Object write(Player player) {
					return player.masterMinerData.timeLastCheckedMs;
				}
			},

			new PlayerJSONProperty("total-gold") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerData.totalGold = property.getAsLong();
				}

				@Override
				Object write(Player player) {
					return player.masterMinerData.totalGold;
				}
			},

			new PlayerJSONProperty("mob-data") {
				@Override
				void read(Player player, JsonElement property) {
					String toParse = property.getAsString();
					String mob[] = toParse.split("<");
					for (int i = 0; i < 8; i++) {
						player.masterMinerData.mobData[i] = new MobData(mob[i]);
					}
				}

				@Override
				Object write(Player player) {
					String toWrite = "";
					for (int i = 0; i < 8; i++) {
						toWrite += player.masterMinerData.mobData[i] + "<";
					}
					return toWrite;
				}
			},

			new PlayerJSONProperty("amount-to-buy") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerData.amountToBuy = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.masterMinerData.amountToBuy;
				}
			},

			new PlayerJSONProperty("total-clicks") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerData.totalClicks = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.masterMinerData.totalClicks;
				}
			},

			new PlayerJSONProperty("prestige-level") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerData.prestigeLevel = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.masterMinerData.prestigeLevel;
				}
			},

			new PlayerJSONProperty("task-handler") {
				@Override
				void read(Player player, JsonElement property) {
					player.masterMinerTask = new MasterMinerTaskHandler(property.getAsString());
				}

				@Override
				Object write(Player player) {
					return player.masterMinerTask.toString();
				}
			},

			new PlayerJSONProperty("lms-coffer") {
				@Override
				void read(Player player, JsonElement property) {
					player.lmsCoffer = property.getAsInt();
				}

				@Override
				Object write(Player player) {
					return player.lmsCoffer;
				}
			},

			new PlayerJSONProperty("RebelionX-mode") {
				@Override
				void read(Player player, JsonElement property) {
					player.RebelionXMode = property.getAsBoolean();
				}

				@Override
				Object write(Player player) {
					return player.RebelionXMode;
				}
			} };
	
	private static final PlayerDBProperty[] DB_PROPERTIES = {

			new PlayerDBProperty("json_properties") {
				@Override
				void read(Player player, JdbcSession session) throws SQLException {
					session.sql("SELECT member_id, data FROM player.player WHERE member_id = ?")
							.set(player.getMemberId()).select((rset, stmt) -> {
								if (rset.next()) {
									player.setMemberId(rset.getInt("member_id"));

									final JsonParser parser = new JsonParser();

									final JsonObject obj = parser.parse(rset.getString("data")).getAsJsonObject();

									for (PlayerJSONProperty property : PROPERTIES) {
										if (obj.has(property.label)) {
											if (obj.get(property.label).isJsonNull()) {
												continue;
											}
											property.read(player, obj.get(property.label));
										}
									}

								}

								return null;

							});
				}

				@Override
				void write(Player player, JdbcSession session) throws SQLException {
					final JsonObject properties = new JsonObject();

					for (PlayerJSONProperty property : PROPERTIES) {
						properties.add(property.label, GSON.toJsonTree(property.write(player)));
					}

					session.sql(
							"INSERT INTO player.player(member_id, data) VALUES (?, ?::json) ON CONFLICT (member_id) DO UPDATE SET data = excluded.data, last_save = DEFAULT")
							.set(player.getMemberId()).set(GSON.toJson(properties)).execute();
				}
			},

			new PlayerDBProperty("host") {
				@Override
				void read(Player player, JdbcSession session) throws SQLException {
					session.sql("SELECT host, time FROM player.host where player_id = ? ORDER BY time LIMIT 1")
							.set(player.getMemberId()).select((rset, stmt) -> {
								if (rset.next()) {
									player.lastHost = rset.getString("host");
								}
								return null;
							});
				}

				@Override
				void write(Player player, JdbcSession session) throws SQLException {
					if (!player.getSession().isPresent()) {
						return;
					}
					session.sql("INSERT INTO player.host(host, player_id) VALUES (?, ?)")
							.set(player.getSession().get().getHost()).set(player.getMemberId()).execute();
				}
			},

			new PlayerDBProperty("skills") {
				@Override
				void read(Player player, JdbcSession session) throws SQLException {
					session.sql("SELECT id, current_level, xp, player_id FROM player.skill WHERE player_id = ?")
							.set(player.getMemberId()).select((rset, stmt) -> {

								final Skill[] skills = new Skill[Skill.SKILL_COUNT];

								while (rset.next()) {
									final int skillId = rset.getInt("id");
									skills[skillId] = new Skill(skillId, rset.getInt("current_level"),
											rset.getDouble("xp"));
								}

								player.skills.setSkills(skills);
								return null;
							});
				}

				@Override
				void write(Player player, JdbcSession session) throws SQLException {
					for (Skill skill : player.skills.getSkills()) {
						session.sql(
								"INSERT INTO player.skill(id, current_level, xp, player_id) VALUES (?, ?, ?::double precision, ?) ON CONFLICT (id, player_id) DO UPDATE SET current_level = excluded.current_level, xp = excluded.xp")
								.set(skill.getSkill()).set(skill.getLevel()).set(skill.getExperience())
								.set(player.getMemberId()).execute();
					}
				}
			},

			new PlayerDBProperty("items") {
				@Override
				void read(Player player, JdbcSession session) throws SQLException {

					// bank
					session.sql(
							"SELECT item_id, amount, slot FROM player.bank WHERE player_id = ? AND item_id IS NOT NULL ORDER BY slot")
							.set(player.getMemberId()).select((rset, stmt) -> {
								while (rset.next()) {
									Item item = new Item(rset.getInt("item_id"), rset.getInt("amount"));
									player.bank.add(item, rset.getInt("slot"));
								}
								return null;
							});

					// equipment
					session.sql("SELECT item_id, amount, slot FROM player.equipment WHERE player_id = ?")
							.set(player.getMemberId()).select((rset, stmt) -> {
								while (rset.next()) {
									player.equipment.set(rset.getInt("slot"),
											new Item(rset.getInt("item_id"), rset.getInt("amount")), false);
								}
								player.equipment.refresh();
								return null;
							});

					// inventory
					session.sql("SELECT item_id, amount, slot FROM player.inventory WHERE player_id = ?")
							.set(player.getMemberId()).select((rset, stmt) -> {
								while (rset.next()) {
									Item item = new Item(rset.getInt("item_id"), rset.getInt("amount"));
									player.inventory.add(item, rset.getInt("slot"));
								}
								return null;
							});

				}

				@Override
				void write(Player player, JdbcSession session) throws SQLException {
					// bank
					session.sql("DELETE FROM player.bank WHERE player_id = ?").set(player.getMemberId()).execute();

					for (int slot = 0; slot < player.bank.getItems().length; slot++) {
						final Item item = player.bank.getItems()[slot];

						if (item == null) {
							continue;
						}

						session.sql(
								"INSERT INTO player.bank(item_id, amount, slot, player_id) VALUES (?, ?, ?, ?) ON CONFLICT (slot, player_id) DO UPDATE SET item_id = excluded.item_id, amount = excluded.amount")
								.set(item.getId()).set(item.getAmount()).set(slot).set(player.getMemberId()).execute();

					}

					// equipment
					session.sql("DELETE FROM player.equipment WHERE player_id = ?").set(player.getMemberId()).execute();

					for (int slot = 0; slot < player.equipment.getItems().length; slot++) {
						Item item = player.equipment.get(slot);

						if (item == null) {
							continue;
						}

						session.sql(
								"INSERT INTO player.equipment(item_id, amount, slot, player_id) VALUES (?, ?, ?, ?) ON CONFLICT (slot, player_id) DO UPDATE SET item_id = excluded.item_id, amount = excluded.amount")
								.set(item.getId()).set(item.getAmount()).set(slot).set(player.getMemberId()).execute();

					}

					// inventory
					session.sql("DELETE FROM player.inventory WHERE player_id = ?").set(player.getMemberId()).execute();

					for (int slot = 0; slot < player.inventory.getItems().length; slot++) {
						Item item = player.inventory.getItems()[slot];

						if (item == null) {
							continue;
						}

						session.sql(
								"INSERT INTO player.inventory(item_id, amount, slot, player_id) VALUES (?, ?, ?, ?)")
								.set(item.getId()).set(item.getAmount()).set(slot).set(player.getMemberId()).execute();
					}

				}
			} };

	@Override
	public void save(Player player) {
//        if (!Config.FORUM_INTEGRATION) {
//            return;
//        }

		HighscoreService.saveHighscores(player);

		/*
		 * new Thread(() -> {
		 * 
		 * for (PlayerDBProperty property : DB_PROPERTIES) { try { JdbcSession session =
		 * new JdbcSession(PostgreService.getConnection()) .autocommit(false);
		 * property.write(player, session); session.commit(); } catch (SQLException ex)
		 * { logger.error(String.format("Failed to save property=%s for player=%s",
		 * property.getName(), player.getName()), ex); } }
		 * 
		 * // extremely important do not remove player.saved.set(true); }).start();
		 */
	}

	@Override
	public LoginResponse load(Player player, String expectedPassword) {
		try {
			final JdbcSession session = new JdbcSession(PostgreService.getConnection());

			boolean exists = session.sql("SELECT EXISTS(SELECT 1 FROM player.player WHERE member_id = ?)")
					.set(player.getMemberId()).select(new SingleOutcome<>(Boolean.class));

			if (!exists) {
				player.newPlayer = true;
				player.needsStarter = true;
				return LoginResponse.NORMAL;
			}

		} catch (SQLException ex) {
			logger.error(String.format("Failed to login new player=%s", player.getName()), ex);
			return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
		}

		try {
			final JdbcSession session = new JdbcSession(PostgreService.getConnection()).autocommit(false);

			for (PlayerDBProperty property : DB_PROPERTIES) {
				property.read(player, session);
			}

			session.commit();
		} catch (SQLException ex) {
			logger.error(String.format("Error loading player=%s", player.getName()), ex);
			return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
		}

		return LoginResponse.NORMAL;

	}

}
