package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import Controller.MainController;

public class Client {
	
	@SuppressWarnings("unused")
	private Socket socket;
	private InputStream inputStream;
    private OutputStream outputStream;
    private MainController controller;
    
    public Client(MainController controller, Socket socket) throws IOException {
    	this.controller = controller;
    	this.socket = socket;
    	inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        sendMap();
        sendCreatures(false);
    }

	private void sendMap() {
    	controller.console.writeln("Send map...");
    	String json = controller.getMap().toJSON();
    	sendToClient(json, false);
    }
	
	public void sendCreatures(boolean mute) {
		if (!mute)
			controller.console.writeln("Send creatures...");
		String json = controller.getMap().creaturesToJSON();
    	sendToClient(json, mute);
	}
    
    public void sendToClient(String json, boolean mute){
        try {
            byte[] bytes = json.getBytes();
            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(json.length());
            byte[] bytesSize = b.array();
            if (!mute)
            	controller.console.write("Sending: " + bytes.length + " bytes... ");
            outputStream.write(bytesSize, 0, 4);
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            if (inputStream.read() == 1) {
            	if (!mute)
            	controller.console.writeln("done.");
            	return;
            }
            controller.console.writeln("Data transfer FAILED.");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}
