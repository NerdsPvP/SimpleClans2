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
 *     Created: 30.09.12 17:42
 */

package com.p000ison.dev.simpleclans2.commands.clan.rank;

import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.ranks.Rank;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a RankSetCommand
 */
public class RankSetCommand extends GenericPlayerCommand {


    public RankSetCommand(SimpleClans plugin)
    {
        super("RankSet", plugin);
        setArgumentRange(2, 50);
        setUsages(Language.getTranslation("usage.rank.set", plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("rank.set.command"));
    }

    @Override
    public String getMenu(ClanPlayer clanPlayer)
    {
        if (clanPlayer.isLeader() || clanPlayer.hasRankPermission("manage.ranks")) {
            return Language.getTranslation("menu.rank.set", plugin.getSettingsManager().getClanCommand());
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = clanPlayer.getClan();

        if (!clan.isLeader(clanPlayer) && !clanPlayer.hasRankPermission("manage.ranks")) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("no.leader.permissions"));
            return;
        }

        ClanPlayer query = plugin.getClanPlayerManager().getClanPlayer(args[0]);

        if (query == null || !query.getClan().equals(clan)) {
            player.sendMessage(Language.getTranslation("the.player.is.not.a.member.of.your.clan"));
            return;
        }

        Rank rank = clan.getRank(GeneralHelper.arrayBoundsToString(1, args));

        System.out.println(clan.getRanks().toArray(new Rank[1])[0].getName());

        if (rank == null) {
            player.sendMessage(Language.getTranslation("rank.not.found"));
            return;
        }

        query.setRank(rank);
        query.update();
    }
}