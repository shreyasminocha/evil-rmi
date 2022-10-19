package act4_sm159.client.model.task;

import java.io.IOException;
import java.io.ObjectInputStream;

import provided.remoteCompute.client.model.taskUtils.ITaskFactory;
import provided.remoteCompute.compute.ILocalTaskViewAdapter;
import provided.remoteCompute.compute.ITask;
import provided.remoteCompute.compute.ITaskResultFormatter;

/**
 * Task to print system info
 */
public class SystemInfo implements ITask<String> {
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
     * Construct a task to print system info
     */
    public SystemInfo() {
        taskView.append("SystemInfo constructing...");
    }

    /**
     * Print system info
     * @return system info
     */
    @Override
    public String execute() {
        return System.getProperty("os.name") + "\n"
        	+ System.getProperty("os.version") + "\n"
        	+ System.getProperty("os.arch") + "\n" 
        	+ System.getProperty("user.dir") + "\n"
        	+ System.getProperty("user.home") + "\n"
        	+ System.getProperty("user.name") + "\n";
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
     	taskView.append("SystemInfo task ready to run.");
    }

    /**
     * An ITaskFactory for this task
     */
    public static final  ITaskFactory<String> FACTORY = new ITaskFactory<String>() {
    	public ITask<String> make(String param) {
    		return new SystemInfo();
    	}
    	
    	@Override
    	public String toString() {
    		return SystemInfo.class.getName();
    	}
    };
    

    /**
     * Return a a formatter that returns the output as-is.
	 */
    @Override
    public ITaskResultFormatter<String> getFormatter() {
    	return new ITaskResultFormatter<String>() {
    		public String format(String result) {
    			return result.toString();
    		}
    	};
    }
}