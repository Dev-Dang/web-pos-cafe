package com.laptrinhweb.zerostarcafe.utils;

import com.laptrinhweb.zerostarcafe.core.location.GeoIpUtil;
import com.laptrinhweb.zerostarcafe.core.location.Location;
import org.junit.jupiter.api.Test;

public class GeoIPUtilTest {

    @Test
    void lookup() {
        String ip = "2402:800:99df:f644:d9b7:ef1f:51c2:f6c2";
        Location loc = GeoIpUtil.lookup(ip);
        System.out.println("IPv6: " + ip + "\n -> Location: " + loc);
    }
}
