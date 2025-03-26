package com.nesine.framework.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.Cookie;

import io.github.cdimascio.dotenv.Dotenv;

public class CookieConfig {

    private final String nsnLoginInfo;
    private final String dnSid;
    private final String il;
    private final String ls;
    private final String tdid;
    private final String nsnid;
    private final String lid;

    public CookieConfig(Dotenv dotenv) {
        this.nsnLoginInfo = dotenv.get("NSNLOGININFO");
        this.dnSid = dotenv.get("_DN_SID");
        this.il = dotenv.get("IL");
        this.ls = dotenv.get("LS");
        this.tdid = dotenv.get("TDID_12B21E52F5E170858F3301A973F80468");
        this.nsnid = dotenv.get("NSNID");
        this.lid = dotenv.get("LID");
        dotenv.get("NSC_JODYFKR2E22YMV4D2TP1TMD2ZD3U3BF");
    }

    public Map<String, Cookie> getCookies(String domain) {
        Map<String, Cookie> cookies = new LinkedHashMap<>();
        cookies.put("NSNLoginInfo", new Cookie.Builder("NSNLoginInfo", nsnLoginInfo).domain(domain).path("/").build());
        cookies.put("_dn_sid", new Cookie.Builder("_dn_sid", dnSid).domain(domain).path("/").build());
        cookies.put("il", new Cookie.Builder("il", il).domain(domain).path("/").build());
        cookies.put("ls", new Cookie.Builder("ls", ls).domain(domain).path("/").build());
        cookies.put("tdid_12b21e52f5e170858f3301a973f80468", new Cookie.Builder("tdid_12b21e52f5e170858f3301a973f80468", tdid).domain(domain).path("/").build());
        cookies.put("nsnid", new Cookie.Builder("nsnid", nsnid).domain(domain).path("/").build());
        cookies.put("lid", new Cookie.Builder("lid", lid).domain(domain).path("/").build());
        cookies.put("NSC_JOdyfkr2e22ymv4d2tp1tmd2zd3u3bf", new Cookie.Builder("NSC_JOdyfkr2e22ymv4d2tp1tmd2zd3u3bf", ls).domain(domain).path("/").build());
        return cookies;
    }
}
