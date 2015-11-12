package models;

import java.util.*;

public class Tag {

    public static Map<String, Long> getCloud() {
        return Post._cloud("tags");
    }

    public static List<String> findAll() {
        return new ArrayList(Post._distinct("tags"));
    }

}