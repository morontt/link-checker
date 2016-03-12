package info.morontt.link_checker;

import java.util.ArrayList;

class HttpFetcher {
    public ArrayList<String> domains;

    public HttpFetcher(ArrayList<String> domains) {
        this.domains = domains;
    }

    public void fetch() {
        System.out.println("fetch");
    }
}
