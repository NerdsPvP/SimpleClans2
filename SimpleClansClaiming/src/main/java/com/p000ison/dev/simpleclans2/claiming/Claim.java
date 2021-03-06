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
 *     Last modified: 27.02.13 14:27
 */

package com.p000ison.dev.simpleclans2.claiming;

import com.p000ison.dev.sqlapi.TableObject;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumn;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnGetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseColumnSetter;
import com.p000ison.dev.sqlapi.annotation.DatabaseTable;

/**
 * Represents a Claim
 */
@DatabaseTable(name = "sc2_claiming")
@SuppressWarnings("unused")
public class Claim implements TableObject {

    @DatabaseColumn(position = 0, databaseName = "id", notNull = true, id = true)
    private long id;

    @DatabaseColumn(position = 1, databaseName = "clan", notNull = true)
    private long clanId;

    private final ClaimLocation location;

    public Claim() {
        this.location = new ClaimLocation();
    }

    public Claim(long clanId, ClaimLocation location) {
        this.clanId = clanId;
        this.location = location;
    }

    public long getClanID() {
        return clanId;
    }

    public long getID() {
        return id;
    }

    @DatabaseColumnSetter(position = 2, databaseName = "x", notNull = true)
    private void setDatabaseX(int x) {
        location.setX(x);
    }

    @DatabaseColumnSetter(position = 3, databaseName = "y", notNull = true)
    private void setDatabaseY(short y) {
        location.setY(y);
    }

    @DatabaseColumnSetter(position = 4, databaseName = "z", notNull = true)
    private void setDatabaseZ(int z) {
        location.setZ(z);
    }

    @DatabaseColumnGetter(databaseName = "x")
    public int getX() {
        return location.getX();
    }

    @DatabaseColumnGetter(databaseName = "z")
    public int getZ() {
        return location.getZ();
    }

    @DatabaseColumnGetter(databaseName = "y")
    public short getY() {
        return (short) location.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Claim claim = (Claim) o;

        if (id != claim.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Claim{" +
                "location=" + location +
                '}';
    }
}
