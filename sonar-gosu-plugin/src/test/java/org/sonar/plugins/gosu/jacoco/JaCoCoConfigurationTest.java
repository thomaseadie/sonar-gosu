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
package org.sonar.plugins.gosu.jacoco;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.plugins.gosu.foundation.Gosu;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class JaCoCoConfigurationTest {

  private Settings settings;
  private JaCoCoConfiguration jacocoSettings;
  private DefaultFileSystem fileSystem;

  @Before
  public void setUp() {
    settings = new MapSettings(new PropertyDefinitions().addComponents(JaCoCoConfiguration.getPropertyDefinitions()));
    fileSystem = new DefaultFileSystem(new File("."));
    jacocoSettings = new JaCoCoConfiguration(settings, fileSystem);
  }

  @Test
  public void shouldExecuteOnProject() throws Exception {
    // no files
    assertThat(jacocoSettings.shouldExecuteOnProject(true)).isFalse();
    assertThat(jacocoSettings.shouldExecuteOnProject(false)).isFalse();

    fileSystem.add(new TestInputFileBuilder("", "src/foo/bar.java").setLanguage("java").build());
    assertThat(jacocoSettings.shouldExecuteOnProject(true)).isFalse();
    assertThat(jacocoSettings.shouldExecuteOnProject(false)).isFalse();

    fileSystem.add(new TestInputFileBuilder("", "src/foo/bar.gosu").setLanguage(Gosu.KEY).build());
    assertThat(jacocoSettings.shouldExecuteOnProject(true)).isTrue();
    assertThat(jacocoSettings.shouldExecuteOnProject(false)).isFalse();

    settings.setProperty(JaCoCoConfiguration.REPORT_MISSING_FORCE_ZERO, true);
    assertThat(jacocoSettings.shouldExecuteOnProject(true)).isTrue();
    assertThat(jacocoSettings.shouldExecuteOnProject(false)).isTrue();
  }

  @Test
  public void defaults() {
    assertThat(jacocoSettings.getReportPath()).isEqualTo("target/jacoco.exec");
    assertThat(jacocoSettings.getItReportPath()).isEqualTo("target/jacoco-it.exec");
  }

  @Test
  public void shouldReturnItReportPathWhenModified() {
    settings.setProperty(JaCoCoConfiguration.IT_REPORT_PATH_PROPERTY, "target/it-jacoco-test.exec");
    assertThat(jacocoSettings.getItReportPath()).isEqualTo("target/it-jacoco-test.exec");
  }

  @Test
  public void shouldReturnReportPathWhenModified() {
    settings.setProperty(JaCoCoConfiguration.REPORT_PATH_PROPERTY, "jacoco.exec");
    assertThat(jacocoSettings.getReportPath()).isEqualTo("jacoco.exec");
  }
}
