Bukkit Maven Plugin
===================
The days of providing a ```plugin.yml``` are over! The simple Bukkit maven plugin takes care of all your needs
automatically by generating the plugin information automatically directly from your ```pom.xml```

**Note:** The plugin is not yet available in any repository. You will have to manually install it.

Usage
-----
```xml
<plugin>
	<groupId>io.github.lordakkarin.maven</groupId>
	<artifactId>bukkit-maven-plugin</artifactId>
	<extensions>true</extensions>

	<configuration>
		<plugin>
			<name>ToolboxCore</name>
			<description>Test</description>
			<version>1.1-SNAPSHOT</version>
			<website>http://example.org</website>

			<mainClass>rocks.spud.toolkit.core.ToolkitCorePlugin</mainClass>
			<loggingPrefix>spud</loggingPrefix>
			<loadState>STARTUP</loadState>
			<database>true</database>

			<authors>
				<author>Akkarin</author>
				<author>Spud</author>
			</authors>

			<dependencies>
				<dependency>SpudInstantSpawn</dependency>
			</dependencies>

			<softDependencies>
				<softDependency>SpudDoubleJump</softDependency>
			</softDependencies>

			<loadBefores>
				<loadBefore>WorldEdit</loadBefore>
			</loadBefores>

			<permissions>
				<permission>
					<name>spud.toolkit.core.test</name>
					<description>A test permission</description>
					<defaultValue>NOT_OP</defaultValue>
					<children>
						<child>
							<name>spud.toolkit.core.test.child</name>
							<inherit>true</inherit>
						</child>
					</children>
				</permission>
			</permissions>

			<commands>
				<command>
					<name>test</name>
					<description>A test command.</description>
					<permission>spud.toolkit.core.test</permission>

					<permissionMessage>U no permz dude!</permissionMessage>
					<usage>/test [test]</usage>

					<aliases>
						<alias>test2</alias>
					</aliases>
				</command>
			</commands>
		</plugin>
	</configuration>
</plugin>
```

Building
--------
You should know what to do with this: ```mvn clean install```

License
-------
```
Copyright [yyyy] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```