package com.github.hiromitsu7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletListener implements ServletContextListener {

  @Inject
  private FileWatcherThread watcher;
  @Inject
  private FileMonitorThread monitor;

  private ExecutorService exec;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    exec = Executors.newFixedThreadPool(2);
    exec.execute(watcher);
    exec.execute(monitor);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    exec.shutdown();
  }
}
