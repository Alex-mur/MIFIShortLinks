package fun.justdevelops.repository;

import fun.justdevelops.data.UrlInfo;
import fun.justdevelops.data.User;
import java.util.*;

public class SimpleStorage implements Repo {

    private ArrayList<User> users;
    private HashMap<String,UrlInfo> urls;


    public SimpleStorage() {
        this.users = new ArrayList<>();
        this.urls = new HashMap<>();
    }

    @Override
    public void putUrlInfo(String shortUrl, UrlInfo urlInfo) {
        urls.put(shortUrl, urlInfo);
    }

    @Override
    public UrlInfo getUrlInfo(String shortUrl) {
        return urls.get(shortUrl);
    }

    @Override
    public void putUser(User user) {
        users.add(user);
    }

    @Override
    public User getUser(UUID id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public boolean hasUrl(String shortUrl) {
        return urls.containsKey(shortUrl);
    }

    @Override
    public void removeUrlInfo(String shortUrl) {
        urls.remove(shortUrl);
    }

    @Override
    public  HashMap<String,UrlInfo> getUserUrls(UUID id) {
        HashMap<String,UrlInfo> result = new HashMap<>();
        for (Map.Entry<String,UrlInfo> e : urls.entrySet()) {
            if (e.getValue().getUserId().equals(id)) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }
}
