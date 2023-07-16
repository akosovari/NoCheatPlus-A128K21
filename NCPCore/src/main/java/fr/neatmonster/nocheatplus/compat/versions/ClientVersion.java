/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.neatmonster.nocheatplus.compat.versions;

public enum ClientVersion {
    /**
     * 1.7.2 or 1.7.4 or 1.7.5 have the same protocol version.
     */
    V_1_7_2(4),
    /**
     * 1.7.6 or 1.7.7 or 1.7.8 or 1.7.9 or 1.7.10 have the same protocol version.
     */
    V_1_7_10(5),

    V_1_8(47),

    V_1_9(107), V_1_9_1(108), V_1_9_2(109),
    /**
     * 1.9.3 or 1.9.4 have the same protocol version.
     */
    V_1_9_3(110),
    V_1_10(210),
    V_1_11(315),
    /**
     * 1.11.1 or 1.11.2 have the same protocol version.
     */
    V_1_11_1(316), V_1_12(335), V_1_12_1(338), V_1_12_2(340),

    V_1_13(393), V_1_13_1(401), V_1_13_2(404),

    V_1_14(477), V_1_14_1(480), V_1_14_2(485), V_1_14_3(490), V_1_14_4(498),

    V_1_15(573), V_1_15_1(575), V_1_15_2(578),

    V_1_16(735), V_1_16_1(736), V_1_16_2(751), V_1_16_3(753),
    /**
     * 1.16.4 or 1.16.5 have the same protocol version.
     */
    V_1_16_4(754),

    V_1_17(755), V_1_17_1(756),

    /**
     * 1.18 or 1.18.1 have the same protocol version.
     */
    V_1_18(757),
    V_1_18_2(758),

    V_1_19(759), V_1_19_1(760), V_1_19_3(761), V_1_19_4(762),
    /**
     * 1.20.1 has the same protocol version.
     */
    V_1_20(763),


    LOWER_THAN_KNOWN_VERSIONS(V_1_7_2.protocolID - 1, false),
    HIGHER_THAN_KNOWN_VERSIONS(V_1_20.protocolID + 1, false),
    UNKNOWN(-1, false);

    private final int protocolID;
    private final String name;

    ClientVersion(int protocolVersion) {
        this.protocolID = protocolVersion;
        this.name = name().substring(2).replace("_", ".");
    }

    ClientVersion(int protocolVersion, boolean knownVersion) {
        this.protocolID = protocolVersion;
        if (!knownVersion) {
            this.name = name();
        } else {
            this.name = name().substring(2).replace("_", ".");
        }
    }

    private static final ClientVersion[] VALUES = values();
    private static final int LOWEST_KNOWN_PROTOCOL_VERSION = LOWER_THAN_KNOWN_VERSIONS.protocolID + 1;
    private static final int HIGHEST_KNOWN_PROTOCOL_VERSION = HIGHER_THAN_KNOWN_VERSIONS.protocolID - 1;

    /**
     * Get a ClientVersion enum by protocol version.
     *
     * @param protocolVersion the protocol version.
     * @return ClientVersion
     */
    public static ClientVersion getById(int protocolVersion) {
        if (protocolVersion < LOWEST_KNOWN_PROTOCOL_VERSION) {
            return LOWER_THAN_KNOWN_VERSIONS;
        } else if (protocolVersion > HIGHEST_KNOWN_PROTOCOL_VERSION) {
            return HIGHER_THAN_KNOWN_VERSIONS;
        } else {
            for (ClientVersion version : VALUES) {
                if (version.protocolID > protocolVersion) {
                    break;
                } else if (version.protocolID == protocolVersion) {
                    return version;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Get the release name of this client version.
     * For example, for the V_1_19_4 enum constant, it would return "1.19.4".
     *
     * @return Release name
     */
    public String getReleaseName() {
        return name;
    }

    /**
     * Protocol version of this client version.
     *
     * @return Protocol version.
     */
    public int getProtocolVersion() {
        return protocolID;
    }

    /**
     * Get the lastest version
     *
     * @return Known lastest version
     */
    public static ClientVersion getLatest() {
        return VALUES[VALUES.length-4]; // -3 to ignore unknown, lower_than, higher than; -1 for offset index=>-4 
    }

    /**
     * Get the oldest version
     *
     * @return Known oldest version
     */
    public static ClientVersion getOldest() {
        return VALUES[0];
    }

    /**
     * Test if the given version is between the two given ones.
     * 
     * @param versionLow
     *            Lower bound
     * @param includeLow
     *            If to allow equality for the low edge.
     * @param versionHigh
     *            Upper bound
     * @param includeHigh
     *            If to allow equality for the high edge.
     * @return true if the client version is in given range
     */
    public boolean isVersionBetween(ClientVersion versionLow, boolean includeLow, ClientVersion versionHigh, boolean includeHigh) {
        if (includeLow) {
            if (isOlderThan(versionLow)) {
                return false;
            }
        } else {
            if (isOlderThanOrEquals(versionLow)) {
                return false;
            }
        }
        if (includeHigh) {
            if (isNewerThan(versionHigh)) {
                return false;
            }
        } else {
            if (isNewerThanOrEquals(versionHigh)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if client version is newer compare to the compared client version?
     *
     * @param target Client version to test
     * @return true if yes and viceversa
     */
    public boolean isNewerThan(ClientVersion target) {
        return protocolID > target.protocolID;
    }

    /**
     * Check if client version is newer or equals compare to the compared client version?
     *
     * @param target Client version to test
     * @return true if yes and viceversa
     */
    public boolean isNewerThanOrEquals(ClientVersion target) {
        return protocolID >= target.protocolID;
    }

    /**
     * Check if client version is older compare to the compared client version?
     *
     * @param target Client version to test
     * @return true if yes and viceversa
     */
    public boolean isOlderThan(ClientVersion target) {
        return protocolID < target.protocolID;
    }

    /**
     * Check if client version older than or equal compare to the compared client version?
     *
     * @param target Client version to test
     * @return true if yes and viceversa
     */
    public boolean isOlderThanOrEquals(ClientVersion target) {
        return protocolID <= target.protocolID;
    }
}
