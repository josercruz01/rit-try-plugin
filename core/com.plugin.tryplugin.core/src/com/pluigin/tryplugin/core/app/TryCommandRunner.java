package com.pluigin.tryplugin.core.app;

import com.pluigin.tryplugin.core.models.ITryCommandView;
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
		this(new SSHSessionManager(),new TryCommandGenerator(),new CommandBuilder());
	}
	
	@Override
	public void run(ServerConfig config, TryProject project) {
		ISSHSession session = null;
		
		try{
			session = sessionManager.createSession(getView(),config);
			String remoteFolderName = session.uploadFiles("~",project.getFilenames());

			String completeCommand = commandBuilder.buildFrom(
					"cd " + remoteFolderName,
					tryCommandGenerator.create(project),
					"cd ..",
					"rm -r " + remoteFolderName
					);
			
			session.execute(getView(),completeCommand); 
		} catch(Exception e) {
			getView().onError(e);
		} finally {
			if(session!=null) session.disconnect();
		}
	}

	public void setView(ITryCommandView view) {
		this.view = view;
	}
	
	public ITryCommandView getView(){
		if(view == null) {
			throw new IllegalArgumentException();
		}
		
		return view;
	}
}