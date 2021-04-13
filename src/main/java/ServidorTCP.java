import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorTCP {

    public static void main(String[] args){

        ServerSocket socketAcogida;
        Socket socketConexion;
        DataInputStream in;
        DataOutputStream out;

        final int PUERTO = 29000;   //puerto que será establecido por el servidor

        try {
            //se establece el socket de acogida con la dirección del servidor y el puerto asociado
            socketAcogida = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado. Esperando petición entrante...");

            while (true){
                //Al momento de escuchar la petición de un cliente, el servidor acepta el acuerdo en tres fases, por
                // lo que crea un nuevo socket, con un puerto definido por el servidor, dedicado a ese cliente
                socketConexion = socketAcogida.accept();
                System.out.println("Conexión establecida con el cliente " +
                        socketConexion.getInetAddress().getHostAddress() + "\n");

                //se establece una flujo de entrada de datos en el socket por parte del cliente
                in = new DataInputStream(socketConexion.getInputStream());
                //se establece un flujo de salida de datos en el socket por parte del servidor
                out = new DataOutputStream(socketConexion.getOutputStream());

                //El servidor le muestra las opciones de zonas horarias que el cliente luego va a seleccionar por consola
                int opcionSeleccionada;
                do {
                    String aEnviar = "\n\nSeleccione la zona a la cual desea consultar su hora actual:\n" +
                        "1. Londres\n2. Madrid\n3. Moscú\n4. Nueva York\n5. Los Ángeles\n6. Tokio\n7. Calcuta" +
                            "\n0. Cerrar sesión";
                    //se envía el mensaje hacia el cliente, que contiene las opciones disponibles a consultar
                    out.writeUTF(aEnviar);

                    //se recibe el mensaje que contiene la opción seleccionada por el cliente
                    String aRecibir = in.readUTF();
                    opcionSeleccionada = Integer.parseInt(aRecibir);

                    //se envía el mensaje que contiene el servicio a la petición del cliente
                    out.writeUTF(horaZonaSeleccionada(opcionSeleccionada));

                    //Se recibe un mensaje de confirmación por parte del cliente
                    aRecibir = in.readUTF();
                    System.out.println(aRecibir);

                } while(opcionSeleccionada != 0);

                //cuando el cliente salga de la sesión, finalmente se cierra el socket de conexión, sin embargo
                //aún se mantiene activo el socket de acogida a otros clientes
                socketConexion.close();
                System.out.println("Conexión finalizada con el cliente " +
                        socketConexion.getInetAddress().getHostAddress() + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String horaZonaSeleccionada(int opcion){
        ZonedDateTime zdt;
        DateTimeFormatter formatter;

        switch (opcion){
            case 0:
                return "Adiós!";
            case 1:
                zdt = ZonedDateTime.now(ZoneId.of("Europe/London"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
            case 2:
                zdt = ZonedDateTime.now(ZoneId.of("Europe/Madrid"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
            case 3:
                zdt = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
            case 4:
                zdt = ZonedDateTime.now(ZoneId.of("America/New_York"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
            case 5:
                zdt = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
            case 6:
                zdt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
            case 7:
                zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z");
                return zdt.format(formatter);
        }

        return "";
    }
}
