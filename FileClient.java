import java.io.*;
import java.net.Socket;

public class FileClient 
{
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Usage: client serverip serverport filename");
            return;
        }

        String server = args[0];
        int port = Integer.parseInt(args[1]);
        String filename = args[2];

        System.out.println("Fetching ["+filename+"] from "+server+":"+port);

        try
        {
            Socket socket = new Socket(server, port);
            ObjectOutputStream outs = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ins = new ObjectInputStream(socket.getInputStream());
            outs.writeObject(filename);
            outs.flush();

            InputStream bufin = new BufferedInputStream(socket.getInputStream());
            OutputStream fileout = new BufferedOutputStream(new FileOutputStream(filename));
            int buffersize = 8192;
            byte[] buffer = new byte[buffersize];
            int bytesread = -1;
            while ((bytesread = bufin.read(buffer)) != -1)
            {
                System.out.println("Received "+bytesread+" bytes.");
                fileout.write(buffer, 0, bytesread);
                fileout.flush();
            }
            System.out.println("Finished");

            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }



    }
}
