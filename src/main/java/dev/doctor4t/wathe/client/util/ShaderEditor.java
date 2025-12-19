package dev.doctor4t.wathe.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Environment(EnvType.CLIENT)
public class ShaderEditor {
    private final List<String> lines;

    public ShaderEditor(String shaderSource) {
        this.lines = new ArrayList<>(Arrays.asList(shaderSource.split("\n")));
    }

    public ShaderEditor addAfter(String searchString, String newLine) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(searchString)) {
                lines.add(i + 1, newLine);
                break;
            }
        }
        return this;
    }

    public ShaderEditor addBefore(String searchString, String newLine) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(searchString)) {
                lines.add(i, newLine);
                break;
            }
        }
        return this;
    }

    public ShaderEditor addUniform(String uniformDeclaration) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().startsWith("uniform ")) {
                lines.add(i, uniformDeclaration);
                break;
            }
        }
        return this;
    }

    public String build() {
        return String.join("\n", lines);
    }
}
