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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class GosuFileSystemTest {

  private DefaultFileSystem fileSystem;
  private GosuFileSystem gosuFileSystem;

  @Before
  public void setUp() {
    fileSystem = new DefaultFileSystem(new File("."));
    gosuFileSystem = new GosuFileSystem(fileSystem);
  }

  @Test
  public void isEnabled() {
    assertThat(gosuFileSystem.hasGosuFiles()).isFalse();

    fileSystem.add(new DefaultInputFile("", "fake.file"));
    assertThat(gosuFileSystem.hasGosuFiles()).isFalse();

    fileSystem.add(new DefaultInputFile("", "fake.gosu").setLanguage(Gosu.KEY));
    assertThat(gosuFileSystem.hasGosuFiles()).isTrue();
  }

  @Test
  public void getSourceFile() {
    assertThat(gosuFileSystem.sourceFiles()).isEmpty();

    fileSystem.add(new DefaultInputFile("", "fake.file"));
    assertThat(gosuFileSystem.sourceFiles()).isEmpty();

    fileSystem.add(new DefaultInputFile("", "fake.gosu").setLanguage(Gosu.KEY));
    assertThat(gosuFileSystem.sourceFiles()).hasSize(1);
  }

  @Test
  public void inputFileFromRelativePath() {
    assertThat(gosuFileSystem.sourceInputFileFromRelativePath(null)).isNull();

    fileSystem.add(new DefaultInputFile("", "fake1.file"));
    assertThat(gosuFileSystem.sourceInputFileFromRelativePath("fake1.file")).isNull();

    fileSystem.add(new DefaultInputFile("", "fake2.file").setType(Type.MAIN).setLanguage(Gosu.KEY));
    assertThat(gosuFileSystem.sourceInputFileFromRelativePath("fake2.file")).isNotNull();

    fileSystem.add(new DefaultInputFile("", "org/sample/foo/fake3.file").setType(Type.MAIN).setLanguage(Gosu.KEY));
    assertThat(gosuFileSystem.sourceInputFileFromRelativePath("foo/fake3.file")).isNotNull();
  }
}
