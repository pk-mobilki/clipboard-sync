package pl.edu.pk.mobilki.clipboardsync.sync;

import android.util.Log;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class TcpClient
{
    private PrintWriter mBufferOut;

    public TcpClient(String serverIp, String serverPort)
    {
        int serverPort1 = Integer.parseInt(serverPort);

        try
        {
            InetAddress serverAddr = InetAddress.getByName(serverIp);
            Socket socket = new Socket(serverAddr, serverPort1);
            mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }
        catch (Exception ex)
        {
            Log.d("STATE", ex.getMessage() + Arrays.toString(ex.getStackTrace()));
        }
    }

    public static boolean isPortOpen(final String ip, final int port, final int timeout) {

        try
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        }

        catch(ConnectException ce)
        {
            ce.printStackTrace();
            return false;
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public void sendMessage(String message)
    {
        if (mBufferOut != null && !mBufferOut.checkError())
        {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public void stopClient()
    {
        if (mBufferOut != null)
        {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mBufferOut = null;
    }
}