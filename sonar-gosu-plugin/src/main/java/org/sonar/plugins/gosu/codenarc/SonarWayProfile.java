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
package org.sonar.plugins.gosu.codenarc;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;
import org.sonar.api.SonarRuntime;


import static org.sonar.plugins.gosu.codenarc.CodeNarcRulesDefinition.*;
//import org.sonar.api.utils.ValidationMessages;

/**
 * Sonar way profile for the Gosu language
 */
public final class SonarWayProfile implements BuiltInQualityProfilesDefinition {

  private static final String NAME = "Sonar way";
  public static final String JSON_PROFILE_PATH = RESOURCE_BASE_PATH + "/Sonar_way_profile.json";


  private final SonarRuntime sonarRuntime;

  public SonarWayProfile(SonarRuntime sonarRuntime) {
    this.sonarRuntime = sonarRuntime;
  }

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(NAME, LANGUAGE_KEY);
    BuiltInQualityProfileJsonLoader.load(profile, REPOSITORY_KEY, JSON_PROFILE_PATH, RESOURCE_BASE_PATH, sonarRuntime);
    profile.done();
  }

}