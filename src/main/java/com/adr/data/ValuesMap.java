/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.data;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author adrian
 */
public class ValuesMap implements Values {

    private final Map<String, ValuesEntry> entries;

    public ValuesMap(ValuesEntry... entries) {
        this(Stream.of(entries));
    }

    public ValuesMap(Collection<ValuesEntry> entries) {
        this(entries.stream());
    }

    public ValuesMap(Stream<ValuesEntry> entries) {
        this.entries = entries.collect(Collectors.toMap(ValuesEntry::getName, Function.identity()));
    }

    @Override
    public String[] getNames() {
        return entries.keySet().stream().toArray(String[]::new);
    }

    @Override
    public Object getValue(String name) {
        return entries.get(name).getValue();
    }

    @Override
    public Kind getKind(String name) {
        return entries.get(name).getKind();
    }

    public ValuesEntry[] getEntries() {
        return entries.values().stream().toArray(ValuesEntry[]::new);
    }
}
