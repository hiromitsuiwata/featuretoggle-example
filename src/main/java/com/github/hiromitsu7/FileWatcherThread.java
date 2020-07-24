package com.github.hiromitsu7;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FileWatcherThread implements Runnable {

  @Inject
  private FileWatcher watcher;

  @Override
  public void run() {
    watcher.start();
  }

}
