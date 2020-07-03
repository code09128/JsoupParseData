package com.example.dustin0128.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by dustin0128 on 2018/8/10.
 */
public class VersionAPI {

    public class Result {
        public String versionName = null;
        public String updateInfos = null;
    }

    public Result getNewVersion(String packageName) throws IOException {
        Result result = new Result();
        Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName + "&hl=it" )
                .timeout(30000)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get();
                result.versionName = doc.select("div[itemprop=softwareVersion]")
                .first()
                .ownText();
                result.updateInfos = doc.select("div.recent-change")
                .first()
                .ownText();
        return result;
    }

}
