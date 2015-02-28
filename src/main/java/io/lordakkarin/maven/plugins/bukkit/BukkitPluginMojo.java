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
package io.lordakkarin.maven.plugins.bukkit;

import io.lordakkarin.maven.plugins.bukkit.configuration.Plugin;
import io.lordakkarin.maven.plugins.bukkit.configuration.Command;
import io.lordakkarin.maven.plugins.bukkit.configuration.Permission;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a packaging type for Bukkit and Spigot plugins.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@Mojo (name = "bukkit-plugin", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class BukkitPluginMojo extends AbstractMojo {

	/**
	 * Defines the default excludes.
	 */
	private static final String[] DEFAULT_EXCLUDES = new String[] { "**/package.html" };

	/**
	 * Defines the default includes.
	 */
	private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };

	/**
	 * Stores the archive configuration.
	 */
	@Parameter
	private MavenArchiveConfiguration archive = new MavenArchiveConfiguration ();

	/**
	 * Defines the directory that contains class and resource directories.
	 */
	@Parameter (defaultValue = "${project.build.outputDirectory}", required = true)
	private File classesDirectory;

	/**
	 * Defines the path to the default manifest file.
	 */
	@Parameter (defaultValue = "${project.build.outputDirectory}/META-INF/MANIFEST.MF", required = true, readonly = true)
	private File defaultManifestFile;

	/**
	 * Defines a list of excluded files.
	 */
	@Parameter
	private String[] excludes;

	/**
	 * Defines the final name.
	 */
	@Parameter (alias = "jarName", property = "plugin.finalName", defaultValue = "${project.build.finalName}")
	private String finalName;

	/**
	 * Orders the plugin to re-generate the jar even if no changes were detected.
	 * Note: We're using a jar property here in case some other plugin sets it for us.
	 */
	@Parameter (property = "jar.forceCreation", defaultValue = "false")
	private boolean forceCreation;

	/**
	 * Defines a list of included files.
	 */
	@Parameter
	private String[] includes;

	/**
	 * Stores the active Jar Archiver implementation.
	 */
	@Component (role = Archiver.class, hint = "jar")
	private JarArchiver jarArchiver;

	/**
	 * Defines the output directory.
	 */
	@Parameter (defaultValue = "${project.build.directory}", required = true)
	private File outputDirectory;

	/**
	 * Defines the plugin configuration.
	 */
	@Parameter (defaultValue = "${classObject}")
	private Plugin plugin;

	/**
	 * Stores the maven project configuration.
	 */
	@Parameter (defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/**
	 * Stores the active maven session.
	 */
	@Parameter (defaultValue = "${session}", readonly = true, required = true)
	private MavenSession session;

	/**
	 * Orders the plugin to skip the generation if the resulting jar would be empty.
	 * Note: We're using the jar property here to make sure other plugins can talk to us.
	 */
	@Parameter (property = "jar.skipIfEmpty", defaultValue = "false")
	private boolean skipIfEmpty;
	/**
	 * Defines whether the default manifest file should be used.
	 */
	@Parameter (property = "plugin.useDefaultManifestFile", defaultValue = "false")
	private boolean useDefaultManifestFile;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute () throws MojoExecutionException, MojoFailureException {
		if (this.skipIfEmpty && (!this.classesDirectory.exists () || this.classesDirectory.list ().length == 0)) {
			this.getLog ().info ("Skipping packaging of the Bukkit plugin");
			return;
		}

		this.updateConfiguration ();
		this.verifyConfiguration ();

		File pluginFile = new File (this.outputDirectory, this.finalName + ".jar");

		MavenArchiver archiver = new MavenArchiver ();
		archiver.setArchiver (this.jarArchiver);
		archiver.setOutputFile (pluginFile);
		this.archive.setForced (this.forceCreation);

		try {
			File contentDirectory = this.classesDirectory;

			if (!contentDirectory.exists ())
				this.getLog ().warn ("Plugin JAR will be empty - no content was marked for inclusion!");
			else
				archiver.getArchiver ().addDirectory (contentDirectory, this.getIncludes (), this.getExcludes ());

			File existingManifest = this.defaultManifestFile;

			if (this.useDefaultManifestFile && existingManifest.exists () && this.archive.getManifestFile () != null) {
				this.getLog ().info (String.format ("Adding existing Manifest to archive: %s", existingManifest.getPath ()));
				this.archive.setManifestFile (existingManifest);
			}

			this.getLog ().info ("Generating and including plugin descriptor");
			File pluginDescriptor = new File (this.outputDirectory, "plugin.yml");

			this.generatePluginDescriptor (pluginDescriptor);
			archiver.getArchiver ().addFile (pluginDescriptor, "plugin.yml");

			archiver.createArchive (this.session, this.project, this.archive);
		} catch (Exception ex) {
			throw new MojoExecutionException ("Could not assemble plugin jar: " + ex.getMessage (), ex);
		}

		this.project.getArtifact ().setFile (pluginFile);
	}

	/**
	 * Generates a new plugin descriptor.
	 * @param pluginDescriptor The plugin descriptor.
	 * @throws java.io.IOException Thrown when writing the descriptor fails.
	 */
	private void generatePluginDescriptor (File pluginDescriptor) throws IOException {
		Map<String, Object> descriptorMap = new HashMap<> ();

		descriptorMap.put ("name", this.plugin.getName ());
		descriptorMap.put ("version", this.plugin.getVersion ());
		if (this.plugin.getDescription () != null) descriptorMap.put ("description", this.plugin.getDescription ());
		if (this.plugin.getAuthors () != null && this.plugin.getAuthors ().size () > 0) {
			if (this.plugin.getAuthors ().size () == 1)
				descriptorMap.put ("author", this.plugin.getAuthors ().get (0));
			else
				descriptorMap.put ("authors", this.plugin.getAuthors ());
		}
		if (this.plugin.getWebsite () != null) descriptorMap.put ("website", this.plugin.getWebsite ());

		descriptorMap.put ("main", this.plugin.getMainClass ());
		if (this.plugin.isDatabase ()) descriptorMap.put ("database", this.plugin.isDatabase ());
		if (this.plugin.getLoggingPrefix () != null) descriptorMap.put ("prefix", this.plugin.getLoggingPrefix ());

		if (this.plugin.getDependencies () != null) descriptorMap.put ("depend", this.plugin.getDependencies ());
		if (this.plugin.getSoftDependencies () != null) descriptorMap.put ("softdepend", this.plugin.getSoftDependencies ());
		if (this.plugin.getLoadBefores () != null) descriptorMap.put ("loadbefore", this.plugin.getLoadBefores ());

		if (this.plugin.getCommands () != null && this.plugin.getCommands ().size () > 0) {
			Map<String, Object> commandMap = new HashMap<> ();

			for (Command command : this.plugin.getCommands ()) {
				Map<String, Object> currentCommandMap = new HashMap<> ();

				if (command.getDescription () != null) currentCommandMap.put ("description", command.getDescription ());
				if (command.getAliases () != null && command.getAliases ().size () > 0) currentCommandMap.put ("aliases", command.getAliases ());
				if (command.getPermission () != null) currentCommandMap.put ("permission", command.getPermission ());
				if (command.getUsage () != null) currentCommandMap.put ("usage", command.getUsage ());

				commandMap.put (command.getName (), currentCommandMap);
			}

			descriptorMap.put ("commands", commandMap);
		}

		if (this.plugin.getPermissions () != null && this.plugin.getPermissions ().size () > 0) {
			Map<String, Object> permissionMap = new HashMap<> ();

			for (Permission permission : this.plugin.getPermissions ()) {
				Map<String, Object> currentPermissionMap = new HashMap<> ();

				if (permission.getDescription () != null) currentPermissionMap.put ("description", permission.getDescription ());
				if (permission.getDefaultValue () == Permission.PermissionDefault.OP) currentPermissionMap.put ("default", permission.getDefaultValue ().mappedValue);

				if (permission.getChildren () != null && permission.getChildren ().size () >= 0) {
					Map<String, Object> childrenMap = new HashMap<> ();

					for (Permission.ChildPermission childPermission : permission.getChildren ()) {
						childrenMap.put (childPermission.getName (), childPermission.isInherit ());
					}

					currentPermissionMap.put ("children", childrenMap);
				}

				permissionMap.put (permission.getName (), currentPermissionMap);
			}

			descriptorMap.put ("permissions", permissionMap);
		}

		// dump map
		FileWriter writer = null;

		try {
			writer = new FileWriter (pluginDescriptor);

			Yaml yaml = new Yaml (this.getDumperOptions ());
			yaml.dump (descriptorMap, writer);
		} finally {
			try { writer.close (); } catch (Exception ex) { }
		}
	}

	/**
	 * Returns the YAML dumper options.
	 * @return The options.
	 */
	private DumperOptions getDumperOptions () {
		DumperOptions options = new DumperOptions ();

		options.setIndent (4);
		options.setDefaultFlowStyle (DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow (true);

		return options;
	}

	/**
	 * Returns a list of defined excludes.
	 * @return The exclusion list.
	 */
	public String[] getExcludes () {
		if (this.excludes == null || this.excludes.length == 0) return DEFAULT_EXCLUDES;
		return this.excludes;
	}

	/**
	 * Returns a list of defined includes.
	 * @return The inclusion list.
	 */
	public String[] getIncludes () {
		if (this.includes == null || this.includes.length == 0) return DEFAULT_INCLUDES;
		return this.includes;
	}

	/**
	 * Updates the configuration.
	 */
	private void updateConfiguration () {
		if (this.plugin.getName () == null) this.plugin.setName (this.project.getName ());
		if (this.plugin.getDescription () == null) this.plugin.setDescription (this.project.getDescription ());
		if (this.plugin.getVersion () == null) this.plugin.setVersion (this.project.getVersion ());
		if (this.plugin.getWebsite () == null) this.plugin.setWebsite (this.project.getUrl ());
	}

	/**
	 * Verifies the configuration.
	 * @throws org.apache.maven.plugin.MojoExecutionException Occurs if the configuration is invalid.
	 */
	private void verifyConfiguration () throws MojoExecutionException {
		if (this.plugin.getName () == null || this.plugin.getName ().isEmpty ()) throw new MojoExecutionException ("Missing plugin property: name");
		if (this.plugin.getVersion () == null || this.plugin.getVersion ().isEmpty ()) throw new MojoExecutionException ("Missing plugin property: version");
		if (this.plugin.getMainClass () == null || this.plugin.getMainClass ().isEmpty ()) throw new MojoExecutionException ("Missing plugin property: mainClass");

		if (this.plugin.getCommands () != null && this.plugin.getCommands ().size () != 0) {
			for (Command command : this.plugin.getCommands ()) {
				if (command.getName () == null || command.getName ().isEmpty ()) throw new MojoExecutionException ("Missing command property: name");
			}
		}

		if (this.plugin.getPermissions () != null && this.plugin.getPermissions ().size () != 0) {
			for (Permission permission : this.plugin.getPermissions ()) {
				if (permission.getName () == null || permission.getName ().isEmpty ()) throw new MojoExecutionException ("Missing permission property: name");
			}
		}
	}
}
