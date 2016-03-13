package info.morontt.link_checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HttpFetcher {
    private static final String DEFAULT_USER_AGENT = "link-checker";

    ArrayList<String> domains;
    ArrayList<LinkPair> pairs;
    Pattern linkPattern;

    class LinkPair {
        String target;
        URL targetURL;
        String referrer;
        String host;
        String schema;
        boolean checked = false;
        int responseCode;

        LinkPair(String t, String r) throws MalformedURLException {
            this(t);
            referrer = r;
        }

        LinkPair(String t) throws MalformedURLException {
            target = t;
            referrer = "...";
            targetURL = new URL(t);

            host = targetURL.getHost();
            schema = targetURL.getProtocol();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            LinkPair other = (LinkPair) obj;

            return target.equals(other.target);
        }
    }

    HttpFetcher(ArrayList<String> domains) {
        this.domains = domains;

        pairs = new ArrayList<LinkPair>();
        for (String domain : domains) {
            try {
                LinkPair link = new LinkPair("http://" + domain);
                pairs.add(link);
            } catch (MalformedURLException e) {
                System.err.println("broken domain \"" + domain + "\" in config.yaml");
                System.exit(1);
            }
        }

        linkPattern = Pattern.compile("href=(?:\"([^\"]*)\"|'([^']*)')");
    }

    void fetch() {
        LinkPair link = getNext();
        link.checked = true;

        try {
            URL url = link.targetURL;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

            link.responseCode = conn.getResponseCode();

            String type = conn.getContentType();
            if (type.indexOf("text/html") == 0) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    checkString(inputLine, link);
                }

                in.close();
            }

            conn.disconnect();
            System.out.println("success: " + link.responseCode + " " + link.target);
        } catch (IOException e) {
            System.err.println("error: " + link.responseCode + " " + link.target + " - referrer: " + link.referrer);
        } catch (ClassCastException e) {
            System.err.println("error: " + link.target + " - referrer: " + link.referrer);
        }
    }

    public boolean hasNext() {
        for (LinkPair link : pairs) {
            if (!link.checked) {
                return true;
            }
        }

        return false;
    }

    protected LinkPair getNext() {
        for (LinkPair link : pairs) {
            if (!link.checked) {
                return link;
            }
        }

        return null;
    }

    protected void checkString(String s, LinkPair link) {
        Matcher m = linkPattern.matcher(s);

        while (m.find()) {
            if (m.group(1) != null) {
                addUrl(m.group(1), link);
            }
            if (m.group(2) != null) {
                addUrl(m.group(2), link);
            }
        }
    }

    protected void addUrl(String url, LinkPair link) {
        if (url.length() == 0
            || url.indexOf("#") == 0
            || url.indexOf("mailto:") == 0
            || url.indexOf("tel:") == 0
        ) {
            return;
        }

        if (url.indexOf("//") == 0) {
            url = link.schema + ":" + url;
        }

        if (url.indexOf("/") == 0) {
            url = link.schema + "://" + link.host + url;
        }

        if (url.indexOf("?") == 0) {
            url = link.target + url;
        }

        if (url.indexOf("#") > 0) {
            url = url.substring(0, url.indexOf("#"));
        }

        try {
            LinkPair newLink = new LinkPair(url, link.target);
            if (domains.contains(newLink.host) && !pairs.contains(newLink)) {
                pairs.add(newLink);
            }
        } catch (MalformedURLException e) {
            System.err.println("error: broken URL \"" + url + "\" - referrer: " + link.target);
        }
    }
}
