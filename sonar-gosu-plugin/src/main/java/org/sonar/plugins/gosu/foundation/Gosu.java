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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.plugins.gosu.GosuPlugin;

import java.util.List;

public class Gosu extends AbstractLanguage {

  public static final String KEY = "gosu";

  /**
   * Settings of the plugin.
   */
  private final Settings settings;

  /**
   * Default constructor
   */
  public Gosu(Settings settings) {
    super(KEY, "Gosu");
    this.settings = settings;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.sonar.api.resources.AbstractLanguage#getFileSuffixes()
   */
  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = filterEmptyStrings(settings.getStringArray(GosuPlugin.FILE_SUFFIXES_KEY));
    if (suffixes.length == 0) {
      suffixes = StringUtils.split(GosuPlugin.DEFAULT_FILE_SUFFIXES, ",");
    }
    return addDot(suffixes);
  }

  private static String[] addDot(String[] suffixes) {
    String[] results = new String[suffixes.length];
    for (int i = 0; i < suffixes.length; i++) {
      String suffix = suffixes[i];
      String dot = suffix.startsWith(".") ? "" : ".";
      results[i] = dot + suffix;
    }
    return results;
  }

  private static String[] filterEmptyStrings(String[] stringArray) {
    List<String> nonEmptyStrings = Lists.newArrayList();
    for (String string : stringArray) {
      if (StringUtils.isNotBlank(string.trim())) {
        nonEmptyStrings.add(string.trim());
      }
    }
    return nonEmptyStrings.toArray(new String[nonEmptyStrings.size()]);
  }

  public String getCodeNarcReportPath() {
    return settings.getString(GosuPlugin.CODENARC_REPORT_PATH);
  }

  public List<String> getBinaryDirectories() {
    String[] binaries = settings.getStringArray(GosuPlugin.SONAR_GOSU_BINARIES);
    if (ArrayUtils.isEmpty(binaries)) {
      binaries = settings.getStringArray(GosuPlugin.SONAR_GOSU_BINARIES_FALLBACK);
    }
    return ImmutableList.copyOf(binaries);
  }
}
