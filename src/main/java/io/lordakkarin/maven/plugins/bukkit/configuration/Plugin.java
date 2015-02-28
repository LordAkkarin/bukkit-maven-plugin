/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lordakkarin.maven.plugins.bukkit.configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the plugin configuration.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class Plugin {

	/**
	 * Defines the plugin author.
	 */
	@Getter
	@Setter
	private List<String> authors;

	/**
	 * Defines a list of plugin commands.
	 */
	@Getter
	private List<Command> commands;

	/**
	 * Defines whether the plugin will use a plugin database.
	 */
	@Getter
	@Setter
	private boolean database = false;

	/**
	 * Defines a set of dependencies.
	 */
	@Getter
	private List<String> dependencies;

	/**
	 * Defines the plugin description.
	 */
	@Getter
	@Setter
	private String description;
	/**
	 * Defines a list of plugins that should get a lower priority than the generated plugin.
	 */
	@Getter
	private List<String> loadBefores;

	/**
	 * Defines the plugin load state.
	 */
	@Getter
	@Setter
	private LoadState loadState = LoadState.POSTWORLD;

	/**
	 * Defines the logging prefix.
	 */
	@Getter
	@Setter
	private String loggingPrefix;

	/**
	 * Defines the main plugin class.
	 */
	@Getter
	@Setter
	private String mainClass;

	/**
	 * Defines the plugin name.
	 */
	@Getter
	@Setter
	private String name;

	/**
	 * Defines a list of plugin permissions.
	 */
	@Getter
	private List<Permission> permissions;

	/**
	 * Defines a set of soft dependencies.
	 */
	@Getter
	private List<String> softDependencies;

	/**
	 * Defines the plugin version.
	 */
	@Getter
	@Setter
	private String version;

	/**
	 * Defines the plugin website.
	 */
	@Getter
	@Setter
	private String website;
}
