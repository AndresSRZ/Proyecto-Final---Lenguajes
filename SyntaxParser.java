package model;

import controller.Server;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class SyntaxParser {    
    private static final String METHOD_PATTERN = "(OPTIONS|GET|HEAD|POST|PUT|DELETE|TRACE|CONNECT)";
    private static final String SCHEME_PATTERN = "(http|https)";
    private static final String HOST_PATTERN = "([A-Za-z0-9_+-./:=?&%;]+)+"; 
    private static final String PROTOCOL_PATTERN = "([A-Za-z0-9_+-./:=?&%;]+)+";
    private static final String GENERIC_PATTERN = ".+";
    private static final String HEADER_NAME_PATTERN =  "[A-Za-z0-9_+-./=?&%;]+"; //"[A-Za-z_+-]+";
    private static final String HEADER_VALUE_PATTERN = "[A-Za-z0-9_+-./:=?&%;]+"; //"[A-Za-z0-9_+-./=?&%;]+";
    private static final String JSON_PATTERN = "\\{\\s*\"(\\w+)\":\\s*(\"[^\"]*\"|\\d+|\\{[^{}]*\\}|\\[[^\\[\\]]*\\])\\s*(,\\s*\"(\\w+)\":\\s*(\"[^\"]*\"|\\d+|\\{[^{}]*\\}|\\[[^\\[\\]]*\\])\\s*)*\\}";
    
    private String metodo;
    private String protocolo;
    
    Server request = new Server();
    
    public String getMetodo(){
        return metodo;
    }

    public void setMetodo(){
        this.metodo = metodo;
    }
    
    public String getProtocolo(){
        return protocolo;
    }

    public void setProtocolo(){
        this.protocolo = protocolo;
    }
    
    public static Map<String, Object> parseJSON(String json) {
        // Remueve las llaves y comillas del JSON
        json = json.replaceAll("[{}\"]", "");

        // Divide el JSON en pares clave-valor
        String[] keyValuePairs = json.split(",");

        // Crea un mapa para almacenar las variables
        Map<String, Object> variables = new HashMap<>();

        // Procesa cada par clave-valor y almacena las variables en el mapa
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            variables.put(key, value);
            // Verifica si el valor es numérico
            /*
            if (value.matches("\\d+")) {
                variables.put(key, Integer.parseInt(value));
            } else {
                variables.put(key, value);
            }*/
        }
        return variables;
    }
    
    public void ApiRest(String linea, String cabecera, String body, int id) {
        String lineaOutput = "";
        String cabeceraOutput = "";
        String bodyOutput = "";
        Boolean parserOk = false;
        //input = "GET http://example.com/api/users HTTP/1.1";
        //linea = "GET http://example.com/api/users HTTP/1.1\r\n";
        /*System.out.println("Linea " + linea);
        System.out.println("Cabecera " + cabecera);
        System.out.println("Cuerpo " + body);*/
        
        //---------------------------------------------------------------------------------------
        //   SE VERIFICA QUE LA LINEA PETICION ESTE BIEN CONSTRUIDA 
        //---------------------------------------------------------------------------------------
        Pattern pattern = Pattern.compile(
            String.format("%s %s://%s %s(\\r\\n)?(\\n)?", METHOD_PATTERN, SCHEME_PATTERN, HOST_PATTERN, PROTOCOL_PATTERN),Pattern.MULTILINE
        );                   
        //System.out.println("pattern " + pattern);               
        Matcher matcher = pattern.matcher(linea);
        //System.out.println("Despues de matcher " + matcher);
        
        if (matcher.matches()) {
            
            //System.out.println("Parser linea de peticion correcta");
            
            //System.out.println("Parser group 1 " + matcher.group(1));
            metodo = matcher.group(1);
            //System.out.println("Parser group 2 " + matcher.group(2));
            String esquema = matcher.group(2);
            //System.out.println("Parser group 3 " + matcher.group(3));
            String servidor = matcher.group(3);
            //System.out.println("Parser group 4 " + matcher.group(4));
            protocolo = matcher.group(4);
                        
            /*
            System.out.println("metodo: " + metodo);
            System.out.println("esquema: " + esquema);
            System.out.println("servidor: " + servidor);
            System.out.println("protocolo: " + protocolo);
            //System.out.println("protocolov: " + protocolov);*/
            
            //---------------------------------------------------------------------------------------
            //   SE VERIFICA QUE LA CABECERA ESTE BIEN CONSTRUIDA 
            //---------------------------------------------------------------------------------------            
            //cabecera = "Content-Type:\n application/json\r\nAuthorization: Bearer abc123\r\n\r\n";
            //System.out.println("Cabecera: " + cabecera);
            
            //pattern = Pattern.compile(String.format("(%s):\\s(%s)"/*"(%s): (%s)(\\r\\n)?(\\n)?"*/, HEADER_NAME_PATTERN, HEADER_VALUE_PATTERN),Pattern.MULTILINE);
            pattern = Pattern.compile(String.format("(%s): (%s)(\\r\\n)?(\\n)?", HEADER_NAME_PATTERN, HEADER_VALUE_PATTERN));
            matcher = pattern.matcher(cabecera);
            //System.out.println("Cabecera1: " + cabecera);
            
            int contador = 0;
            while (matcher.find()) {
                contador = contador + 1;
                String headerName = matcher.group(1);
                String headerValue = matcher.group(2);
            }
            //int lineas = cabecera.length() - cabecera.replace(":", "").length();
            int lineas = cabecera.split("\n").length;            //System.out.println("Cabecera1: " + cabecera);
            /*System.out.println("Contador: " + contador);
            System.out.println("Lineas: " + lineas);*/
            
            if (lineas == contador){
                //System.out.println("Cabecera2: " + cabecera);
                //---------------------------------------------------------------------------------------
                //   SE VERIFICA QUE EL CUERPO PETICION ESTE BIEN CONSTRUIDA 
                //--------------------------------------------------------------------------------------- 
                
                //System.out.println("Parser cabecera de peticion correcta. Lineas cabecera correctas: " + lineas);
                //body = "{\"name\": \"John\", \"age\": 30}\r\n";
                if(!body.isEmpty()){
                    pattern = Pattern.compile(
                    String.format("%s(\\r\\n)?(\\n)?", JSON_PATTERN),Pattern.MULTILINE);         
                
                    matcher = pattern.matcher(body);
                    
                    if (matcher.matches()) {
                        //System.out.println("Parser cuerpo de peticion correcto");
                        parserOk = true;
                        lineaOutput = protocolo + " 200 OK";
                    }
                    else{
                        lineaOutput = "400 BAD REQUEST\n";
                        cabeceraOutput = "PARSER-ERROR: Cuerpo de peticion incorrecta. Json incorrecto" + body;
                    }
                }
                else {
                    parserOk = true;
                    lineaOutput = protocolo + " 200 OK";
                }
            }
            else {
                lineaOutput = "400 BAD REQUEST\n";
                cabeceraOutput = "PARSER-ERROR: Cabecera de peticion incorrecta. Lineas cabecera incorrectas: " + (lineas-contador);
            }
        } 
        else {
            //System.out.println("Parser: Error de sintaxis");
            pattern = Pattern.compile(String.format("%s%s(\\r\\n)?(\\n)?", METHOD_PATTERN, GENERIC_PATTERN));                                      
            matcher = pattern.matcher(linea);    
            if (matcher.matches()) {
                //System.out.println("Parametro metodo OK");
                pattern = Pattern.compile(String.format("%s %s%s(\\r\\n)?(\\n)?", METHOD_PATTERN, SCHEME_PATTERN, GENERIC_PATTERN)); 
                matcher = pattern.matcher(linea);    
                if (matcher.matches()) {
                    //System.out.println("Parametro esquema OK");
                    pattern = Pattern.compile(String.format("%s %s://%s%s(\\r\\n)?(\\n)?", METHOD_PATTERN, SCHEME_PATTERN, HOST_PATTERN, GENERIC_PATTERN)); 
                    matcher = pattern.matcher(linea);    
                    if (matcher.matches()) {
                        //System.out.println("Parametro servidor OK");
                        pattern = Pattern.compile(String.format("%s %s://%s %s%s(\\r\\n)?(\\n)?", METHOD_PATTERN, SCHEME_PATTERN, HOST_PATTERN, PROTOCOL_PATTERN, GENERIC_PATTERN)); 
                        matcher = pattern.matcher(linea);    
                        if (matcher.matches()) {
                            //lineaOutput = "400 BAD REQUEST\n";
                            //cabeceraOutput = "PARSER-ERROR: El protocolo no se incluyo en la linea de peticion \n";
                        }
                        else{
                            lineaOutput = "400 BAD REQUEST\n";
                            cabeceraOutput = "PARSER-ERROR: El protocolo no se incluyo en la linea de peticion \n";
                        }
                    }
                    else{
                        lineaOutput = "400 BAD REQUEST\n";
                        cabeceraOutput = "PARSER-ERROR: El servidor no se incluyo en la linea de peticion \n";
                    }
                }
                else{
                    lineaOutput = "400 BAD REQUEST\n";
                    cabeceraOutput = "PARSER-ERROR: El esquema (http|https) no se incluyo en la linea de peticion \n";
                }
            }
            else{
                lineaOutput = "400 BAD REQUEST\n";
                cabeceraOutput = "PARSER-ERROR: El metodo (OPTIONS|GET|HEAD|POST|PUT|DELETE|TRACE|CONNECT) no se incluyo en la linea de peticion \n"; 
            }
            /*System.out.println("Parser group 1 " + matcher.group(1));
            metodo = matcher.group(1);
            System.out.println("Parser group 2 " + matcher.group(2));
            String esquema = matcher.group(2);
            System.out.println("Parser group 3 " + matcher.group(3));
            String servidor = matcher.group(3);
            System.out.println("Parser group 4 " + matcher.group(4));
            String protocolo = matcher.group(4);*/
            lineaOutput = "400 BAD REQUESTttt\n";
        }
        if (parserOk){
            switch (metodo) {
                case "GET":
                    if (id==0) {
                        //System.out.println(lineaOutput);
                        request = new Server("GET", "http://localhost:8080/api/users", "Content-Type: application/json", "");
                        
                        break;
                    }
                    else {
                        System.out.println(lineaOutput);
                        request = new Server("GET", "http://localhost:8080/api/users", "Content-Type: application/json", "", id);
                        break;
                    }
                case "POST":
                    lineaOutput = protocolo + " 201 CREATED";                    
                    System.out.println(lineaOutput);

                    Map<String, Object> variables = parseJSON(body);
                    
                    String usuario = variables.get("nombres") + "," + variables.get("email") + "," + variables.get("phone");
                    
                    request = new Server("POST", "http://localhost:8080/api/users", "Content-Type: application/json", usuario);
                    break;
                case "PUT":
                    lineaOutput = protocolo + " 201 CREATED";                    
                    System.out.println(lineaOutput);
                    
                    Map<String, Object> variables1 = parseJSON(body);
                    
                    String usuario1 = variables1.get("nombres") + "," + variables1.get("email") + "," + variables1.get("phone");
                    
                    request = new Server("PUT", "http://localhost:8080/api/users", "Content-Type: application/json", usuario1, id);
                    break;
                case "DELETE":
                    System.out.println(lineaOutput);
                    
                    request = new Server("DELETE", "http://localhost:8080/api/users", "Content-Type: application/json", "",id);
                    
                    break;
                    
                default:
                    System.out.println("Método no soportado");
                    break;
            }
        }
        else {
            System.out.println(lineaOutput + cabeceraOutput + bodyOutput);
        }
    }
}