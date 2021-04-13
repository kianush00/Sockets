import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteTCP {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args){

        Socket socketCliente;
        DataInputStream in;
        DataOutputStream out;

        final String serverName = "127.0.0.1";   //dirección IP del socket del cliente, en este caso es la misma máquina
        final int serverPort = 29000;     //puerto que usará el cliente para conectarse al servidor

        try {
            //se establece el socket del cliente, que contiene la dirección y puerto de destino, para poder comunicarse
            //con el servidor
            socketCliente = new Socket(serverName,serverPort);
            System.out.println("Conexión establecida con el servidor " +
                    socketCliente.getInetAddress().getHostAddress() + "\n");

            //se establece una flujo de entrada de datos en el socket por parte del servidor
            in = new DataInputStream(socketCliente.getInputStream());
            //se establece un flujo de salida de datos en el socket por parte del cliente
            out = new DataOutputStream(socketCliente.getOutputStream());

            int opcion;
            do {
                //se recibe el mensaje enviado por el servidor, que contiene las opciones disponibles a consultar
                String aRecibir = in.readUTF();
                System.out.println(aRecibir);

                opcion = elegirOpcionYValidar(0,7);
                //la opción seleccionada se envía al servidor, codificado en UTF
                out.writeUTF(String.valueOf(opcion));

                //Se recibe la hora actual de la zona seleccionada, en caso de querer salir se recibe un mensaje de despedida
                aRecibir = in.readUTF();
                System.out.println(aRecibir);

                //Se envía un mensaje de confirmación por parte del cliente
                out.writeUTF("Servicio recibido!");

                if (opcion != 0){
                    Thread.sleep(4000);
                }
            } while (opcion != 0);

            //cuando el cliente salga de la sesión, finalmente se cierra el socket del cliente
            socketCliente.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int elegirOpcionYValidar(int min, int max) {
        int opcion = -1;    //se inicializa la opción
        boolean opcionEsValida = false;

        while(!opcionEsValida){
            while (!input.hasNextInt()){
                System.err.println("La opción ingresada no es un entero. Intenta nuevamente:");
                input.next();   //pasa al siguiente iterador
            }

            opcion = input.nextInt();
            if((opcion < min) || (opcion > max)){
                System.err.println("La opción ingresada no se encuentra dentro del rango. Intenta nuevamente:");
                opcionEsValida = false;
            }else{
                opcionEsValida = true;
            }
        }
        return opcion;
    }
}
