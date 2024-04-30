package com.finalcoder.testsuite.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AllureUtils {
  private static final String projectPath = System.getProperty("user.dir");
  private static final Path allureHome = Paths.get(projectPath, "/allure/");
  private static final Path allureReport = Paths.get(projectPath, "/target/allure-report");
  private static final Path resultsPath = Paths.get(projectPath, "/target/allure-results");
  private static final String allureExecutable =
      System.getProperty("os.name").toLowerCase().contains("win") ? "allure.bat" : "allure";
  private static final Path executeablePath = allureHome.resolve("bin").resolve(allureExecutable);
  private static final int defaultTimeout = 3600;

  @SneakyThrows
  public static void GenerateSingleFileReport() {
    if (StringUtils.isBlank(projectPath)) return;
    final CommandLine commandLine = getSingleFileReportCommandLine();
    execute(commandLine);
  }

  public static void GenerateReport() {
    if (StringUtils.isBlank(projectPath)) return;
    final CommandLine commandLine = getReportCommandLine();
  }

  private static CommandLine getCommandLine() {
    return new CommandLine(AllureUtils.executeablePath.toAbsolutePath().toFile());
  }

  private static CommandLine getReportCommandLine() {
    final CommandLine commandLine = getCommandLine();

    commandLine.addArgument("generate");
    commandLine.addArgument("--clean");
    commandLine.addArgument(AllureUtils.resultsPath.toAbsolutePath().toString(), true);
    commandLine.addArgument("-o");
    commandLine.addArgument(AllureUtils.allureReport.toAbsolutePath().toString(), true);
    return commandLine;
  }

  private static CommandLine getSingleFileReportCommandLine() {
    final CommandLine commandLine = getCommandLine();

    commandLine.addArgument("generate");
    commandLine.addArgument("--clean");
    commandLine.addArgument("--single-file");
    commandLine.addArgument(AllureUtils.resultsPath.toAbsolutePath().toString(), true);
    commandLine.addArgument("-o");
    commandLine.addArgument(AllureUtils.allureReport.toAbsolutePath().toString(), true);
    return commandLine;
  }

  private static void execute(final CommandLine commandLine) throws IOException {
    final DefaultExecutor executor = new DefaultExecutor();
    final ExecuteWatchdog watchdog = new ExecuteWatchdog(AllureUtils.defaultTimeout);
    executor.setWatchdog(watchdog);
    executor.setExitValue(0);
    executor.execute(commandLine);
  }
}
