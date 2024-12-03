package fun.justdevelops;

import fun.justdevelops.data.UrlInfo;
import fun.justdevelops.data.User;
import fun.justdevelops.repository.Repo;
import fun.justdevelops.repository.SimpleStorage;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


public class LinksManager {
    private static LinksManager instance;
    private Repo repo;
    private boolean isRunning;
    private STATE state;
    private User currentUser;
    private Scanner scan;
    private Config config;

    private enum STATE {
        MAIN_MENU,
        CREATE_URL,
        OPEN_URL,
        LIST_URLS, DELETE_URL,
    }

    private LinksManager() {
        repo = new SimpleStorage();
        config = new Config();
        instance = this;
    }

    public static LinksManager getInstance() {
        if (instance == null) {
            instance = new LinksManager();
        }
        return instance;
    }

    public void run() {
        isRunning = true;
        state = STATE.MAIN_MENU;
        while (isRunning) {
            try {
                clearScreen();
                showUserInfo();
                switch (state) {
                    case MAIN_MENU -> showMainMenu();
                    case CREATE_URL -> showCreateUrlMenu();
                    case OPEN_URL -> showOpenUrlMenu();
                    case DELETE_URL -> showDeleteUrlMenu();
                    case LIST_URLS -> showListUrlsMenu();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scan.close();
    }

    private void clearScreen() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    private void showUserInfo() {
        System.out.println("==========СЕРВИС КОРОТКИХ ССЫЛОК==========");
        if (currentUser == null) {
            System.out.println("Пользователь: НЕ АУТЕНТИФИЦИРОВАН");
            System.out.println("Для входа в систему введите одну из ваших коротких ссылок в разделе 2");
            System.out.println("При создании новой ссылки будет создан новый пользователь");
        } else {
            System.out.println("Пользователь: " + currentUser.getId());
            System.out.println("Создан: " + currentUser.getCreatedAt());
        }
        System.out.println();
    }

    private void showMainMenu() {
        System.out.println("===============ГЛАВНОЕ МЕНЮ===============");
        System.out.println("1. Создать короткую ссылку");
        System.out.println("2. Перейти по короткой ссылке");
        if (currentUser != null) {
            System.out.println("3. Посмотреть свои ссылки");
            System.out.println("4. Удалить ссылку");
            System.out.println("5. Выйти из системы");
        }
        System.out.println("6. Закрыть программу");
        System.out.print("Ваш выбор: ");

        String input = readUserInput();
        try {
            int result = Integer.parseInt(input);
            if (result < 1 || result > 6) {
                throw new Exception();
            }
            switch (result) {
                case 1 -> state = STATE.CREATE_URL;
                case 2 -> state = STATE.OPEN_URL;
                case 3 -> {
                    if (currentUser != null) state = STATE.LIST_URLS;
                }
                case 4 -> {
                    if (currentUser != null) state = STATE.DELETE_URL;
                }
                case 5 -> {
                    if (currentUser != null) currentUser = null;
                }
                case 6 -> {
                    isRunning = false;
                    System.out.println("Прогамма закрывается");
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            System.out.println("Вы должны ввести число от 1 до 4, соответствующее выбранному пункту меню");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void showCreateUrlMenu() {
        System.out.println("===============СОЗДАНИЕ КОРОТКОЙ ССЫЛКИ===============");
        System.out.println("Введите адрес и нажмите Enter");
        System.out.println("Или введите символ [m], чтобы вернуться в главное меню");
        String input = readUserInput();
        if (input.equals("m")) {
            state = STATE.MAIN_MENU;
            return;
        }
        try {
            String shortUrl = createUrlInfo(input);
            System.out.println("Короткая ссылка создана: " + shortUrl);
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
            state = STATE.MAIN_MENU;
        } catch (MalformedURLException | URISyntaxException me) {
            System.out.println("ОШИБКА. Указанная вами строка не является валидным адресом URL");
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
        }
    }

    private void showOpenUrlMenu() {
        System.out.println("===============ПЕРЕХОД ПО КОРОТКОЙ ССЫЛКЕ===============");
        System.out.println("Введите адрес вашей КОРОТКОЙ ссылки целиком и нажмите Enter");
        System.out.println("Или введите символ [m], чтобы вернуться в главное меню");
        String input = readUserInput();
        if (input.equals("m")) {
            state = STATE.MAIN_MENU;
            return;
        }
        try {
            openShortUrl(input);
        } catch (MalformedURLException | URISyntaxException me) {
            System.out.println("ОШИБКА. Указанная вами строка не является валидным адресом URL");
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
        }
    }

    private void showListUrlsMenu() {
        if (currentUser == null) {
            state = STATE.MAIN_MENU;
            return;
        }
        System.out.println("===============СПИСОК ССЫЛОК===============");
        var userUrls = repo.getUserUrls(currentUser.getId());
        for (Map.Entry<String,UrlInfo> e : userUrls.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue().getFullUrl() +
                    " (Число переходов: " + e.getValue().getUsageCount() + " из " + e.getValue().getUsageLimit() +
                    ", Действительна до: " + e.getValue().getExpiresAt() + ")");
        }
        System.out.println("Нажмите Enter чтобы вернуться в главное меню");
        String input = readUserInput();
        state = STATE.MAIN_MENU;
    }

    private void showDeleteUrlMenu() {
        if (currentUser == null) {
            state = STATE.MAIN_MENU;
            return;
        }
        System.out.println("===============УДАЛЕНИЕ КОРОТКОЙ ССЫЛКИ===============");
        System.out.println("Введите адрес вашей КОРОТКОЙ ссылки целиком и нажмите Enter");
        System.out.println("Или введите символ [m], чтобы вернуться в главное меню");
        String input = readUserInput();
        if (input.equals("m")) {
            state = STATE.MAIN_MENU;
            return;
        }
        try {
            deleteShortUrl(input);
        } catch (MalformedURLException | URISyntaxException me) {
            System.out.println("ОШИБКА. Указанная вами строка не является валидным адресом URL");
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
        }
    }

    private void deleteShortUrl(String shortUrl) throws MalformedURLException, URISyntaxException {
        URL url = new URL(shortUrl);
        url.toURI();

        UrlInfo info = repo.getUrlInfo(shortUrl);
        if (info == null) {
            System.out.println("Такой ссылки не существует");
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
            return;
        }
        if (!info.getUserId().equals(currentUser.getId())) {
            System.out.println("Эта ссылка принадлежит другому пользователю");
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
            return;
        }
        printUrlInfoDetails(info);
        System.out.println("Вы действительно хотите удалить эту ссылку? y/n");
        String input = readUserInput();
        if (input.equals("y")) {
            repo.removeUrlInfo(shortUrl);
            System.out.println("Ссылка удалена");
        }
    }

    private String createUrlInfo(String fullUrl) throws MalformedURLException, URISyntaxException {
        URL url = new URL(fullUrl);
        url.toURI();

        String shortUrl = compactUrl(fullUrl);
        if (currentUser == null) {
            currentUser = new User();
            repo.putUser(currentUser);
        }
        int lifetime = config.getDefaultLinkLifetime();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(lifetime);
        UrlInfo info = new UrlInfo(
                currentUser.getId(),
                fullUrl,
                3,
                expiresAt
        );
        repo.putUrlInfo(shortUrl, info);
        return shortUrl;
    }

    private String readUserInput() {
        if (scan == null) scan = new Scanner(System.in);
        String result = "";
        scan.reset();
        if (scan.hasNextLine()) result = scan.nextLine().trim();
        return result;
    }

    private String compactUrl(String fullUrl) {
        String urlId = generateUrlId();
        while (repo.hasUrl(config.getBaseUrl() + urlId)) {
            urlId = generateUrlId();
        }
        return config.getBaseUrl() + urlId;
    }

    private String generateUrlId() {
        Random r = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] text = new char[config.getUrlIdLength()];
        for (int i = 0; i < config.getUrlIdLength(); i++)
        {
            text[i] = characters.charAt(r.nextInt(characters.length()));
        }
        return new String(text);
    }

    private void openShortUrl(String shortUrl) throws MalformedURLException, URISyntaxException {
        URL url = new URL(shortUrl);
        url.toURI();

        UrlInfo info = repo.getUrlInfo(shortUrl);
        if (info == null) {
            System.out.println("Такой ссылки не существует");
            System.out.println("Нажмите Enter для продолжения");
            readUserInput();
            return;
        }
        if (!info.isValid()) {
            System.out.println("Истек срок действия либо лимит переходов. Ссылка будет удалена");
            repo.removeUrlInfo(shortUrl);
            return;
        }
        printUrlInfoDetails(info);
        currentUser = repo.getUser(info.getUserId());
        System.out.println("Нажмите Enter, чтобы открыть ссылку в браузере.");
        readUserInput();
        info.use();
        repo.putUrlInfo(shortUrl, info);
        openBrowser(info.getFullUrl());
    }

    private void printUrlInfoDetails(UrlInfo info) {
        System.out.println("Ссылка обнаружена");
        System.out.println("Полный адрес: " + info.getFullUrl());
        System.out.println("Число переходов: " + info.getUsageCount() + " из " + info.getUsageLimit());
        System.out.println("Действительна до: " + info.getExpiresAt());
    }

    private void openBrowser(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Не ссылку в браузере: [" + url + "]");
        }
    }
}
