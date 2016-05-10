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
        sendCreatures();
    }

	private void sendMap() {
    	controller.console.writeln("Send map...");
    	String json = controller.getMap().toJSON();
    	sendToClient(json);
    }
	
	public void sendCreatures() {
		controller.console.writeln("Send creatures...");
		String json = controller.getMap().creaturesToJSON();
    	sendToClient(json);
	}
    
    public void sendToClient(String json){
        try {
            byte[] bytes = json.getBytes();
            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(json.length());
            byte[] bytesSize = b.array();
            controller.console.write("Sending: " + bytes.length + " bytes... ");
            outputStream.write(bytesSize, 0, 4);
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            if (inputStream.read() == 1) {
            	controller.console.writeln("done.");
            	return;
            }
            controller.console.writeln("FAILED.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
