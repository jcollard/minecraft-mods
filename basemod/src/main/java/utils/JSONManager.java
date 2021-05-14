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

import utils.quickarmor.QuickArmor;
import utils.quickblock.QuickBlock;
import utils.quickitem.QuickItem;

public class JSONManager {

	public static final String assetsDir = "src/main/resources/assets/basemod/";
	public static final String templatesDir = "utils/templates/";
	public static final String texturesDir = "assets/textures/";

	public static String generateItem(String textureName, String safeName, QuickItem item) {
		if (textureName == "") {
			QuickItem.generateErrors
					.add("The 'texture' variable is not set for the " + item.getClass().getSimpleName() + " class.");
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
		for (String line : lines) {
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

	public static String generateBlock(String textureName, String safeName, QuickBlock block) {
		if (textureName == "") {
			QuickBlock.generateErrors
					.add("The 'texture' variable is not set for the " + block.getClass().getSimpleName() + " class.");
			return "Failed!";
		}

		// Write Block Texture

		Path textureFile = Paths.get(".").resolve(texturesDir + textureName + ".png");
		Path targetTexturePath = Paths.get(".").resolve(assetsDir + "textures/blocks/" + textureName + ".png");
		try {
			Files.deleteIfExists(targetTexturePath);
			Files.copy(textureFile, targetTexturePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			QuickBlock.generateErrors.add("Could not find '" + textureName + ".png'");
			return "Failed";
		}

		// Write Block Model JSON
		{

			Path templateFile = Paths.get(".").resolve(templatesDir + "block.template");
			List<String> lines;
			try {
				lines = Files.readAllLines(templateFile);
			} catch (IOException e) {
				e.printStackTrace();
				QuickBlock.generateErrors.add("Could not locate the QuickBlock template file!");
				return "Failed";
			}

			StringBuilder newLines = new StringBuilder();
			for (String line : lines) {
				String newLine = line.replace("{{TEXTURE_NAME}}", textureName);
				newLine = newLine.replace("{{PARENT}}", block.parentModel);
				newLines.append(newLine + '\n');
			}

			Path targetJSONPath = Paths.get(".").resolve(assetsDir + "models/block/" + safeName + ".json");
			try {
				Files.deleteIfExists(targetJSONPath);
				Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);
			} catch (IOException e) {
				e.printStackTrace();
				QuickBlock.generateErrors.add("Could not generate '" + safeName + ".json'.");
				return "Failed!";
			}
		}

		// Write Block State JSON

		{

			Path templateFile = Paths.get(".").resolve(templatesDir + "block_state.template");
			List<String> lines;
			try {
				lines = Files.readAllLines(templateFile);
			} catch (IOException e) {
				e.printStackTrace();
				QuickBlock.generateErrors.add("Could not locate the QuickBlock template file!");
				return "Failed";
			}

			StringBuilder newLines = new StringBuilder();
			for (String line : lines) {
				String newLine = line.replace("{{BLOCK_NAME}}", safeName);
				newLines.append(newLine + '\n');
			}

			Path targetJSONPath = Paths.get(".").resolve(assetsDir + "blockstates/" + safeName + ".json");
			try {
				Files.deleteIfExists(targetJSONPath);
				Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);
			} catch (IOException e) {
				e.printStackTrace();
				QuickBlock.generateErrors.add("Could not generate '" + safeName + ".json'.");
				return "Failed!";
			}
		}

		// Write Block Item JSON
		
		{

			Path templateFile = Paths.get(".").resolve(templatesDir + "block_item.template");
			List<String> lines;
			try {
				lines = Files.readAllLines(templateFile);
			} catch (IOException e) {
				e.printStackTrace();
				QuickBlock.generateErrors.add("Could not locate the QuickBlock template file!");
				return "Failed";
			}

			StringBuilder newLines = new StringBuilder();
			for (String line : lines) {
				String newLine = line.replace("{{BLOCK_NAME}}", safeName);
				newLines.append(newLine + '\n');
			}

			Path targetJSONPath = Paths.get(".").resolve(assetsDir + "models/item/" + safeName + ".json");
			try {
				Files.deleteIfExists(targetJSONPath);
				Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);
			} catch (IOException e) {
				e.printStackTrace();
				QuickBlock.generateErrors.add("Could not generate '" + safeName + ".json'.");
				return "Failed!";
			}
		}

		return "Success!";
	}
	
	public static String generateArmor(QuickArmor armor) {
		List<String> errors = armor.getErrors();
		if(!errors.isEmpty()) {
			QuickArmor.generateErrors.addAll(errors);
			return "Failed.";
		}

		Path templateFile = Paths.get(".").resolve(templatesDir + "item.template");
		List<String> lines;
		try {
			lines = Files.readAllLines(templateFile);
		} catch (IOException e) {
			e.printStackTrace();
			QuickArmor.generateErrors.add("Could not locate the QuickItem template file!");
			return "Failed";
		}
		
		List<String[]> itemEntries = new LinkedList<>();
		itemEntries.add(new String[] {armor.getTextureChest(), armor.getSafeRegistryName() + "_chestplate"});
		itemEntries.add(new String[] {armor.getTextureHead(), armor.getSafeRegistryName() + "_helmet"});
		itemEntries.add(new String[] {armor.getTextureFeet(), armor.getSafeRegistryName() + "_boots"});
		itemEntries.add(new String[] {armor.getTextureLegs(), armor.getSafeRegistryName() + "_leggings"});

		
		//Create each item (chest, head, feet, legs)
		for (String[] entry : itemEntries) {
			String textureName = entry[0];
			String safeName = entry[1];
			StringBuilder newLines = new StringBuilder();
			for (String line : lines) {
				String newLine = line.replace("{{TEXTURE_NAME}}", textureName);
				newLine = newLine.replace("{{PARENT}}", armor.getParentModel());
				newLines.append(newLine + '\n');
			}
	
			Path textureFile = Paths.get(".").resolve(texturesDir + textureName + ".png");
			Path targetTexturePath = Paths.get(".").resolve(assetsDir + "textures/items/" + textureName + ".png");
			try {
				Files.deleteIfExists(targetTexturePath);
				Files.copy(textureFile, targetTexturePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				QuickArmor.generateErrors.add("Could not find '" + textureName + ".png'");
				return "Failed";
			}

			Path targetJSONPath = Paths.get(".").resolve(assetsDir + "models/item/" + safeName + ".json");
			try {
				Files.deleteIfExists(targetJSONPath);
				Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);
			} catch (IOException e) {
				e.printStackTrace();
				QuickArmor.generateErrors.add("Could not generate '" + safeName + ".json'.");
				return "Failed!";
			}
		}
		
		// Create the Armor Model Texture
		
		Path textureFile = Paths.get(".").resolve(texturesDir + armor.getTexture() + "_layer_1.png");
		Path targetTexturePath = Paths.get(".").resolve(assetsDir + "textures/models/armor/" + armor.getSafeRegistryName() + "_layer_1.png");
		try {
			Files.deleteIfExists(targetTexturePath);
			Files.copy(textureFile, targetTexturePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			QuickArmor.generateErrors.add("Could not find '" + armor.getTexture() + "_layer_1.png'");
			return "Failed";
		}
		
		textureFile = Paths.get(".").resolve(texturesDir + armor.getTexture() + "_layer_2.png");
		targetTexturePath = Paths.get(".").resolve(assetsDir + "textures/models/armor/" + armor.getSafeRegistryName() + "_layer_2.png");
		try {
			Files.deleteIfExists(targetTexturePath);
			Files.copy(textureFile, targetTexturePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			QuickArmor.generateErrors.add("Could not find '" + armor.getTexture() + "_layer_2.png'");
			return "Failed";
		}

		return null; //newLines.toString();
	}

	private static List<String> generateEntries(Map<String, String> items, String prefix) throws IOException {
		List<String> itemEntries = new LinkedList<>();
		for (Entry<String, String> item : items.entrySet()) {
			String left = "\"" + prefix + item.getKey() + "\"";
			String right = "\"" + item.getValue() + "\"";
			itemEntries.add(left + ": " + right);
		}
		return itemEntries;

	}

	public static String generateLangFile(Map<String, String> items, Map<String, String> blocks) throws IOException {
		List<String> entries = generateEntries(items, "item.basemod.");
		entries.addAll(generateEntries(blocks, "block.basemod."));

		String itemEntry = entries.stream().collect(Collectors.joining(","));

		Path templateFile = Paths.get(".").resolve(templatesDir + "lang.template");
		List<String> lines = Files.readAllLines(templateFile);
		StringBuilder newLines = new StringBuilder();
		for (String line : lines) {
			String newLine = line.replace("{{ITEM_ENTRY}}", itemEntry);
			newLines.append(newLine + '\n');
		}

		Path targetJSONPath = Paths.get(".").resolve(assetsDir + "lang/en_us.json");
		Files.deleteIfExists(targetJSONPath);
		Files.write(targetJSONPath, newLines.toString().getBytes(), StandardOpenOption.CREATE);

		return newLines.toString();
	}

}