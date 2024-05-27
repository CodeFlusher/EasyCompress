package me.codeflusher.easy_compress.data_save;

import me.codeflusher.easy_compress.util.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class PresetRegistry {
    static class PresetNotFoundException extends Exception{}
    private static final ArrayList<Preset> registeredPresets = new ArrayList<>();
    public static void register(Preset preset){
        Logger.message("Preset Registry", "Registering Preset");
        if(!registeredPresets.contains(preset))
            registeredPresets.add(preset);
    }

    public static Preset findInRegistry(String name) throws PresetNotFoundException {
        Optional<Preset> optionalPreset = registeredPresets.stream().filter(preset -> preset.getName().equals(name)).findFirst();
        if (optionalPreset.isPresent()){
            return optionalPreset.get();
        }
        throw new PresetNotFoundException();
    }
    public static ArrayList<Preset> getRegisteredPresets() {
        return registeredPresets;
    }
    public static ArrayList<String> getRegisteredPresetsNames() {
        return new ArrayList<>(){{
            ArrayList<Preset> presets = getRegisteredPresets();
            for (Preset preset : presets){
                add(preset.getName());
            }
        }};
    }
}
