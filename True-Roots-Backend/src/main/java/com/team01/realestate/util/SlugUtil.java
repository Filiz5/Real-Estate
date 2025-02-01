package com.team01.realestate.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.function.Function;

public final class SlugUtil {

    private SlugUtil() {
    }

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String generateUniqueSlug(String input, Function<String, Boolean> isSlugExists) {
        String baseSlug = toSlug(input); // İlk olarak temel slug oluştur
        String uniqueSlug = baseSlug;
        int counter = 1;

        // Veritabanında aynı slug varsa yeni bir slug oluştur
        while (isSlugExists.apply(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter++;
        }

        return uniqueSlug;
    }
}
