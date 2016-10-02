package com.rysingdragon.hisys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class Config {

    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode node;

    public Config(Path config, HoconConfigurationLoader loader) {
        this.loader = loader;
        if (!Files.exists(config)) {
            try {
                Files.createDirectories(config.getParent());
                Files.createFile(config);
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            load();
        }
    }

    public void save() {
        try {
            loader.save(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommentedConfigurationNode getNode() {
        return this.node;
    }
}
