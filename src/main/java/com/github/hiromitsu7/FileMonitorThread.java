package com.github.hiromitsu7;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

@ApplicationScoped
public class FileMonitorThread implements Runnable {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Getter
  private Map<String, String> labels = new HashMap<>();

  @Override
  public void run() {
    FileAlterationObserver observer = new FileAlterationObserver("/etc/podinfo");
    FileAlterationMonitor monitor = new FileAlterationMonitor(5000L);
    FileAlterationListener listener = new FileAlterationListenerAdaptor() {
      @Override
      public void onFileCreate(File file) {
        // code for processing creation event
      }

      @Override
      public void onFileDelete(File file) {
        // code for processing deletion event
      }

      @Override
      public void onFileChange(File file) {
        readLabels(file.toPath());
      }
    };
    observer.addListener(listener);
    monitor.addObserver(observer);
    try {
      monitor.start();
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
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
}
