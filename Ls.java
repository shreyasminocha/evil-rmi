package act4_sm159.client.model.task;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import provided.remoteCompute.client.model.taskUtils.ITaskFactory;
import provided.remoteCompute.compute.ILocalTaskViewAdapter;
import provided.remoteCompute.compute.ITask;
import provided.remoteCompute.compute.ITaskResultFormatter;

/**
 * Task to list files
 */
public class Ls implements ITask<String[]> {
	/**
	 * SerialversionUID for well-defined serialization.
	 */
    private static final long serialVersionUID = 227L;
    
    /**
     * Adapter to the local view.  Marked "transient" so that it is not serialized
     * and instead is re-attached at the destination (the server).  
     */
    private transient ILocalTaskViewAdapter taskView = ILocalTaskViewAdapter.DEFAULT_ADAPTER;
    
    /**
     * Construct a task to list files
     */
    public Ls() {
        taskView.append("Ls constructing...");
    }

    /**
     * List hw files
     * @return hw files
     */
    @Override
    public String[] execute() {
    	File currentDirectory = new File("..");
    	
    	return Arrays.asList(currentDirectory.listFiles())
			.stream()
			.map(file -> file.getName())
			.toArray(size -> new String[size]);
    }

    /**
     * Reinitializes transient fields upon deserialization.  See the 
     * <a href="http://download.oracle.com/javase/6/docs/api/java/io/Serializable.html">
     * Serializable</a> marker interface docs.
     * taskView is set to a default value for now (ILocalTaskViewAdapter.DEFAULT_ADAPTER).
     * @param stream The object stream with the serialized data
     * @throws IOException if the input stream cannot be read correctly
     * @throws ClassNotFoundException if the class file associated with the input stream cannot be located.
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    	stream.defaultReadObject(); // Deserialize the non-transient data
    	taskView = ILocalTaskViewAdapter.DEFAULT_ADAPTER;  // re-initialize the transient field
    }

    /**
     * Sets the task view adapter to a new value.  Used by the server to attach
     * the task to its view.
     */
    @Override
    public void setTaskViewAdapter(ILocalTaskViewAdapter viewAdapter) {
     	taskView = viewAdapter;
     	taskView.append("Ls task ready to run.");
    }

    /**
     * An ITaskFactory for this task
     */
    public static final  ITaskFactory<String[]> FACTORY = new ITaskFactory<String[]>() {
    	public ITask<String[]> make(String param) {
    		return new Ls();
    	}
    	
    	@Override
    	public String toString() {
    		return Ls.class.getName();
    	}
    };
    

    /**
     * Return a a formatter that returns the output as-is.
	 */
    @Override
    public ITaskResultFormatter<String[]> getFormatter() {
    	return new ITaskResultFormatter<String[]>() {
    		public String format(String[] result) {
    			return String.join("\n", result);
    		}
    	};
    }
}