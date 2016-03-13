package info.morontt.link_checker;

import java.util.ArrayList;

class HttpFetcher {
    ArrayList<String> domains;
    ArrayList<LinkPair> pairs;

    class LinkPair {
        String target;
        String referrer;
        boolean checked = false;

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
            pairs.add(new LinkPair(domain));
        }
    }

    void fetch() {
        LinkPair link = getNext();
        link.checked = true;

        System.out.println("fetch: " + link.target);
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
