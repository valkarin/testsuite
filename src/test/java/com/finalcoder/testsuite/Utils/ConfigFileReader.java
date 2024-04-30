package com.finalcoder.testsuite.Utils;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.ConfigurationUtils;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

@Getter
@Log4j2
public final class ConfigFileReader {

  private ImmutableConfiguration config;

  private ConfigFileReader(String file) {
    try {
      var params = new Parameters();
      var builder =
          new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
              .configure(params.fileBased().setFileName(file));
      this.config = ConfigurationUtils.unmodifiableConfiguration(builder.getConfiguration());
    } catch (ConfigurationException e) {
      log.error("There was an error while reading the configuration file", e);
      this.config = null;
    }
  }

  public static ConfigFileReader withFile(String file) {
    return new ConfigFileReader(file);
  }
}
