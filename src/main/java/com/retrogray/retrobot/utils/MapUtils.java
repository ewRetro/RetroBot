package com.retrogray.retrobot.utils;

import gnu.trove.impl.sync.*;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.*;
import gnu.trove.map.hash.*;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

public class MapUtils {
    private MapUtils() {}

    public static TLongSet newLongSet() {
        return new TSynchronizedLongSet(new TLongHashSet(), new Object());
    }

    public static TLongList newLongList() {
        return new TSynchronizedLongList(new TLongArrayList(), new Object());
    }

    public static TLongIntMap newLongIntMap() {
        return new TSynchronizedLongIntMap(new TLongIntHashMap(), new Object());
    }

    public static TLongLongMap newLongLongMap() {
        return new TSynchronizedLongLongMap(new TLongLongHashMap(), new Object());
    }

    public static <T> TLongObjectMap<T> newLongObjectMap() {
        return new TSynchronizedLongObjectMap<>(new TLongObjectHashMap<>(), new Object());
    }

    public static <T> TObjectLongMap<T> newObjectLongMap() {
        return new TSynchronizedObjectLongMap<>(new TObjectLongHashMap<>(), new Object());
    }

    public static <T> TObjectIntMap<T> newObjectIntMap() {
        return new TSynchronizedObjectIntMap<>(new TObjectIntHashMap<>(), new Object());
    }
}