package com.pluigin.tryplugin.core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.pluigin.tryplugin.core.app.TryCommandRunner;
import com.pluigin.tryplugin.core.models.ITryCommandView;
import com.pluigin.tryplugin.core.models.ServerConfig;
import com.pluigin.tryplugin.core.models.TryProject;

public class Main {
	/**
	 * Configuration
	 */

	/**
	 * Main application. 
	 * Execute series of tests with SSH and SCP to execute remote commands
	 * and to upload files remotely on top of SSH
	 * 
	 * @param args command line passed in arguments
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		// core try command runner
		TryCommandRunner runner = new TryCommandRunner();

		// get server auth
		String host = "localhost";
		String username = "raymi";

		println("provide password:");
		String password = in.readLine();


		// ssh view handler
		ITryCommandView view = new ITryCommandView() {
			@Override
			public boolean promptYesNoRSAKeyFingerprint(String str) {
				println(str);
				try {
					return in.readLine().equals("yes");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				println("");
			}

			@Override
			public void onCommandExecuted(String result, int exitStatus) {
				println(result);
				println("exit-status:"+exitStatus);
			}
		};

		// ssh server configuration
		ServerConfig config = new ServerConfig(host,username,password);
		config.setPort(3000);

		// files to upload
		ArrayList<String> files = new ArrayList<String>();
		files.add("main.cpp");
		files.add("main.h");

		// Try project settings
		TryProject project = new TryProject("jrc","homework1",files);
		runner.setView(view);
		runner.run(config, project);

		println("Running!!!");

	}

	public static void println(Object text){
		System.out.println(text);
	}
}
