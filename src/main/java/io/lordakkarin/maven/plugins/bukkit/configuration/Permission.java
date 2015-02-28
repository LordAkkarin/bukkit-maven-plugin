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
 * Represents a plugin permission.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class Permission {

	/**
	 * Defines the permission name.
	 */
	@Getter
	private String name;

	/**
	 * Defines the permission description.
	 */
	@Getter
	private String description;

	/**
	 * Defines the default value.
	 */
	@Getter
	private PermissionDefault defaultValue = PermissionDefault.OP;

	/**
	 * Defines the child permissions.
	 */
	@Getter
	private List<ChildPermission> children;

	/**
	 * Provides a list of available default values.
	 */
	public static enum PermissionDefault {
		TRUE ("true"),
		FALSE ("false"),
		OP ("op"),
		NOT_OP ("not op");

		/**
		 * Stores the mapped value.
		 */
		public final String mappedValue;

		/**
		 * Constructs a new PluginPermissionDefault.
		 * @param mappedValue The mapped value.
		 */
		private PermissionDefault (String mappedValue) {
			this.mappedValue = mappedValue;
		}
	}

	/**
	 * Represents a child permission.
	 */
	public static class ChildPermission {

		/**
		 * Defines the permission name.
		 */
		@Getter
		private String name;

		/**
		 * Defines whether the child permission inherits the parent value.
		 */
		@Getter
		private boolean inherit = false;
	}
}
