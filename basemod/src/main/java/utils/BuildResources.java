package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import info.ModInfo;
import utils.quickitem.QuickItem;

public class BuildResources {
	
	public static final String TEMPLATES_DIR = "utils/templates/";
	public static final String META_INF_DIR = "src/main/resources/META-INF/";
	public static final String ASSETS_DIR = "assets/";

	public static void main(String[] args) throws IOException {

		QuickItem.generateResources();
		
		boolean errors = false;
		
		
		if (!QuickItem.generateErrors.isEmpty()) {
			errors = true;
		}
		
		if (errors) {
			System.err.println("\n\n\n\n");
			System.err.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			
			System.err.println("We tried to diagnose, here is what we found: ");
			
			

			System.err.println();
			for(String error : QuickItem.generateErrors) {
				System.err.println(error);
			}
			System.err.println();
			System.err.println(
				"  ______ _____  _____   ____  _____  \n" + 
				" |  ____|  __ \\|  __ \\ / __ \\|  __ \\ \n" + 
				" | |__  | |__) | |__) | |  | | |__) |\n" + 
				" |  __| |  _  /|  _  /| |  | |  _  / \n" + 
				" | |____| | \\ \\| | \\ \\| |__| | | \\ \\ \n" + 
				" |______|_|  \\_\\_|  \\_\\\\____/|_|  \\_\\\n"
				);
		System.err.println("Scroll up to see errors!");
			System.err.println();
			System.err.println("Press Enter to Continue.");
			System.err.println();
			System.err.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			
			Scanner s = new Scanner(System.in);

			s.nextLine();
			s.close();
			System.exit(1);
		}

		Path descFile = Paths.get(".").resolve(ASSETS_DIR + "description.txt");
		List<String> descLines = Files.readAllLines(descFile);
		String description = descLines.stream().collect(Collectors.joining("\n"));
		
		Path templateFile = Paths.get(".").resolve(TEMPLATES_DIR + "mods.toml.template");
		List<String> lines = Files.readAllLines(templateFile);
		StringBuilder newLines = new StringBuilder();
		for(String line : lines) {
			String newLine = line.replace("{{MOD_NAME}}", ModInfo.MOD_NAME);
			newLine = newLine.replace("{{AUTHORS}}", ModInfo.AUTHORS);
			newLine = newLine.replace("{{DESCRIPTION}}", description);
			newLines.append(newLine + '\n');
		}
		
		Path infoFile = Paths.get(".").resolve(META_INF_DIR + "mods.toml");
		Files.write(infoFile, newLines.toString().getBytes(), StandardOpenOption.CREATE);
		
	}

}
