package mekanism.common.network.to_client.container.property.list;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import mekanism.common.network.BasePacketHandler;
import net.minecraft.network.FriendlyByteBuf;

public class StringListPropertyData extends ListPropertyData<String> {

    public StringListPropertyData(short property, @Nonnull List<String> values) {
        super(property, ListType.STRING, values);
    }

    public static StringListPropertyData read(short property, int elements, FriendlyByteBuf buffer) {
        List<String> values = new ArrayList<>(elements);
        for (int i = 0; i < elements; i++) {
            values.add(BasePacketHandler.readString(buffer));
        }
        return new StringListPropertyData(property, values);
    }

    @Override
    protected void writeListElement(FriendlyByteBuf buffer, String value) {
        buffer.writeUtf(value);
    }
}