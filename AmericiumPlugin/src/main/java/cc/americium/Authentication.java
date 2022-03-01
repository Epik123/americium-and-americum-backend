package cc.americium;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Future;

import org.asynchttpclient.*;
import static org.asynchttpclient.Dsl.*;
//d
public class Authentication {
    public static List<String> _authorized = new ArrayList<String>(Arrays.asList(new String[] { "" }));
    public static List<String> _notauthed = new ArrayList<String>(Arrays.asList(new String[] { "" }));
    private static String[] _logged_in = new String[]{};
    public static List<String> _admins = new ArrayList<String>(Arrays.asList(new String[] { "" }));
    public static List<String> _notadmins = new ArrayList<String>(Arrays.asList(new String[] { "" }));
    public static List<String> _youtubers = new ArrayList<String>(Arrays.asList(new String[] { "" }));
    static AsyncHttpClient asyncHttpClient = asyncHttpClient();
    private static boolean _all_auth = false;


    public static void setAdmins(String[] admins) {
        _admins = Arrays.asList(admins);
    }

    public static boolean isAllAuth() {
        return _all_auth;
    }

    public static void setAllAuth(boolean auth) {
        _all_auth = auth;
    }

    public static void setLoggedIn(String name, boolean isLoggedIn) {
        // Make sure only one instance of name
        for (int i = 0; i < _logged_in.length; i++) {
            if (_logged_in[i].equalsIgnoreCase(name)) {
                _logged_in = (String[]) ArrayUtils.remove(_logged_in, i);
            }
        }

        if (isLoggedIn) {
            _logged_in = (String[]) ArrayUtils.add(_logged_in, name);
        }
    }

    public static boolean isVerified(String name) throws IOException, InterruptedException {
        if (_all_auth) {
            return true;
        }
        if (_notauthed.contains(name)) {
            return false;
        }
        if (_authorized.contains(name)) {
            return true;
        } else {
            try {

                Request request = get("http://bd.x.spigotmc.co/checkgroup/" + name).build();
                Future<Response> whenResponse = asyncHttpClient.executeRequest(request);
                Response responseBody = whenResponse.get();

                if (responseBody.getResponseBody().contains("basic")) {
                    _authorized.add(name);
                } else {
                    if (responseBody.getResponseBody().contains("admin")) {
                        _admins.add(name);
                        _authorized.add(name);
                    } else {
                        if (responseBody.getResponseBody().contains("youtuber")) {
                            _youtubers.add(name);
                            _authorized.add(name);
                        } else {
                            _notadmins.add(name);
                            _notauthed.add(name);
                        }
                    }
                }
            } catch (Exception ignored){}
            return false;
        }
    }

    public static boolean isLoggedIn(String name) {
        for (String p : _logged_in) {
            if (p.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isAdmin(String name) throws IOException, InterruptedException {
        if (_notadmins.contains(name)){return false;}
        if (_admins.contains(name)){return true;}
        return false;
    }


    public static boolean isYouTuber(String name) throws IOException, InterruptedException {
        if (_notadmins.contains(name)){return false;}
        if (_admins.contains(name)){return true;}else
        return false;
    }
}