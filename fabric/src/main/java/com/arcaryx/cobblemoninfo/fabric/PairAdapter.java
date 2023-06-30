package com.arcaryx.cobblemoninfo.fabric;

import com.arcaryx.cobblemoninfo.config.ShowType;
import com.arcaryx.cobblemoninfo.waila.TooltipType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class PairAdapter extends TypeAdapter<Pair<TooltipType, ShowType>> {
    @Override
    public void write(JsonWriter out, Pair<TooltipType, ShowType> value) throws IOException {
        out.beginObject();
        out.name("tooltip").value(value.getLeft().name());
        out.name("showType").value(value.getRight().name());
        out.endObject();
    }

    @Override
    public Pair<TooltipType, ShowType> read(JsonReader in) throws IOException {
        in.beginObject();
        TooltipType left = null;
        ShowType right = null;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "tooltip" -> left = TooltipType.valueOf(in.nextString());
                case "showType" -> right = ShowType.valueOf(in.nextString());
            }
        }
        in.endObject();
        return Pair.of(left, right);
    }
}
