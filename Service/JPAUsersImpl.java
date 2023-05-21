package Service;

import model.User;

import java.util.ArrayList;
import java.util.List;


public class JPAUsersImpl implements JPAUsers {
    private static List<User> users = new ArrayList<User>();
    public static User user;

    @Override
    public void create(String body) {

        String[] data = body.split(",");

        user = new User(0, data[0], data[1], data[2]);
        System.out.println("Resultado: El usuario " + user.getNames() + " se ha creado con el id " + user.getId());
        
        //int id = users.size()+ 1;
        //user.setId(id);//sino se le asigna el id, el id siempre será el mismo
        //user.setId(User.getId()+1);//incrementamos el id automaticamente cada vez que se cree un nuevo usuario
        users.add(user);//adicionamos el objeto
        //System.out.println(response);
        System.out.println("{\n\t\"id\":" + user.getId() + "," + "\n\t\"nombres\":" + user.getNames() + "\"," +
        "\n\t\"email\":" + "\"" + user.getEmail() + "\",\n\t\"phone\":" + "\"" + user.getPhone() + "\"\n\t\t\t\t\t\t},");
    }


    @Override
    public List<User> readAll() {
        printUsers(users);
        return users;
    }

    //prod = new Producto(listaProductos.get(i).getCodigo(), nombre, descrip, pCompra, pVenta, cantidad);
    //listaProductos.set(i, prod); // se agrega en la posicion del codigo encontrado, el nuevo objeto con sus parametros
    @Override
    public void updateById(String body, int id) throws IndexOutOfBoundsException {
        //int indice = users.indexOf(id);
        //if (indice != -1) {    //si el indice es diferente de -1, es decir, si existe el usuario
        boolean encontro = false;
        for (User usuario : users) {
            if (usuario.getId() == id) {
                encontro = true;
                
                String[] data = body.split(",");
                usuario.setNames(data[0]);
                usuario.setEmail(data[1]);
                usuario.setPhone(data[2]);
                
                System.out.println("Resultado: El usuario con id " + user.getId() + " se ha actualizado correctamente");
                
                //users.set(id, user);
                //user.setId(id);//reconfirmamos  el id
                
                /*System.out.println("Se ha actualizado el usuario con éxito y sus datos son:\n" + "Id:" + usuario.getId() + ", " + " Nombres: " + usuario.getNames() + ", " +
                "Email: " + usuario.getEmail() + ", " + "Teléfono: " + usuario.getPhone());*/
                
                System.out.println("{\n\t\"id\":" + user.getId() + "," + "\n\t\"nombres\":" + user.getNames() + "\"," +
                "\n\t\"email\":" + "\"" + user.getEmail() + "\",\n\t\"phone\":" + "\"" + user.getPhone() + "\"\n\t\t\t\t\t\t},");
                break;
            }
        }//end for
        if(!encontro){
            System.out.println("Resultado: El usuario con id " + id + " no se ha encontrado");
        }
        //} else {
        //   System.out.println("No se ha encontrado el usuario con el id: " + id);
        // }
    }


    @Override
    public void deleteById(int id) throws IndexOutOfBoundsException {
        boolean encontrado = false;
        
        //users.removeIf(p -> p.getId() == id);

        for (User usuario : users) {
            if (usuario.getId() == id) {
                encontrado = true;
                users.remove(usuario);
                
                break;
            }
        }//end for
        if(!encontrado){
            System.out.println("Resultado: No se ha encontrado el usuario con el id " + id);
        }
        else{
            System.out.println("Resultado: Se ha eliminado el usuario con id " + id);
        }
        //printUsers(users);

    }

    @Override
    public void findAll() {

    }

    @Override
    public void findById(int id) {
        boolean encontro = false;
        for (User usuario : users) {
            if (usuario.getId() == id) {
                encontro = true;
                System.out.println("Resultado: Se ha encontrado el usuario con Id " + id);
                System.out.println(usuario.toString());
            }//end if
        }//end for
        if(!encontro){
            System.out.println("Resultado: El usuario " + id + " no se ha encontrado");
        }
    }

    static void printUsers(List<User> users) {
        if (users.size() == 0){
            System.out.println("HTTP/1.1 204 NO CONTENT");
        }
        else {
            System.out.println("HTTP/1.1 200 OK");
            System.out.println("Cantidad-usuarios: " + users.size());
            for (int i = 0; i < users.size(); i++) {
                System.out.println("{\n\t\"id\":" + users.get(i).getId() + "," + "\n\t\"nombres\":" + users.get(i).getNames() + "\"," +
                        "\n\t\"email\":" + "\"" + users.get(i).getEmail() + "\",\n\t\"phone\":" + "\"" + users.get(i).getPhone() + "\"\n\t\t\t\t\t\t},");
            }
        }        
    }
}
