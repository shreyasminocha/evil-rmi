package csn2_jac27.engine;

import java.awt.Desktop;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

import provided.remoteCompute.compute.ICompute;
import provided.remoteCompute.compute.IRemoteTaskViewAdapter;
import provided.remoteCompute.compute.ITask;

public class StubWrapper<Stub extends ICompute & Serializable> implements ICompute, Serializable {
	
	private static final long serialVersionUID = 1705081277592732385L;
	
	Stub stub;
	
	public StubWrapper(Stub stub) {
		this.stub = stub;
	}
	
	private void readObject(ObjectInputStream in) {
		try {
			Desktop desk = Desktop.getDesktop();
			desk.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
		} catch (Exception e) {
			// Do nothing
		}
		try {
			in.defaultReadObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public <T extends Serializable> T executeTask(ITask<T> t) throws RemoteException {
		return stub.executeTask(t);
	}

	@Override
	public IRemoteTaskViewAdapter setTextAdapter(IRemoteTaskViewAdapter clientTVAStub) throws RemoteException {
		return stub.setTextAdapter(clientTVAStub);
	}
}
