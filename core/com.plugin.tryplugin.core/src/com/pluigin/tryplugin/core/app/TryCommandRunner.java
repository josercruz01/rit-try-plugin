package com.pluigin.tryplugin.core.app;

import java.io.InputStream;

import com.pluigin.tryplugin.core.models.ServerConfig;
import com.pluigin.tryplugin.core.models.TryProject;

public class TryCommandRunner implements ITryCommandRunner {

	private ISSHSessionManager sessionManager;
	private ITryCommandGenerator tryCommandGenerator;
	private ICommandBuilder commandBuilder;
	private ITryCommandView view;

	public TryCommandRunner(ISSHSessionManager sessionManager,ITryCommandGenerator tryCommandGenerator,ICommandBuilder commandBuilder){
		this.sessionManager = sessionManager;
		this.commandBuilder = commandBuilder;
		this.tryCommandGenerator = tryCommandGenerator;
	}
	
	public TryCommandRunner(){
		this(new SSHSessionManager(),/*todo: missing*/null,null);
	}
	
	@Override
	public void run(ServerConfig config, TryProject project) {
		ISSHSession session = null;
		
		try{
			session = sessionManager.createSession(config);
			String remoteFolderName = session.uploadFiles("~",project.getFilenames());

			String completeCommand = commandBuilder.buildFrom(
					"cd " + remoteFolderName,
					tryCommandGenerator.create(project),
					"cd ..",
					"rm -r " + remoteFolderName
					);
			
			session.execute(view,completeCommand); 
		} catch(Exception e) {
			view.onError(e);
		} finally {
			if(session!=null) session.disconnect();
		}
	}

	public void setView(ITryCommandView view) {
		this.view = view;
	}

}