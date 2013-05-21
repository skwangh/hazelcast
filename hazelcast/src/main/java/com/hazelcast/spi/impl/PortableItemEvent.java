/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.spi.impl;

import com.hazelcast.core.ItemEventType;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;

/**
 * @ali 5/20/13
 */
public class PortableItemEvent implements Portable {

    private Data item;
    private ItemEventType eventType;
    private String uuid;

    public PortableItemEvent() {
    }

    public PortableItemEvent(Data item, ItemEventType eventType, String uuid) {
        this.item = item;
        this.eventType = eventType;
        this.uuid = uuid;
    }

    public Data getItem() {
        return item;
    }

    public ItemEventType getEventType() {
        return eventType;
    }

    public String getUuid() {
        return uuid;
    }

    public int getFactoryId() {
        return SpiPortableHook.ID;
    }

    public int getClassId() {
        return SpiPortableHook.ITEM_EVENT;
    }

    public void writePortable(PortableWriter writer) throws IOException {
        writer.writeInt("e", eventType.getType());
        writer.writeUTF("u", uuid);
        if (item == null){
            writer.writeInt("i", -1);
        } else {
            writer.writeInt("i", 1);
            item.writeData(writer.getRawDataOutput());
        }

    }

    public void readPortable(PortableReader reader) throws IOException {
        eventType = ItemEventType.getByType(reader.readInt("e"));
        uuid = reader.readUTF("u");
        if (reader.readInt("i") == 1){
            item = new Data();
            item.readData(reader.getRawDataInput());
        }
    }
}
