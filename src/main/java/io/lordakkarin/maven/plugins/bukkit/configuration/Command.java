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

import java.util.List;

/**
 * Represents a single command definition.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class Command {

	/**
	 * Defines the command name.
	 */
	@Getter
	private String name;

	/**
	 * Defines the plugin description.
	 */
	@Getter
	private String description;

	/**
	 * Defines a set of plugin aliases.
	 */
	@Getter
	private List<String> aliases;

	/**
	 * Defines a permission node for this plugin.
	 */
	@Getter
	private String permission;

	/**
	 * Defines a permission message to display.
	 */
	@Getter
	private String permissionMessage;

	/**
	 * Defines a usage message.
	 */
	@Getter
	private String usage;
}
