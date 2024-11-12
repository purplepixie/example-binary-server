import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer 
{
    private ServerSocket socket;
    private int port = 8000;
    private String fileDir = "files/";

    public class Connection extends Thread // inner class used to handle connections
    {
        private Socket clientSocket;
        private String fileDir = "";
        private ObjectOutputStream outs;
        private BufferedOutputStream bouts; // we'll use this to send file bytes for simplicity
        private ObjectInputStream ins;

        public Connection(Socket s, String fd) // prep a new client connection
        {
            this.clientSocket = s;
            this.fileDir = fd;
        }

        public void run() // Thread code to actually handle connection
        {
            System.out.println("Client thread started");
            try
			{
				outs = new ObjectOutputStream(clientSocket.getOutputStream());
                bouts = new BufferedOutputStream(clientSocket.getOutputStream());
				ins = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("Client Initialised");
                String filename = (String) ins.readObject();
                System.out.println("Client requests file: "+filename);

                filename = this.fileDir + filename; // full file path (SECURITY NIGHTMARE)
                // open the file
                InputStream fileinstream = new FileInputStream(filename);
                int bufferSize = 8192; // 8k chunks
                int bytesread = -1;
                byte[] buffer = new byte[bufferSize];
                while ((bytesread = fileinstream.read(buffer)) != -1)
                {
                    System.out.print(".");
                    bouts.write(buffer, 0, bytesread);
                    bouts.flush();
                }
                System.out.println(" finished sending");
                this.clientSocket.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
        }
    }

    public void setPort(int p) { this.port = p; }
    public int getPort() { return this.port; }

    public void RunServer() // start the server and ServerSocket
    {
        System.out.println("Opening Server on Port "+this.port);
        try
        {
            this.socket = new ServerSocket(this.port);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        System.out.println("Server Port Opened");

        while(true) // just keep on truckin'
        {
            System.out.println("Server waiting for connection...");
            try
            {
                Socket client = this.socket.accept();
                System.out.println("Client has made connection");
                Connection conn = new Connection(client, this.fileDir);
                conn.start();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args)
    {
        FileServer fs = new FileServer();
        if (args.length > 1)
			fs.setPort(Integer.parseInt(args[1]));
        fs.RunServer();
    }
}
