package com.example.istream.utils;

import android.net.Uri;

public class YouTubeUtils {

    public static String extractVideoId(String url) {
        if (url == null || url.trim().isEmpty()) return null;

        try {
            Uri uri = Uri.parse(url.trim());
            String host = uri.getHost();

            if (host == null) return null;
            host = host.toLowerCase();

            if (host.contains("youtu.be")) {
                String path = uri.getPath();
                if (path != null && path.length() > 1) {
                    String id = path.substring(1).split("/")[0];
                    return id.length() == 11 ? id : null;
                }
            }

            String v = uri.getQueryParameter("v");
            if (v != null) {
                return v.length() == 11 ? v : null;
            }

            String path = uri.getPath();
            if (path != null) {
                String[] parts = path.split("/");
                if ((path.startsWith("/shorts/") || path.startsWith("/embed/")) && parts.length >= 3) {
                    String id = parts[2];
                    return id.length() == 11 ? id : null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isValidYouTubeUrl(String url) {
        String id = extractVideoId(url);
        return id != null && id.length() == 11;
    }
}