package info.morontt.link_checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class HttpFetcher {
    private static final String DEFAULT_USER_AGENT = "link-checker";

    ArrayList<String> domains;
    ArrayList<LinkPair> pairs;

    class LinkPair {
        String target;
        String referrer;
        boolean checked = false;
        int responseCode;

        LinkPair(String t, String r) {
            target = t;
            referrer = r;
        }

        LinkPair(String t) {
            target = t;
            referrer = "...";
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
            pairs.add(new LinkPair("http://" + domain));
        }
    }

    void fetch() {
        LinkPair link = getNext();
        link.checked = true;

        try {
            URL url = new URL(link.target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

            link.responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
            }
            in.close();
            conn.disconnect();

            System.out.println("success: " + link.responseCode + " " + link.target);
        } catch (MalformedURLException e) {
            System.err.println("error: broken URL " + link.target);
        } catch (IOException e) {
            System.err.println("error: " + link.responseCode + " " + link.target);
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
}
