package com.github.hiromitsu7;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

@ApplicationScoped
public class FileWatcher {

  private static final String PATH = "/etc/podinfo/";
  private WatchService watcher;
  private boolean started = false;
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Getter
  private Map<String, String> labels = new HashMap<>();

  public void start() {

    readLabels(Paths.get(PATH, "labels"));

    try {
      watcher = FileSystems.getDefault().newWatchService();
      Path dir = Paths.get(PATH);
      WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
      started = true;

      while (started) {
        try {
          key = watcher.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          logger.error("msg: ", e);
        }
        if (key != null) {
          for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> watchEventKind = event.kind();
            if (watchEventKind == StandardWatchEventKinds.OVERFLOW) {
              continue;
            }
            if (watchEventKind == StandardWatchEventKinds.ENTRY_CREATE) {
              logger.info("File Created: " + event.context());
            }
            if (watchEventKind == StandardWatchEventKinds.ENTRY_MODIFY) {
              String context = event.context().toString();
              if (context.equals("labels")) {
                Path path = Paths.get(PATH, context);
                logger.info("File Modified: " + path);
                readLabels(path);
              }
            }
            if (watchEventKind == StandardWatchEventKinds.ENTRY_DELETE) {
              logger.info("File Delete: " + event.context());
            }
            key.reset();
          }
        }
      }
    } catch (IOException e) {
      logger.error("msg: ", e);
    }
  }

  private void readLabels(Path path) {
    try {
      List<String> lines = Files.readAllLines(path);
      logger.info("lines: " + lines);
      labels = new HashMap<>();
      for (String line : lines) {
        if (line.contains("=")) {
          String[] split = line.split("=");
          labels.put(split[0], split[1]);
        }
      }
    } catch (IOException e) {
      logger.error("msg: ", e);
    }
  }

  public void stop() {
    started = false;
    try {
      if (watcher != null) {
        watcher.close();
      }
    } catch (IOException e) {
      logger.error("msg: ", e);
    }
  }
}
