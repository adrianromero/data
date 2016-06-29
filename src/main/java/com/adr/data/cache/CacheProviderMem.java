//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.
package com.adr.data.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 *
 * @author adrian
 */
public class CacheProviderMem implements CacheProvider {
    
    private final Cache<String, String> cache;

    public CacheProviderMem() {
        cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build();
    }
    
    @Override
    public void put(String key, String value) {
        cache.put(key, value);
    }

    @Override
    public String getIfPresent(String key) {
        return cache.getIfPresent(key);
    } 
}