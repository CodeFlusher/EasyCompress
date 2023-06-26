package me.urbanfaust.easy_compress.data_save;

import java.util.ArrayList;
import java.util.Optional;

public class PresetRegistry {
    static class PresetNotFoundException extends Exception{}
    private static ArrayList<Preset> registeredPresets = new ArrayList<>();
    public static void register(Preset preset){
        System.out.println("Register");
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

    public static void registerFromFile(String filePath){
        register(new SettingsManager().loadPreset(filePath));
    }
}
