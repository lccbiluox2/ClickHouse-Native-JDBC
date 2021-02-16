/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.housepower.protocol;

import com.github.housepower.misc.ByteBufHelper;
import com.github.housepower.serde.BinarySerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.sql.SQLException;

public interface Request extends ByteBufHelper, Encodable {

    ProtoType type();

    @Deprecated
    void writeImpl(BinarySerializer serializer) throws IOException, SQLException;

    @Deprecated
    default void writeTo(BinarySerializer serializer) throws IOException, SQLException {
        serializer.writeVarInt(type().id());
        this.writeImpl(serializer);
    }

    @Override
    default void encode(ByteBuf buf) {
        writeVarInt(buf, type().id());
        this.encode0(buf);
    }

    void encode0(ByteBuf buf);

    enum ProtoType {
        REQUEST_HELLO(0),
        REQUEST_QUERY(1),
        REQUEST_DATA(2),
        REQUEST_PING(4);

        private final int id;

        ProtoType(int id) {
            this.id = id;
        }

        public long id() {
            return id;
        }
    }
}