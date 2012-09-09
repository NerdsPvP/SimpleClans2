/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:33
 */

package com.p000ison.dev.simpleclans2.clanplayer;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.ranks.Rank;
import com.p000ison.dev.simpleclans2.requests.VoteResult;
import com.p000ison.dev.simpleclans2.util.DateHelper;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Represents a ClanPlayer
 */
public class ClanPlayer {

    private SimpleClans plugin;

    private long id = -1;
    private String name;
    private Clan clan;
    private boolean banned, leader, trusted, friendlyFire;
    private long lastSeen, joinDate;
    private int neutralKills, rivalKills, civilianKills, deaths;
    private PlayerFlags flags;
    private VoteResult lastVoteResult = VoteResult.UNKNOWN;
    private Rank rank;

    public ClanPlayer(SimpleClans plugin, String name)
    {
        flags = new PlayerFlags();
        this.plugin = plugin;
        this.name = name;
    }

    public ClanPlayer(SimpleClans plugin, long id, String name)
    {
        this(plugin, name);
        this.id = id;
    }

    /**
     * Returns the id of the clan the player is in
     *
     * @return The id
     */
    public long getClanId()
    {
        return clan.getId();
    }

    /**
     * Gets the clan the player is in.
     *
     * @return The player's clan
     */
    public Clan getClan()
    {
        return clan;
    }

    /**
     * Sets the players clan
     *
     * @param clan The clan to set
     */
    public void setClan(Clan clan)
    {
        this.clan = clan;
    }

    public String getName()
    {
        return name;
    }

    public boolean isBanned()
    {
        return banned;
    }

    public void setBanned(boolean banned)
    {
        this.banned = banned;
    }

    public boolean isLeader()
    {
        return leader;
    }

    public void setLeader(boolean leader)
    {
        this.leader = leader;
    }

    public boolean isTrusted()
    {
        return trusted;
    }

    public void setTrusted(boolean trusted)
    {
        this.trusted = trusted;
    }

    public boolean isFriendlyFireOn()
    {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire)
    {
        this.friendlyFire = friendlyFire;
    }

    public long getLastSeenDate()
    {
        return lastSeen;
    }

    public void setLastSeenDate(long lastSeen)
    {
        this.lastSeen = lastSeen;
    }

    public long getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(long joinDate)
    {
        this.joinDate = joinDate;
    }

    public int getNeutralKills()
    {
        return neutralKills;
    }

    public void addNeutralKills(int neutralKills)
    {
        this.setNeutralKills(this.getNeutralKills() + 1);
    }

    public int getRivalKills()
    {
        return rivalKills;
    }

    public void addRivalKills()
    {
        this.setRivalKills(this.getRivalKills() + 1);
    }

    public int getCivilianKills()
    {
        return civilianKills;
    }

    public void addCivilianKills()
    {
        this.setCivilianKills(this.getCivilianKills() + 1);
    }

    public int getDeaths()
    {
        return deaths;
    }

    public void addDeath()
    {
        this.setDeaths(this.getDeaths() + 1);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public PlayerFlags getFlags()
    {
        return flags;
    }

    public void addPastClan(String string)
    {
        flags.addPastClan(string);
    }

    public Set<String> getPastClans()
    {
        return flags.getPastClans();
    }

    public void setFlags(PlayerFlags flags)
    {
        this.flags = flags;
    }

    /**
     * Gets the days this clanPlayer is inactive
     *
     * @return The days this clanPlayer is inactive
     */
    public double getInactiveDays()
    {
        return DateHelper.differenceInDays(lastSeen, System.currentTimeMillis());
    }

    public VoteResult getLastVoteResult()
    {
        return lastVoteResult;
    }

    public void setLastVoteResult(VoteResult lastVoteResult)
    {
        this.lastVoteResult = lastVoteResult;
    }

    /**
     * Returns weighted kill score for this player (kills multiplied by the
     * different weights)
     *
     * @return Returns weighted kills.
     */
    public double getWeightedKills()
    {
        return (getRivalKills() * plugin.getSettingsManager().getKillWeightRival()) + (getNeutralKills() * plugin.getSettingsManager().getKillWeightNeutral()) + (getCivilianKills() * plugin.getSettingsManager().getKillWeightCivilian());
    }

    /**
     * Returns weighted-kill/death ratio
     *
     * @return The KDR.
     */
    public float getKDR()
    {
        int totalDeaths = getDeaths();

        if (totalDeaths == 0) {
            totalDeaths = 1;
        }

        return ((float) getWeightedKills()) / ((float) totalDeaths);
    }

    /**
     * Returns a player object. May be null if the player is not online
     *
     * @return Returns a player object.
     */
    public Player toPlayer()
    {
        return plugin.getServer().getPlayerExact(name);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Clan && ((Clan) obj).getId() == id;
    }

    @Override
    public int hashCode()
    {
        return (int) id;
    }

    public void setNeutralKills(int neutralKills)
    {
        this.neutralKills = neutralKills;
    }

    public void setRivalKills(int rivalKills)
    {
        this.rivalKills = rivalKills;
    }

    public void setCivilianKills(int civilianKills)
    {
        this.civilianKills = civilianKills;
    }

    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }

    public boolean hasPermission(String permission)
    {
        return rank != null && rank.hasPermission(permission);
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }
}
