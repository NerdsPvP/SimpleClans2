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
 *     Last modified: 11.03.13 16:59
 */

package com.p000ison.dev.simpleclans2.claiming;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.p000ison.dev.simpleclans2.api.Configuration;
import com.p000ison.dev.simpleclans2.api.logging.Logging;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents a SettingsManager
 */
public class SettingsManager {
    private SimpleClansClaiming plugin;
    private Configuration config;

    private int claimSize, claimHeigth;

    private double taxesPerChunk;
    private long taxesInterval;

    private BiMap<Byte, String> mapIDs;

    public SettingsManager(SimpleClansClaiming plugin) {
        this.plugin = plugin;
        init();
    }

    public boolean init() {
        try {
            this.config = new Configuration(new File(plugin.getDataFolder(), "config.yml"));
            this.config.setDefault("config.yml", plugin);
        } catch (IOException e) {
            Logging.debug(e, false);
        } catch (InvalidConfigurationException e) {
            Logging.debug(Level.SEVERE, "Failed at loading config! Maybe the formatting is invalid! Check your config.yml: \n%s", e.getMessage());
        }

        if (config == null) {
            return false;
        }

        save();
        load();
        return true;
    }

    private void load() {
        ConfigurationSection claim = config.getConfigurationSection("claim");

        claimSize = claim.getInt("size");
        claimHeigth = claim.getInt("height");

        ConfigurationSection taxes = config.getConfigurationSection("taxes");

        taxesPerChunk = taxes.getDouble("per-chunk");
        taxesInterval = taxes.getLong("interval");


        ConfigurationSection worlds = config.getConfigurationSection("worlds");

        Map<Byte, String> tempMapIDs = new HashMap<Byte, String>();

        for (String world : worlds.getKeys(false)) {
            tempMapIDs.put((byte) worlds.getInt(world), world);
        }

        mapIDs = ImmutableBiMap.copyOf(tempMapIDs);
    }

    public void save() {
        try {
            this.config.save();
        } catch (IOException e) {
            Logging.debug(e, false);
        }
    }

    public void reload() {
        try {
            config.reload();
        } catch (IOException e) {
            Logging.debug(e, false);
            return;
        } catch (InvalidConfigurationException e) {
            Logging.debug(Level.SEVERE, "Failed at loading config! Maybe the formatting is invalid! Check your config.yml: \n%s", e.getMessage());
            return;
        }
        load();
    }

    public int getClaimSize() {
        return claimSize;
    }

    public int getClaimHeigth() {
        return claimHeigth;
    }

    public double getTaxesPerChunk() {
        return taxesPerChunk;
    }

    public long getTaxesInterval() {
        return taxesInterval;
    }

    public BiMap<Byte, String> getMapIDs() {
        return mapIDs;
    }
}
