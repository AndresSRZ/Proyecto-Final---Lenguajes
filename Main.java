package org.narvasoft;

import controller.Server;
import model.User;
import model.SyntaxParser;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SyntaxParser parser = new SyntaxParser();
        Server request = new Server();
        
        Scanner scanner = new Scanner(System.in);
        Scanner scannerId = new Scanner(System.in);
        String opcion;
        String linea;
        String cabecera;
        String body;
        int id;
        String nombre;
        String correo;
        String telefono;
        
        while (true) {
            System.out.println("\n");
            System.out.println("----------------------------------------------------");
            System.out.println("-----Proyecto final - Lenguajes de programación-----");
            System.out.println("----------------------------------------------------");
            System.out.println("- 1. Leer usuarios existentes                      -");
            System.out.println("- 2. Encontrar un usuario                          -");
            System.out.println("- 3. Crear un usuario                              -");
            System.out.println("- 4. Editar la información de un usuario existente -");
            System.out.println("- 5. Eliminar un usuario                           -");
            System.out.println("- 6. Verificar parser (EXTRA)                      -");
            System.out.println("- 0. Salir                                         -");
            System.out.println("----------------------------------------------------");
            System.out.println("Selecciona una opción (1-6): ");
            
            opcion = scanner.nextLine();
            
            System.out.println("\n");

            if (opcion.equals("1")) {
                linea = "GET http://example.com/api/users HTTP/1.1\r\n";
                cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                body = "";
                
                parser.ApiRest(linea, cabecera, body, 0);
            } 
            else if (opcion.equals("2")) {
                System.out.println("Digite el id del usuario que desea buscar: ");
                id = scannerId.nextInt();
                
                linea = "GET http://example.com/api/users HTTP/1.1\r\n";
                cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                body = "";
                
                parser.ApiRest(linea, cabecera, body, id);
            } 
            else if (opcion.equals("3")) {
                System.out.println("Ingrese el nombre del usuario que desea crear: ");
                nombre = scanner.nextLine();
                
                System.out.println("Ingrese el correo del usuario que desea crear: ");
                correo = scanner.nextLine();
                
                System.out.println("Ingrese el telefono del usuario que desea crear: ");
                telefono = scanner.nextLine();
                
                linea = "POST http://example.com/api/users HTTP/1.1\r\n";
                cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                body = "{\"nombres\": \"" + nombre + "\", \"email\": \"" + correo + "\", \"phone\": \"" + telefono + "\"}\r\n";
                
                parser.ApiRest(linea, cabecera, body, 0);
            } 
            else if (opcion.equals("4")) {
                System.out.println("Digite el id del usuario que desea editar: ");
                id = scannerId.nextInt();
                         
                System.out.println("Ingrese el nuevo nombre del usuario con id " + id + ":");
                nombre = scanner.nextLine();
                
                System.out.println("Ingrese el nuevo correo del usuario con id " + id + ":");
                correo = scanner.nextLine();
                
                System.out.println("Ingrese el nuevo telefono del usuario con id " + id + ":");
                telefono = scanner.nextLine();
                
                linea = "PUT http://example.com/api/users HTTP/1.1\r\n";
                cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                body = "{\"nombres\": \"" + nombre + "\", \"email\": \"" + correo + "\", \"phone\": \"" + telefono + "\"}\r\n";
                
                parser.ApiRest(linea, cabecera, body, id);
            } 
            else if (opcion.equals("5")) {
                System.out.println("Digite el id del usuario que desea eliminar: ");
                id = scannerId.nextInt();
                
                linea = "DELETE http://example.com/api/users HTTP/1.1\r\n";
                cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                body = "";
                                
                parser.ApiRest(linea, cabecera, body, id);
            }            
            else if (opcion.equals("6")) {
                System.out.println("Ingrese una linea de peticion (Ej: GET http://example.com/api/users HTTP/1.1)");
                linea = scanner.nextLine();                
                //linea = "GET http://example.com/api/users HTTP/1.1";

                
                System.out.println("Ingrese una cabecera de peticion (Ej: Content-Type: application/json)");
                cabecera = scanner.nextLine();           
                //cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";

                
                System.out.println("Ingrese un cuerpo de peticion (Ej: {\"nombres\": \"" + "Andres" + "\", \"email\": \"" + "andres@gmail.com" + "\", \"phone\": \"" + "3006678065" + "\"}");
                body = scanner.nextLine();           
                //body = "";
                
                parser.ApiRest(linea, cabecera + "\r\n", body, 0);
            }
            else if (opcion.equals("0")) {
                System.out.println("Saliendo del programa...");

                break;
            }
            else {
                System.out.println("Elección inválida. Por favor, inténtelo de nuevo.");
            }
        }
    }
}

