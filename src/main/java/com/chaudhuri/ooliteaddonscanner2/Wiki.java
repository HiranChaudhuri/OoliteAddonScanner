/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class Wiki {
    private static final Logger log = LogManager.getLogger(Wiki.class);
    
    private static Map<String, String> wikiPageCache = new TreeMap<>();
    private static Set<String> wikiPageMisses = new TreeSet<>();

    /**
     * Runnable to perform Wiki checks in background threads.
     */
    public static class WikiCheck implements Runnable {
        
        private Wikiworthy ww;
        
        /**
         * Creates a new WikiCheck.
         * @param ww the object to check
         */
        public WikiCheck(Wikiworthy ww) {
            this.ww = ww;
        }

        /**
         * Performs the actual check, hopefully on a background thread.
         */
        @Override
        public void run() {
            checkWikiPage(ww);
        }
        
    }
    
    /**
     * Prevent this class from being instantiated.
     */
    private Wiki() {
    }
    
    /**
     * Returns the URL to a Wiki page with the given name.
     * 
     * @param name the name of the page
     * @return the url to the page
     */
    public static String getPageUrl(String name) {
        if (name == null) {
            throw new IllegalArgumentException("parameter must not be null");
        }
        
        final String base = "http://wiki.alioth.net/index.php/";
        /*
         * The URLEncoder uses too much of + escaping
         * so do not use 'base + URLEncoder.encode(name, Charset.forName("utf-8"));'
         */
        
        return base
                + name.replace(" ", "%20")
                        .replace("\"", "%22")
                        .replace("[", "%5B")
                        .replace("]", "%5D");
    }
    
    /**
     * Checks whether a wiki page exists.
     * The result will be added as warnings into the wikiworthy.
     * 
     * @param wikiworthy the wikiworty to check
     */
    public static void checkWikiPage(Wikiworthy wikiworthy) {
        log.debug("checkWikiPage({})", wikiworthy);

        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {    
            RequestBuilder builder = RequestBuilder.head(getPageUrl(wikiworthy.getName()));
            builder.setCharset(StandardCharsets.UTF_8);
            
            HttpUriRequest request = builder.build();
            try (final CloseableHttpResponse response = httpclient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    wikiworthy.setAsWikipage(request.getURI().toString());
                } else {
                    if (wikiworthy instanceof Expansion) {
                        wikiworthy.addWarning(String.format("%s -> %s %s", request.getURI(), response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));
                        
                        checkLowHangingFruit(wikiworthy, httpclient);
                    }
                }
            }
        }
        catch (Exception e) {
            wikiworthy.addWarning(String.format("Wiki check failed: %s: %s", e.getClass().getName(), e.getMessage()));
        }
    }
    
    private static void checkLowHangingFruit(Wikiworthy wikiworthy, CloseableHttpClient httpclient) {
        // check whether we have a low-hanging fruit for Expansions
        if (wikiworthy instanceof Expansion) {
            Expansion expansion = (Expansion)wikiworthy;

            if (String.valueOf(expansion.getInformationUrl()).contains("//wiki.alioth.net/index.php")) {

                RequestBuilder builder2 = RequestBuilder.head(expansion.getInformationUrl());
                builder2.setCharset(StandardCharsets.UTF_8);
                HttpUriRequest request2 = builder2.build();
                try (final CloseableHttpResponse response2 = httpclient.execute(request2)) {
                    if (response2.getStatusLine().getStatusCode() == 200) {
                        expansion.addWarning("Low hanging fuit: Information URL exists...");
                    } else {
                        expansion.addWarning("High hanging fruit");
                    }
                } catch (IOException e) {
                    log.warn("Could not check for low hanging fruit", e);
                }

            }
        }
    }
    
    /**
     * Checks whether a wiki page exists. This method speeds up by caching the results.
     * 
     * @param name name of the wiki page
     * @return url of the wiki page - if it exists. Null otherwise.
     */
    public static String wikiPageFor(String name) {
        log.debug("wikiPageFor({})", name);
        
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (wikiPageMisses.contains(name)) {
            // we already found out this page is missing
            return null;
        }
        String urlStr = wikiPageCache.get(name);
        if (urlStr != null) {
            // we already got a positive response
            return urlStr;
        }
        
        // no positive response cached, neither as hit nor fail
        urlStr = getPageUrl(name);
        
        try {
            URL url = new URL(urlStr);
            try (InputStreamReader rin = new InputStreamReader(url.openStream())) {
                StringBuilder sb = new StringBuilder();
                char[] buffer = new char[1024];
                int read = read = rin.read(buffer);
                while (read >= 0) {
                    sb.append(buffer, 0, read);
                    read = rin.read(buffer);
                }
                log.trace("wiki={}", sb);
                if (sb.toString().contains("There is currently no text in this page.")) {
                    // page does not exist, the wiki is offering to create that page
                    wikiPageMisses.add(name);
                    return null;
                } else {                
                    wikiPageCache.put(name, urlStr);
                    return urlStr;
                }
            }
            
        } catch (Exception e) {
            wikiPageMisses.add(name);
            log.trace("Cannot find url {}", urlStr, e);
            return null;
        }
        
    }
    
}
