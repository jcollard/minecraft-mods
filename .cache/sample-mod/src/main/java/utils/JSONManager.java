package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import utils.quickitem.QuickItem;

public class JSONManager {

	public static final String assetsDir = "src/main/resources/assets/basemod/";
	public static final String templatesDir = "utils/templates/";
	public static final String texturesDir = "assets/textures/";
	
	public static String generateItem(String textureName, String safeName, QuickItem item) {
		if(textureName == "") {
			QuickItem.generateErrors.add("The 'texture' variable is not set for the " + item.getClass().getSimpleName() + " class.");
			return "Failed!";
		}
		
		Path templateFile = Paths.get(".").resolve(templatesDir + "item.template");
		List<String> lines;
		try {
			lines = Files.readAllLines(templateFile);
		} catch (IOException e) {
			e.printStackTrace();
			QuickItem.generateErrors.add("Could not locate the QuickItem template file!");
			return "Failed";
		}
		
		StringBuilder newLines = new StringBuilder();
		for(String line : lines) {
			String newLine = line.replace("{{TEXTURE_NAME}}", textureName);
			newLine = newLine.replace("{{PARENT}}", item.parentModel);
			newLines.append(newLine + '\n');
		}
		
		Path textureFile = Paths.get(".").resolve(texturesDir + textureName + ".png");
		Path targetTexturePath = Paths.get(".").resolve(assetsDir + "textures/items/" + textureName + ".png");
		try {
			Files.deleteIfExists(targetTexturePath);
			Files.copy(textureFile, targetTexturePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			QuickItem.generateErrors.add("Could not find '" + textureName + ".png'");
			return "Failed";
		}
		
		
		Path targetJSONPath = Paths.get(".").resolve(assetsDir + "models/item/" + safeName + ".json");
		try {
			Files.deleteIfExists(targetJSONPath);
			Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
			QuickItem.generateErrors.add("Could not generate '" + safeName + ".json'.");
			return "Failed!";
		}
		
		return newLines.toString();
	}
	
	public static String generateLangFile(Map<String, String> items) throws IOException {
		List<String> itemEntries = new LinkedList<>();
		for(Entry<String, String> item : items.entrySet()) {
			String left = "\"item.basemod." + item.getKey() + "\"";
			String right = "\"" + item.getValue() + "\"";
			itemEntries.add(left + ": " + right);
		}
		String itemEntry = itemEntries.stream().collect(Collectors.joining(","));
		
		Path templateFile = Paths.get(".").resolve(templatesDir + "lang.template");
		List<String> lines = Files.readAllLines(templateFile);
		StringBuilder newLines = new StringBuilder();
		for(String line : lines) {
			String newLine = line.replace("{{ITEM_ENTRY}}", itemEntry);
			newLines.append(newLine + '\n');
		}
		
		Path targetJSONPath = Paths.get(".").resolve(assetsDir + "lang/en_us.json");
		Files.deleteIfExists(targetJSONPath);
		Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);
		
		return newLines.toString();
	}
	
}