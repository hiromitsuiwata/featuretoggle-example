package com.github.hiromitsu7;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private FileWatcher watcher;

  @Inject
  private FileMonitorThread monitor;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Map<String, String> labels1 = watcher.getLabels();
    Map<String, String> labels2 = monitor.getLabels();

    out.println("hello");
    out.println(labels1.toString());
    out.println(labels2.toString());
    out.flush();
    out.close();
  }
}
