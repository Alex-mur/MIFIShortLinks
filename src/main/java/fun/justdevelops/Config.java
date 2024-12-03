package fun.justdevelops;

import java.util.Properties;

public class Config
{
    Properties properties;
    public Config()
    {
        properties = new java.util.Properties();
        try {
            properties.load(this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("app_config.cfg"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getDefaultLinkLifetime() {
        int result = 48;
        try {
            return Integer.parseInt(properties.getProperty("DEFAULT_URL_LIFETIME_HOURS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getUrlIdLength() {
        int result = 0;
        try {
            result = Integer.parseInt(properties.getProperty("SHORT_URL_ID_LENGTH"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result < 2 || result > 8) return 6;
        return result;
    }

    public String getBaseUrl() {
        return properties.getProperty("SITE_BASE_URL");
    }
}