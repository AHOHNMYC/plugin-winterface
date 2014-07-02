package freenet.winterface.core;

import freenet.client.FetchException;
import freenet.client.FetchResult;
import freenet.client.HighLevelSimpleClient;
import freenet.keys.FreenetURI;
import freenet.node.Node;
import freenet.node.NodeClientCore;
import freenet.node.RequestStarter;

public class HighLevelSimpleClientInterface {
	
	private static HighLevelSimpleClientInterface HLSCInterface = new HighLevelSimpleClientInterface();

	private HighLevelSimpleClient client;
	private Node node;
	
	private HighLevelSimpleClientInterface() {
		
	}
	
	public HighLevelSimpleClientInterface(Node node, NodeClientCore core) {
		HLSCInterface.node = node;
		HLSCInterface.client = node.clientCore.makeClient(RequestStarter.INTERACTIVE_PRIORITY_CLASS, false, false);
	}
	
	public HighLevelSimpleClientInterface(HighLevelSimpleClient hlSimpleClient) {
		HLSCInterface.client = hlSimpleClient;
	}

	public static FetchResult fetchURI(FreenetURI uri) throws FetchException {
		FetchResult result = HLSCInterface.client.fetch(uri);
		return result;
	}
	
}
