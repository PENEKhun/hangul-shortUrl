package com.penek.shortUrl.Util;

import com.maxmind.geoip2.DatabaseReader;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;

import java.io.InputStream;
import java.net.InetAddress;

public class IpAddressUtils {
    public static String getCountry(String ip) {
        InputStream ip2CountryDatabase = geoLiteDataBase();

        try (ip2CountryDatabase) {
            if (ip2CountryDatabase == null) {
                throw new CustomException(ErrorCode.IP_DB_NOT_FOUND);
            }
            return parseCountry(ip, ip2CountryDatabase);
        } catch (Exception e) {
            return "Unknown Country";
        }
    }

    public static InputStream geoLiteDataBase() {
        ClassLoader classLoader = IpAddressUtils.class.getClassLoader();
        return classLoader.getResourceAsStream("GeoLite2-Country_20220308/GeoLite2-Country.mmdb");
    }

    public static String parseCountry(String ip, InputStream localDB) {
        try {
            DatabaseReader reader = new DatabaseReader.Builder(localDB).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            return reader.country(ipAddress).getCountry().getIsoCode();
        } catch (Exception e) {
            return "Unknown Country";
        }
    }
}
