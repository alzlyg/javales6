package ru.zlygostev;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;


/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) {

        ServerSocket serv = null;
        Socket sock = null;
        try {
            serv = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            sock = serv.accept();
            System.out.println("Клиент подключился");
            Scanner sc = new Scanner(sock.getInputStream());
            PrintWriter pw = new PrintWriter(sock.getOutputStream());
            while (true) {
                String message = sc.nextLine();
                if (message.equals("end")) break;
                System.out.println(message);
                pw.println("Эхо: " + message);
                pw.flush();
            }
        } catch (IOException e) {
            System.out.println("Ошибка инициализации сервера");
        } finally {
            try {
                serv.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
