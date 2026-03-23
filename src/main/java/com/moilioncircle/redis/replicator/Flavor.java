/*
 * Copyright 2026
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moilioncircle.redis.replicator;

import static com.moilioncircle.redis.replicator.Constants.RDB_VERSION;
import static com.moilioncircle.redis.replicator.Constants.VALKEY_VERSION;
import static com.moilioncircle.redis.replicator.util.Strings.lappend;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.12.0
 */
public enum Flavor {
    REDIS("REDIS"), VALKEY("VALKEY");
    
    private String magic;
    
    Flavor(String magic) {
        this.magic = magic;
    }
    
    public String getMagic() {
        return magic;
    }
    
    public String convertToRdbVersion(int rdbVer) {
        if (this == REDIS) {
            return lappend(rdbVer, 4, '0');
        } else if (this == VALKEY) {
            return lappend(rdbVer, 3, '0');
        } else {
            throw ERROR;
        }
    }
    
    public int getRdbVersion(String version) {
        if (this == REDIS) {
            if (!REDIS_VERSIONS.containsKey(version)) {
                throw new AssertionError("unsupported redis version :" + version);
            }
            return REDIS_VERSIONS.get(version);
        } else if (this == VALKEY) {
            if (!VALKEY_VERSIONS.containsKey(version)) {
                throw new AssertionError("unsupported valkey version :" + version);
            }
            return VALKEY_VERSIONS.get(version);
        } else {
            throw ERROR;
        }
    }
    
    public static Flavor toFlavor(String flavor) {
        if (flavor == null) throw ERROR;
        if (flavor.equals(REDIS.magic.toLowerCase())) return REDIS;
        if (flavor.equals(VALKEY.magic.toLowerCase())) return VALKEY;
        throw ERROR;
    }
    
    //
    private static final Map<String, Integer> REDIS_VERSIONS = new HashMap<>();
    private static final Map<String, Integer> VALKEY_VERSIONS = new HashMap<>();
    private static Error ERROR = new AssertionError("unsupported flavor");
    
    static {
        REDIS_VERSIONS.put("2.6", 6);
        REDIS_VERSIONS.put("2.8", 6);
        REDIS_VERSIONS.put("3.0", 6);
        REDIS_VERSIONS.put("3.2", 7);
        REDIS_VERSIONS.put("4.0", 8);
        REDIS_VERSIONS.put("5.0", 9);
        REDIS_VERSIONS.put("6.0", 9);
        REDIS_VERSIONS.put("6.2", 9);
        REDIS_VERSIONS.put("7.0", 10);
        REDIS_VERSIONS.put("7.2", 11);
        REDIS_VERSIONS.put("7.4", RDB_VERSION);
        REDIS_VERSIONS.put("8.0", RDB_VERSION);
        REDIS_VERSIONS.put("8.2", RDB_VERSION);
        REDIS_VERSIONS.put("8.4", RDB_VERSION);
        
        // VALKEY_VERSIONS.put("7.2", 11);
        // VALKEY_VERSIONS.put("8.0", 11);
        // VALKEY_VERSIONS.put("8.1", 11);
        VALKEY_VERSIONS.put("9.0", VALKEY_VERSION);
        VALKEY_VERSIONS.put("9.1", VALKEY_VERSION);
    }
}
