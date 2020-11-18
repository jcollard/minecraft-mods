#!/bin/bash

## The Big Sur update for mac creates an issue related to the default JVM that is being run.
## This results in issues launching eclipse. To solve this, you need to update the
## Info.plist in the Eclipse installation folder to specify which version of java to use
## In 2020 - 2021, students are using Java 1.8 to compile MC Forge. This file looks for 
## Java 1.8.0_261 (the version provided with the installation package) and then creates a
## configuration file specifying that version should be used.
## This file assumes Eclipse was installed by dragging it into the Applications directory on Catalina or Big Sur

cd -- "$(dirname "$BASH_SOURCE")"

JHOME="$(/usr/libexec/java_home -v 1.8.0_261)"

if [ "$?" != "0" ]; then
  echo "ERROR: Could not locate Java 1.8.0_261"
  exit 1
fi

if [ ! -f "/Applications/Eclipse.app/Contents/Info.plist" ]; then
  echo "ERROR: Could not locate Eclipse installation directoy."
  exit 1
fi

if [ ! -f "/Applications/Eclipse.app/Contents/Info.plist.backup" ]; then
  echo "Backing up Info.plist"
  cp /Applications/Eclipse.app/Contents/Info.plist /Applications/Eclipse.app/Contents/Info.plist.backup
fi

echo "Updating Info.plist with JAVA=$JHOME"

echo """
<?xml version="1.0" encoding="UTF-8" standalone="no"?><plist version="1.0">
  
  <dict>
    	
    <key>CFBundleExecutable</key>
    		
    <string>eclipse</string>
    	
    <key>CFBundleGetInfoString</key>
    		
    <string>Eclipse 4.16 for Mac OS X, Copyright IBM Corp. and others 2002, 2020. All rights reserved.</string>
    	
    <key>CFBundleIconFile</key>
    		
    <string>Eclipse.icns</string>
    	
    <key>CFBundleIdentifier</key>
    		
    <string>org.eclipse.platform.ide</string>
    	
    <key>CFBundleInfoDictionaryVersion</key>
    		
    <string>6.0</string>
    	
    <key>CFBundleName</key>
    		
    <string>Eclipse</string>
    	
    <key>CFBundlePackageType</key>
    		
    <string>APPL</string>
    	
    <key>CFBundleShortVersionString</key>
    		
    <string>4.16.0</string>
    	
    <key>CFBundleSignature</key>
    		
    <string>????</string>
    	
    <key>CFBundleVersion</key>
    		
    <string>4.16.0.I20200604-0540</string>
    	
    <key>NSHighResolutionCapable</key>
    		
    <true/>
    	
    <key>CFBundleDevelopmentRegion</key>
    		
    <string>English</string>
    		
	
    <key>Eclipse</key>
    		
    <array>
      <string>-vm</string>
      <string>$JHOME/bin/java</string>

      <!-- to use a specific Java version (instead of the platform's default) uncomment one of the following options,
					or add a VM found via $/usr/libexec/java_home -V
				<string>-vm</string><string>/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Commands/java</string>
				<string>-vm</string><string>/Library/Java/JavaVirtualMachines/1.8.0.jdk/Contents/Home/bin/java</string>
			-->
      			
      <string>-keyring</string>
      <string>~/.eclipse_keyring</string>
      		
    </array>
    
    <key>CFBundleDisplayName</key>
    <string>Eclipse</string>
  	<key>CFBundleURLTypes</key>
		<array>
			<dict>
				<key>CFBundleURLName</key>
					<string>Eclipse command</string>
				<key>CFBundleURLSchemes</key>
					<array>
						<string>eclipse+command</string>
					</array>
			</dict>
			<dict>
				<key>CFBundleURLName</key>
					<string>Eclipse Marketplace</string>
				<key>CFBundleURLSchemes</key>
					<array>
						<string>eclipse+mpc</string>
					</array>
			</dict>
		</array>
</dict>
  
</plist>
""" > /Applications/Eclipse.app/Contents/Info.plist