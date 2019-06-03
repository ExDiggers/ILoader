package cc.flycode.loader.api;

import cc.invictusgames.iUtils.user.User;
import cc.invictusgames.iUtils.user.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author langgezockt (langgezockt@gmail.com)
 * 02.06.2019 / 21:04
 * ILoader / cc.flycode.loader.api
 */

public class IUtilsAPI {

    /**
     * ban a player
     *
     * @param uuid     uuid of the player
     * @param executor name of the executor
     * @param duration duration of the ban in seconds, use -1 for permanent bans
     * @param reason   reason for the ban
     * @param silent   staff only broadcast
     */
    public static void ban(UUID uuid, String executor, long duration, String reason, boolean silent) {
        User.getByUUID(uuid).ban(executor, duration, reason, silent);
    }

    /**
     * mute a player
     *
     * @param uuid     uuid of the player
     * @param executor name of the executor
     * @param duration duration of the ban in seconds, use -1 for permanent bans
     * @param reason   reason for the ban
     * @param silent   staff only broadcast
     */
    public static void mute(UUID uuid, String executor, long duration, String reason, boolean silent) {
        User.getByUUID(uuid).mute(executor, duration, reason, silent);
    }

    /**
     * blacklist a player
     *
     * @param uuid     uuid of the player
     * @param executor name of the executor
     * @param reason   reason for the blacklist
     * @param silent   staff only broadcast
     */
    public static void blacklist(UUID uuid, String executor, String reason, boolean silent) {
        User.getByUUID(uuid).blacklist(executor, reason, silent);
    }

    /**
     * warn a player
     *
     * @param uuid     uuid of the player
     * @param executor name of the executor
     * @param reason   reason for the warn
     * @param silent   staff only broadcast
     */
    public static void warn(UUID uuid, String executor, String reason, boolean silent) {
        User.getByUUID(uuid).warn(executor, reason, silent);
    }

    /**
     * strike a player
     *
     * @param uuid     uuid of the player
     * @param executor name of the executor
     * @param reason   reason for the strike
     * @param silent   staff only broadcast
     */
    public static void strike(UUID uuid, String executor, String reason, boolean silent) {
        User.getByUUID(uuid).strike(executor, reason, silent);
    }

    /**
     * kick a player
     *
     * @param uuid     uuid of the player
     * @param executor name of the executor
     * @param reason   reason for the kick
     * @param silent   staff only broadcast
     */
    public static void kick(UUID uuid, String executor, String reason, boolean silent) {
        User.getByUUID(uuid).kick(executor, reason, silent);
    }

    /**
     * get the rank of a player
     *
     * @param uuid uuid of the player
     * @return name of the players rank
     */
    public static String getRank(UUID uuid) {
        return User.getByUUID(uuid).getRank().getName();
    }

    /**
     * check if a player is banned
     *
     * @param uuid uuid of the player
     * @return if player is banned
     */
    public static boolean isBanned(UUID uuid) {
        return User.getByUUID(uuid).getActiveBan() == null;
    }

    /**
     * get the reason of a players ban
     *
     * @param uuid uuid of the player
     * @return reason of a players ban
     */
    public static String getBanReason(UUID uuid) {
        return User.getByUUID(uuid).getActiveBan().getReason();
    }

    /**
     * get the duration of a players ban
     *
     * @param uuid uuid of the player
     * @return duration of a players ban
     */
    public static String getBanDuration(UUID uuid) {
        return User.getByUUID(uuid).getActiveBan().getDuration();
    }

    /**
     * get the remaining time of a players ban
     *
     * @param uuid uuid of the player
     * @return remaining time of a players ban
     */
    public static String getBanRemaining(UUID uuid) {
        return User.getByUUID(uuid).getActiveBan().getRemainingTime();
    }

    /**
     * get the date of a players ban
     *
     * @param uuid uuid of the player
     * @return date of a players ban
     */
    public static String getBanDate(UUID uuid) {
        return User.getByUUID(uuid).getActiveBan().getDate();
    }

    /**
     * get the expiration date of a players ban
     *
     * @param uuid uuid of the player
     * @return expiration date of a players ban
     */
    public static String getBanUntil(UUID uuid) {
        return User.getByUUID(uuid).getActiveBan().getUntil();
    }

    /**
     * check if a player is muted
     *
     * @param uuid uuid of the player
     * @return if player is muted
     */
    public static boolean isMuted(UUID uuid) {
        return User.getByUUID(uuid).getActiveMute() == null;
    }

    /**
     * get the reason of a players mute
     *
     * @param uuid uuid of the player
     * @return reason of a players mute
     */
    public static String getMuteReason(UUID uuid) {
        return User.getByUUID(uuid).getActiveMute().getReason();
    }

    /**
     * get the duration of a players mute
     *
     * @param uuid uuid of the player
     * @return duration of a players mute
     */
    public static String getMuteDuration(UUID uuid) {
        return User.getByUUID(uuid).getActiveMute().getDuration();
    }

    /**
     * get the remaining time of a players mute
     *
     * @param uuid uuid of the player
     * @return remaining time of a players mute
     */
    public static String getMuteRemaining(UUID uuid) {
        return User.getByUUID(uuid).getActiveMute().getRemainingTime();
    }

    /**
     * get the date of a players mute
     *
     * @param uuid uuid of the player
     * @return date of a players mute
     */
    public static String getMuteDate(UUID uuid) {
        return User.getByUUID(uuid).getActiveMute().getDate();
    }

    /**
     * get the expiration date of a players mute
     *
     * @param uuid uuid of the player
     * @return expiration date of a players mute
     */
    public static String getMuteUntil(UUID uuid) {
        return User.getByUUID(uuid).getActiveMute().getUntil();
    }

    /**
     * check if a player is blacklisted
     *
     * @param uuid uuid of the player
     * @return if player is blacklisted
     */
    public static boolean isBlacklisted(UUID uuid) {
        return User.getByUUID(uuid).getActiveBlacklist() == null;
    }

    /**
     * get the reason of a players blacklist
     *
     * @param uuid uuid of the player
     * @return reason of a players blacklist
     */
    public static String getBlacklistReason(UUID uuid) {
        return User.getByUUID(uuid).getActiveBlacklist().getReason();
    }

    /**
     * get the date of a players ban
     *
     * @param uuid uuid of the player
     * @return date of a players ban
     */
    public static String getBlacklistDate(UUID uuid) {
        return User.getByUUID(uuid).getActiveBlacklist().getDate();
    }

    public static boolean doesRankExist(String name) {
        return Rank.doesRankExist(name);
    }

    /**
     * get a players ranks prefix
     *
     * @param uuid uuid of the player
     * @return prefix of a players rank
     */
    public static String getPrefix(UUID uuid) {
        return User.getByUUID(uuid).getRank().getPrefix();
    }

    /**
     * get a ranks prefix
     *
     * @param rank name of the rank
     * @return prefix of a rank
     */
    public static String getPrefix(String rank) {
        return Rank.getByName(rank).getPrefix();
    }

    /**
     * get a players ranks suffix
     *
     * @param uuid uuid of the player
     * @return suffix of a players rank
     */
    public static String getSuffix(UUID uuid) {
        return User.getByUUID(uuid).getRank().getSuffix();
    }

    /**
     * get a ranks suffix
     *
     * @param rank name of the rank
     * @return suffix of a rank
     */
    public static String getSuffix(String rank) {
        return Rank.getByName(rank).getSuffix();
    }

    /**
     * get a players ranks color
     *
     * @param uuid uuid of the player
     * @return color of a players rank
     */
    public static String getColor(UUID uuid) {
        return User.getByUUID(uuid).getRank().getColor();
    }

    /**
     * get a ranks color
     *
     * @param rank name of the rank
     * @return color of a rank
     */
    public static String getColor(String rank) {
        return Rank.getByName(rank).getColor();
    }

    /**
     * get a players ranks chat color
     *
     * @param uuid uuid of the player
     * @return chat color of a players rank
     */
    public static String getChatColor(UUID uuid) {
        return User.getByUUID(uuid).getRank().getChatColor();
    }

    /**
     * get a ranks chat color
     *
     * @param rank name of the rank
     * @return chat color of a rank
     */
    public static String getChatColor(String rank) {
        return Rank.getByName(rank).getChatColor();
    }

    /**
     * get a players ranks weight
     *
     * @param uuid uuid of the player
     * @return weight of a players rank
     */
    public static long getWeight(UUID uuid) {
        return User.getByUUID(uuid).getRank().getWeight();
    }

    /**
     * get a ranks weight
     *
     * @param rank name of the rank
     * @return weight of a rank
     */
    public static long getWeight(String rank) {
        return Rank.getByName(rank).getWeight();
    }

    /**
     * get all Ranks sorted by weight
     *
     * @return ranks sorted by weight
     */
    public static List<String> getRanksSorted() {
        List<String> sortedRanks = new ArrayList<>();
        Rank.getRanksSorted().forEach(rank -> sortedRanks.add(rank.getName()));
        return sortedRanks;
    }

    /**
     * get online players sorted by their rank weight
     *
     * @return online players sorted by their rank weight
     */
    public static List<Player> getOnlinePlayersSorted() {
        List<Player> sortedPlayers = new ArrayList<>();
        User.getOnlineUsersSorted().forEach(user -> sortedPlayers.add(Bukkit.getPlayer(user.getUuid())));
        return sortedPlayers;
    }

}
