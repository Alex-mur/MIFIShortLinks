package fun.justdevelops.repository;

import fun.justdevelops.data.UrlInfo;
import fun.justdevelops.data.User;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface Repo {

    void putUrlInfo(String shortUrl, UrlInfo urlInfo);

    UrlInfo getUrlInfo(String shortUrl);

    void putUser(User user);

    User getUser(UUID id);

    boolean hasUrl(String shortUrl);

    void removeUrlInfo(String shortUrl);

    HashMap<String,UrlInfo> getUserUrls(UUID id);
}
