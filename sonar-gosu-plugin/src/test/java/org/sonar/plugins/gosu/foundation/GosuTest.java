/*
 * Sonar Gosu Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
package org.sonar.plugins.gosu.foundation;

import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.gosu.GosuPlugin;

import static org.fest.assertions.Assertions.assertThat;

public class GosuTest {

  @Test
  public void test() {
    Settings settings = new Settings();
    Gosu language = new Gosu(settings);
    assertThat(language.getKey()).isEqualTo("gosu");
    assertThat(language.getName()).isEqualTo("Gosu");
    assertThat(language.getFileSuffixes()).isEqualTo(new String[] {".gs", ".gsx"});

    settings.setProperty(GosuPlugin.FILE_SUFFIXES_KEY, "");
    assertThat(language.getFileSuffixes()).containsOnly(".gs", ".gsx");

    settings.setProperty(GosuPlugin.FILE_SUFFIXES_KEY, ".gosu1, .gosu2");
    assertThat(language.getFileSuffixes()).containsOnly(".gosu1", ".gosu2");
  }

  @Test
  public void binaryDirectories() throws Exception {
    Settings settings = new Settings();
    Gosu language = new Gosu(settings);

    settings.setProperty(GosuPlugin.SONAR_GOSU_BINARIES, "");
    assertThat(language.getBinaryDirectories()).isEmpty();

    settings.setProperty(GosuPlugin.SONAR_GOSU_BINARIES, "target/firstDir , target/secondDir");
    assertThat(language.getBinaryDirectories()).hasSize(2);

    settings.setProperty(GosuPlugin.SONAR_GOSU_BINARIES, "");
    // property 'sonar.binaries' is set by maven and gradle plugins
    settings.setProperty(GosuPlugin.SONAR_GOSU_BINARIES_FALLBACK, "target/firstDir , target/secondDir");
    assertThat(language.getBinaryDirectories()).hasSize(2);
  }
}
