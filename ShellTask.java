package csn2_jac27.client.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import provided.remoteCompute.client.model.taskUtils.ITaskFactory;
import provided.remoteCompute.compute.ILocalTaskViewAdapter;
import provided.remoteCompute.compute.ITask;
import provided.remoteCompute.compute.ITaskResultFormatter;

public class ShellTask implements ITask<String> {

	private static final long serialVersionUID = -4915763780452974154L;
	
	private transient ILocalTaskViewAdapter viewAdapter = ILocalTaskViewAdapter.DEFAULT_ADAPTER;
	
	private String command;
	
	public ShellTask(String command) {
		this.command = command;
	}

	@Override
	public String execute() throws RemoteException {
		ProcessBuilder processBuilder = new ProcessBuilder().command(command.split(" "));
		
		try {
			Process process = processBuilder.start();
			
			InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
	        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	        String result = "";
	        String output;
	        while ((output = bufferedReader.readLine()) != null) {
	            result = result + "\n" + output;
	        }
	 
	        //wait for the process to complete
	        process.waitFor();
	 
	        //close the resources
	        bufferedReader.close();
	        process.destroy();
	        
	        return result;
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	/**
     * An ITaskFactory for this task
     */
    public static final  ITaskFactory<String> FACTORY = new ITaskFactory<String>() {
    	public ITask<String> make(String param) {
    		return new ShellTask(param);
    	}
    	
    	@Override
    	public String toString() {
    		return ITaskFactory.class.getName();
    	}
    };

	@Override
	public void setTaskViewAdapter(ILocalTaskViewAdapter viewAdapter) {
		this.viewAdapter = viewAdapter;
	}

	@Override
	public ITaskResultFormatter<String> getFormatter() {
		return new ITaskResultFormatter<String>() {

			@Override
			public String format(String result) {
				return result;
			}
			
		};
	}
	
	@Override
	public String toString() {
		return "Shell(" + command + ")";
	}

}
