/*
 * Sonar Gosu Plugin
 * Copyright (C) 2016-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.gosu.cobertura;

import com.google.common.annotations.VisibleForTesting;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.gosu.GosuPlugin;
import org.sonar.plugins.gosu.foundation.Gosu;
import org.sonar.plugins.gosu.foundation.GosuFileSystem;

import java.io.File;

public class CoberturaSensor implements Sensor {

  private static final Logger LOG = Loggers.get(CoberturaSensor.class);

  private final Settings settings;
  private final FileSystem fileSystem;
  private final GosuFileSystem gosuFileSystem;

  public CoberturaSensor(Settings settings, FileSystem fileSystem) {
    this.settings = settings;
    this.fileSystem = fileSystem;
    this.gosuFileSystem = new GosuFileSystem(fileSystem);
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.onlyOnLanguage(Gosu.KEY).name(this.toString());

  }

  @Override
  public void execute(SensorContext context) {
    if (shouldExecuteOnProject()) {
      analyse(context);
    }
  }

  @VisibleForTesting
  boolean shouldExecuteOnProject() {
    return gosuFileSystem.hasGosuFiles();
  }

  public void analyse(SensorContext context) {
    String reportPath = settings.getString(GosuPlugin.COBERTURA_REPORT_PATH);

    if (reportPath != null) {
      File xmlFile = new File(reportPath);
      if (!xmlFile.isAbsolute()) {
        xmlFile = new File(fileSystem.baseDir(), reportPath);
      }
      if (xmlFile.exists()) {
        LOG.info("Analyzing Cobertura report: " + reportPath);
        new CoberturaReportParser(context, fileSystem).parseReport(xmlFile);
      } else {
        LOG.info("Cobertura xml report not found: " + reportPath);
      }
    } else {
      LOG.info("No Cobertura report provided (see '" + GosuPlugin.COBERTURA_REPORT_PATH + "' property)");
    }
  }

  @Override
  public String toString() {
    return "Gosu CoberturaSensor";
  }

}
