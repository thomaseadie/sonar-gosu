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
package org.sonar.plugins.gosu.foundation;

import com.google.common.collect.Lists;

import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;

import javax.annotation.CheckForNull;

import java.io.File;
import java.util.List;

public class GosuFileSystem {

  private final FileSystem fileSystem;
  private final FilePredicates predicates;
  private final FilePredicate isGosuLanguage;
  private final FilePredicate isMainTypeFile;

  public GosuFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    this.predicates = fileSystem.predicates();
    this.isGosuLanguage = predicates.hasLanguage(Gosu.KEY);
    this.isMainTypeFile = predicates.hasType(Type.MAIN);
  }

  public boolean hasGosuFiles() {
    return fileSystem.hasFiles(isGosuLanguage);
  }

  public List<File> sourceFiles() {
    Iterable<File> files = fileSystem.files(predicates.and(isGosuLanguage, isMainTypeFile));
    return Lists.newArrayList(files);
  }

  public List<InputFile> gosuInputFiles() {
    Iterable<InputFile> inputFiles = fileSystem.inputFiles(isGosuLanguage);
    return Lists.newArrayList(inputFiles);
  }

  public List<InputFile> sourceInputFiles() {
    Iterable<InputFile> inputFiles = fileSystem.inputFiles(predicates.and(isGosuLanguage, isMainTypeFile));
    return Lists.newArrayList(inputFiles);
  }

  @CheckForNull
  public InputFile sourceInputFileFromRelativePath(String relativePath) {
    return fileSystem.inputFile(predicates.and(predicates.matchesPathPattern("**/" + relativePath), isGosuLanguage, isMainTypeFile));
  }

}
