package com.hallo.fw.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author
 * @date
 */
public class _List {

    public static <T> boolean isEmpty(List<T> objects) {
        return objects == null || objects.size() == 0;
    }

    public static <T> List<T> create() {
        return new ArrayList<T>();
    }
}
