package practicaCompras;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.JFileChooser;

public class ClienteCarrito {
// Funcion Principal
    public static void main(String [] arc) throws IOException{        
        try{
            Socket cl = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // Se inicializa buffer para leer datos desde teclado
            System.out.println("                       ##### Practica 1 - Carrito de Compras #####\n");
            System.out.println("----- Gonzalez Barrientos Geovanni Daniel 3CV18 Aplicaciones para Comunicaciones en Red -----\n\n");

            System.out.println("**** CLIENTE INICIADO ****\n");
            System.out.printf("Escriba la direccion del servidor: ");
            String host = br.readLine(); // Se lee la direccion del servidor
            System.out.printf("\nEscriba el puerto: ");
            int pto = Integer.parseInt(br.readLine()); // Se lee el puerto del servidor
         
            cl = new Socket(host,pto); // Crea conexion con servidor segun direccion y puerto
            System.out.printf("\n!!! CONEXION CON SERVIDOR ESTABLECIDO !!! \nEsperando catalogo desde el servidor... ");
            
            menuCliente(cl);
            System.out.printf("\n!!! CONEXION CON SERVIDOR FINALIZADO !!!");
          
        }catch (IOException | NumberFormatException e){
        }// catch
    }// main
    
    
    // Funcion para enviar archivos
    public static void menuCliente(Socket cl){
        try{
            Producto[] catalogo = new Producto[20]; // Se crea array para almacenar el catalogo
            Producto[] carrito = new Producto[20]; // Se crea array para almacenar el carrito
            Producto auxProd = new Producto(0,"",0,0,""); // Se inicializa auxiliar con valores predeterminados
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // Buffer para realizar lecturas desde teclado
            int menu, opcion, opcion2, i;
            float subtotal = 0,total = 0; // Variables para realizar calculos al momento de comprar
            
            // Se inicializa el arreglo con valores predeterminados
            for(i=0;i<catalogo.length;i++){ 
                catalogo[i] = new Producto(0,"",0,0,"");
                carrito[i] = new Producto(0,"",0,0,"");
            }
            catalogo = downloadCatalogo(cl, catalogo); // se descarga el catalogo y se asigna al arreglo
            
            do{// Menu principal del cliente
                System.out.print("\n\n\n\n\n\n\n\n**** MENU PRINCIPAL ****\n");
                System.out.print("1) Ver Catalogo de productos \n2) Ver Carrito \n3) Agregar producto a Carrito \n4) Modificar productos del carrito \n");
                System.out.print("5) Eliminar producto de carrito \n6) Vaciar Carrito \n7) Comprar productos del carrito \n0) Salir del programa \n\n");
                
                System.out.print("Ingrese el numero correspondiente al inciso y pulse Enter: ");
                menu = Integer.parseInt(br.readLine());
                
                switch(menu){
                    case 1: // Ver Catalogo de Productos
                        verCatalogo(catalogo,carrito,1);
                        break;
                        
                    case 2: // Ver Carrito
                        verCatalogo(catalogo,carrito,2);
                        break;
                        
                    case 3: // Agregar Producto a Carrito
                        verCatalogo(catalogo,carrito,1);
                        System.out.print("Ingrese el ID del producto que desea agregar a su carrito: ");
                        opcion = Integer.parseInt(br.readLine());
                        
                        // Bucle para obtener los datos del producto seleccionado en catalogo
                        for(i=0; i<catalogo.length; i++){
                            if(catalogo[i].id == opcion){ // busca el producto por id
                                if(catalogo[i].stock != 0){
                                    auxProd.id = catalogo[i].id;
                                    auxProd.name = catalogo[i].name;
                                    auxProd.price = catalogo[i].price;
                                    auxProd.stock = catalogo[i].stock;
                                    auxProd.description = catalogo[i].description;
                                    
                                    catalogo[i].stock--; // Resta una unidad del stock para agregarlo al carrito
                                    
                                    if(catalogo[i].stock == 0){ // Verifica si ya no hay existencias del producto en catalogo
                                        catalogo[i] = new Producto(0,"",0,0,"");
                                    }
                                    break;
                                }//if
                            }//if
                        }//for
                        
                        // Bucle para ingresar el producto al carrito
                        for(i=0; i<carrito.length; i++){
                            if(carrito[i].id == auxProd.id){ // ya existe el producto en carrito
                                    carrito[i].stock++;
                                    break;
                            }else{ // Producto es nuevo en carrito
                                if(carrito[i].id < 100){
                                    carrito[i].id = auxProd.id;
                                    carrito[i].name = auxProd.name;
                                    carrito[i].price = auxProd.price;
                                    carrito[i].stock = 1;
                                    carrito[i].description = auxProd.description;
                                    break;
                                }
                            }//else
                        }//for
                        System.out.print("!!! Producto ha sido agregado al carrito !!!\n");  
                        System.out.print("Presione Enter para Continuar... \n");  
                        br.readLine();
                        break;
                        
                    case 4: // Modificar Productos
                        verCatalogo(catalogo,carrito,2);
                        System.out.print("Que operacion desea realizar? ( 1) Aumentar cantidad / 2) Restar cantidad ): ");
                        opcion = Integer.parseInt(br.readLine());
                        
                        switch(opcion){
                            case 1: // Aumentar cantidad
                                System.out.print("Ingrese el ID del producto que desea modificar su carrito: ");
                                opcion = Integer.parseInt(br.readLine());
                                
                                System.out.print("Ingrese la cantidad de unidades que desea agregar de ese producto: ");
                                opcion2 = Integer.parseInt(br.readLine());

                                // Bucle para obtener los datos del producto seleccionado en catalogo
                                for(i=0; i<catalogo.length; i++){
                                    if( (catalogo[i].id == opcion ) && (opcion2 <= catalogo[i].stock) ){ // busca el producto por id
                                            catalogo[i].stock =- opcion2; // Resta una unidad del stock para agregarlo al carrito

                                            if(catalogo[i].stock <= 0){ // Verifica si ya no hay existencias del producto en catalogo
                                                catalogo[i] = new Producto(0,"",0,0,"");
                                            }// if
                                            
                                            // Bucle para ingresar el producto al carrito
                                            for(i=0; i<carrito.length; i++){
                                                if(carrito[i].id == opcion){ // ya existe el producto en carrito
                                                        carrito[i].stock =+ opcion2;
                                                        break;
                                                }// if
                                            }//for
                                            break;
                                    }//if
                                }//for

                                System.out.print("!!! Cantidad del Producto ha sido modificado !!!\n");  
                                System.out.print("Presione Enter para Continuar... \n");  
                                br.readLine();
                                break;
                                
                            case 2: // Restar Cantidad
                                System.out.print("Ingrese el ID del producto que desea modificar en su carrito: ");
                                opcion = Integer.parseInt(br.readLine());
                                
                                System.out.print("Ingrese la cantidad de unidades que desea restar de ese producto: ");
                                opcion2 = Integer.parseInt(br.readLine());

                                // Bucle para obtener los datos del producto seleccionado en carrito
                                for(i=0; i<carrito.length; i++){
                                    if( (carrito[i].id == opcion ) && (opcion2 <= carrito[i].stock) ){ // busca el producto por id
                                        // se crea copia de respaldo por si previamente se han agregado todas las unidades 
                                        // disponibles de ese producto en catalogo y se eliminan todos del carrito
                                        auxProd.id = carrito[i].id;
                                        auxProd.name = carrito[i].name;
                                        auxProd.price = carrito[i].price;
                                        auxProd.stock = carrito[i].stock;
                                        auxProd.description = carrito[i].description;
                                        
                                        carrito[i].stock =- opcion2; // Resta las unidades seleccionadas del producto en carrito

                                        if(carrito[i].stock <= 0){ // Verifica si ya no hay existencias del producto en carrito
                                            carrito[i] = new Producto(0,"",0,0,""); // borra el producto del carrito
                                        }// if

                                        // Bucle para ingresar el producto al carrito
                                        for(i=0; i<catalogo.length; i++){
                                            if(catalogo[i].id == opcion){ // ya existe el producto en carrito
                                                catalogo[i].stock =+ opcion2;
                                                break;
                                            }else{ // producto no existe en catalogo
                                                if(catalogo[i].id < 100){
                                                    catalogo[i].id = auxProd.id;
                                                    catalogo[i].name = auxProd.name;
                                                    catalogo[i].price = auxProd.price;
                                                    catalogo[i].stock = auxProd.stock;
                                                    catalogo[i].description = auxProd.description;
                                                    break;
                                                }// if
                                            }//else
                                        }//for
                                        System.out.print("!!! Cantidad del Producto ha sido modificado !!!\n");  
                                        System.out.print("Presione Enter para Continuar... \n");  
                                        br.readLine();
                                        break;
                                    }//if
                                }//for

                                break;
                                
                            default:
                                System.out.print("!!! Ingreso una opcion invalida !!!\n");  
                                System.out.print("Presione Enter para reintentar... \n");  
                                br.readLine();
                        }// switch
                        break;
                        
                    case 5: // Eliminar Producto de Carrito
                        verCatalogo(catalogo,carrito,2);
                        System.out.print("Ingrese el ID del producto que desea eliminar del carrito: ");
                        opcion = Integer.parseInt(br.readLine());
                        
                        // Bucle para obtener los datos del producto seleccionado en carrito
                        for(i=0; i<carrito.length; i++){
                            if(carrito[i].id == opcion){ // busca el producto por id
                                // almacena la informacion del producto temporalmente    
                                auxProd.id = carrito[i].id;
                                auxProd.name = carrito[i].name;
                                auxProd.price = carrito[i].price;
                                auxProd.stock = carrito[i].stock;
                                auxProd.description = carrito[i].description;

                                // Elimina los datos del producto en carrito
                                carrito[i] = new Producto(0,"",0,0,"");
                                break;    
                            }//if
                        }//for
                        
                        // Bucle para ingresar el producto al catalogo
                        for(i=0; i<catalogo.length; i++){
                            if(catalogo[i].id == auxProd.id){ // ya existe el producto en catalogo
                                    catalogo[i].stock =+ auxProd.stock;
                                    break;
                            }else{ // Producto es nuevo en carrito
                                if(catalogo[i].id < 100){
                                    catalogo[i].id = auxProd.id;
                                    catalogo[i].name = auxProd.name;
                                    catalogo[i].price = auxProd.price;
                                    catalogo[i].stock = auxProd.stock;
                                    catalogo[i].description = auxProd.description;
                                    break;
                                }
                            }//else
                        }//for
                        System.out.print("!!! Producto ha sido eliminado del carrito !!!\n");  
                        System.out.print("Presione Enter para Continuar... \n");  
                        br.readLine();
                        break;
                        
                    case 6: // Vacia Carrito
                        verCatalogo(catalogo,carrito,2);
                        System.out.print("Desea eliminar todos los productos de su carrito? ( 1) SI / 2) NO) : ");
                        opcion = Integer.parseInt(br.readLine());
                        
                        if(opcion == 1){ // Se acepta vaciar carrito
                            // Bucle para vaciar carrito
                            for(i=0; i<carrito.length; i++){
                                if(carrito[i].id >= 100){ // verifica la existencia de un producto
                                    // almacena la informacion del producto temporalmente    
                                    auxProd.id = carrito[i].id;
                                    auxProd.name = carrito[i].name;
                                    auxProd.price = carrito[i].price;
                                    auxProd.stock = carrito[i].stock;
                                    auxProd.description = carrito[i].description;

                                    // Elimina los datos del producto en carrito
                                    carrito[i] = new Producto(0,"",0,0,"");
                                    
                                    // Bucle para ingresar el producto al catalogo
                                    for(i=0; i<catalogo.length; i++){
                                        if(catalogo[i].id == auxProd.id){ // ya existe el producto en catalogo
                                                catalogo[i].stock =+ auxProd.stock;
                                                break;
                                        }else{ // Producto es nuevo en catalogo
                                            if(catalogo[i].id < 100){
                                                catalogo[i].id = auxProd.id;
                                                catalogo[i].name = auxProd.name;
                                                catalogo[i].price = auxProd.price;
                                                catalogo[i].stock = auxProd.stock;
                                                catalogo[i].description = auxProd.description;
                                                break;
                                            }
                                        }//else
                                    }//for
                                }//if
                            }//for
                            System.out.print("!!! Carrito se ha vaciado correctamente !!!\n");  
                            System.out.print("Presione Enter para Continuar... \n");  
                            br.readLine();
                        }
                        break;
                        
                    case 7: // Comprar productos del carrito
                        // DIRECTAMENTE PONER IMPRESION DE PDF 
                          // Bucle para vaciar carrito
                        for(i=0; i<carrito.length; i++){
                            if(carrito[i].id >= 100){ // verifica la existencia de un producto
                                // almacena la informacion del producto temporalmente    
                                auxProd.id = carrito[i].id;
                                auxProd.name = carrito[i].name;
                                auxProd.price = carrito[i].price;
                                auxProd.stock = carrito[i].stock;
                                auxProd.description = carrito[i].description;
                                
                                subtotal = carrito[i].stock * carrito[i].price;
                                total =+ subtotal;
                                
                                System.out.print("Producto: " + auxProd.name + " / Cantidad: " + auxProd.stock + " / Precio Unitario: $" + auxProd.price +  "\n");
                                System.out.print("Subtotal para " + auxProd.name + " : $" + subtotal + "\n\n");

                                // Elimina los datos del producto en carrito
                                carrito[i] = new Producto(0,"",0,0,"");
                            }//if
                        }//for
                        System.out.print("Total de la Compra: $" + total + "\n\n");
                        System.out.print("!!! Gracias por realizar su compra !!!\n");  
                        System.out.print("Presione Enter para continuar... \n");  
                        br.readLine();
                        break;
                        
                    case 0:
                        System.out.print("!!! Gracias por utilizar el programa !!!\n");  
                        System.out.print("Presione Enter para Finalizar... \n");  
                        br.readLine();
                        
                        // Bucle para vaciar carrito
                        for(i=0; i<carrito.length; i++){
                            if(carrito[i].id >= 100){ // verifica la existencia de un producto
                                // almacena la informacion del producto temporalmente    
                                auxProd.id = carrito[i].id;
                                auxProd.name = carrito[i].name;
                                auxProd.price = carrito[i].price;
                                auxProd.stock = carrito[i].stock;
                                auxProd.description = carrito[i].description;

                                // Elimina los datos del producto en carrito
                                carrito[i] = new Producto(0,"",0,0,"");

                                // Bucle para ingresar el producto al catalogo
                                for(i=0; i<catalogo.length; i++){
                                    if(catalogo[i].id == auxProd.id){ // ya existe el producto en catalogo
                                            catalogo[i].stock =+ auxProd.stock;
                                            break;
                                    }else{ // Producto es nuevo en catalogo
                                        if(catalogo[i].id < 100){
                                            catalogo[i].id = auxProd.id;
                                            catalogo[i].name = auxProd.name;
                                            catalogo[i].price = auxProd.price;
                                            catalogo[i].stock = auxProd.stock;
                                            catalogo[i].description = auxProd.description;
                                            break;
                                        }// if
                                    }//else
                                }//for
                            }//if
                        }//for
                        break;
                        
                    default:
                        System.out.print("!!! Ha ingresado una opcion invalida !!!\n");  
                        System.out.print("Presione Enter para reintentar... \n");  
                        br.readLine();
                }// switch menu principal
                
            }while(menu != 0);// while menu principal
            uploadCatalogo(cl, catalogo); // Se actualiza catalogo y se envia al servidor

        }catch(IOException | NumberFormatException e){
        }// catch  
    }// menuCliente

    
    // Metodo para ver catalogo y carrito
    public static void verCatalogo(Producto[] catalogo, Producto[] carrito,int s){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            if (s == 1){ // Muestra el catalogo de productos
                System.out.print("\n--------- Catalogo de Productos ---------\n");
                for (Producto catalogo1 : catalogo) {
                    if (catalogo1.id >= 100) {
                        System.out.print("ID del producto: " + catalogo1.id + "\nNombre del Producto: " + catalogo1.name + "\n");
                        System.out.print("Precio del Producto: " + catalogo1.price + "\nCantidad Disponible: " + catalogo1.stock + "\n");
                        System.out.print("Descripcion: " + catalogo1.description + "\n\n");
                    } // if
                } // for

            }else{ // Muestra el carrito del cliente
                System.out.print("\n---------------- Carrito ----------------\n");
                for (Producto carrito1 : carrito) {
                    if (carrito1.id >= 100) {
                        System.out.print("ID del producto: " + carrito1.id + "\nNombre del Producto: " + carrito1.name + "\n");
                        System.out.print("Precio del Producto: " + carrito1.price + "\nCantidad Disponible: " + carrito1.stock + "\n");
                        System.out.print("Descripcion: " + carrito1.description + "\n\n");
                    } // if
                } // for
            }// else
            System.out.print("----------------------------------------\n\n");
            System.out.print("Presione Enter para Continuar... \n");  
            br.readLine();
        }catch(IOException e){
        
        }// catch
    }// ver catalogo y carrito
    
    
    // Funcion para descargar el catalogo de productos desde el fichero serializado
    public static Producto[] downloadCatalogo(Socket cl, Producto[] catalogo){
        try{        
            String nameArchivos; // Variable para almacenar el nombre de los archivos entrantes
            String directorio; // Variable para almacenar la ruta absoluta de los archivos entrantes
            byte[] b = new byte[524288];
        
            DataInputStream dis = new DataInputStream(cl.getInputStream()); // Se inicializa el flujo de entrada 
            DataOutputStream dos; // Se declara el que sera el flujo de salida
            long tam = dis.readLong(); // Se lee la cantidad de archivos que enviara el cliente
            long i = 0;

            while(i < tam){ // Bucle para recibir cada uno de los archivos del cliente
                directorio = dis.readUTF(); // Se recibe la ruta absoluta del archivo entrante
                nameArchivos = dis.readUTF(); // Se recibe el nombre del archivo entrante 

                dos = new DataOutputStream(new FileOutputStream(new File(directorio))); // Se inicializa el flujo de salida 
                long recibidos = 0;
                int n = 0;
                int porcentaje;

                while(recibidos<tam){ // Bucle para la transeferencia de bytes 
                    n = dis.read(b);
                    dos.write(b,0,n);
                    dos.flush();
                    recibidos = recibidos + n;
                    porcentaje = (int)((recibidos*10)/tam);

                    System.out.print("Recibido: " + porcentaje + "%\r");
                } // Termina while
                System.out.print("\n!!! Archivo " + nameArchivos + " Recibido !!!\n");
                i= i+1;
            } // termina while
            
            // Se inicializan los flujos de entrada para deserializar el objeto
            FileInputStream fin = new FileInputStream(new File("./Servidor/archCatalogoCliente.txt")); 
            ObjectInputStream in = new ObjectInputStream(fin);
            catalogo = (Producto[]) in.readObject();
            
            return catalogo;
        }catch(IOException | ClassNotFoundException e){  
        }// catch
        return catalogo;
    }// downloadCatalogo
 
    
    // Funcion para escribir el catalogo de productos en el fichero
    public static void uploadCatalogo(Socket cl, Producto[] catalogo){
        try{
            // Se inicializa flujo de salida para el objeto que residira en un archivo de texto
            FileOutputStream fout = new FileOutputStream(new File("./Servidor/archCatalogoCliente.txt")); 
            ObjectOutputStream oos = new ObjectOutputStream(fout); 
            oos.writeObject(catalogo); // Se almacena objeto serializado en fichero 
            oos.flush();
            
             // Se inicia procedimiento para realizar el envio de ficheros
            JFileChooser jf = new JFileChooser(); // Se inicializa el selector de archivos
            jf.setDialogTitle("Seleccione el archivo \" archCatalogoCliente.txt \" para enviar al servidor ... "); // Establece el titulo de la ventana
            jf.setCurrentDirectory(new File("."));
            jf.setMultiSelectionEnabled(false); // Permite seleccion multiple de archivos
            jf.setFileSelectionMode(JFileChooser.FILES_ONLY); // Solo aceptara archivos
            System.out.printf("\nA continuacion debera seleccionar los archivos a enviar... ");
            int r = jf.showOpenDialog(null); // Muestra la ventana del selector

            if(r == JFileChooser.APPROVE_OPTION){ // Procedimiento cuando se haya confirmado los archivos a enviar
                File[] f = jf.getSelectedFiles(); // Array para almacenar los archivos seleccionados
                long tam = f.length; // Total de los Archivos a enviar
                int i = 0;
                
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); // Se inicializa el flujo de salida
                DataInputStream dis; // Se declara el que sera el flujo de entrada    
                System.out.printf("\nEspere mientras enviamos los archivos al servidor...  \n");
                dos.writeLong(tam); // Envia numero de archivos seleccionados
                dos.flush();

                for(i = 0; i < tam ; i++){ // Bucle para enviar cada uno de los archivos seleccionados
                    String pathArch = f[i].getAbsolutePath(); // Almacena la ruta absoluta del archivo a enviar
                    String nameArch = f[i].getName() ; // Almacena el nombre del archivo a enviar

                    dis = new DataInputStream(new FileInputStream(new File(pathArch))); // Inicializa el flujo de entrada
                    dos.writeUTF(pathArch); // Envia la ruta absoluta del archivo seleccionado al servidor
                    dos.flush();

                    dos.writeUTF(nameArch); // Envia el nombre del archivo seleccionado al servidor
                    dos.flush();

                    byte[] b = new byte[524288]; // 100KB
                    long enviados = 0;
                    int porcentaje;
                    int n = 0;

                    while(enviados<tam){ // Bucle para enviar bytes
                        n = dis.read(b);
                        dos.write(b,0,n);
                        dos.flush();

                        enviados = enviados + n;
                        porcentaje = (int)((enviados*10)/tam);
                        System.out.print("\nEnviado: " + porcentaje + "%\r");
                    } // Termina while
                    System.out.print("\n!!! Archivo " + nameArchivos + " enviado a servidor !!!\n");
                    //dis.close();
                } //termina for
            } // Termina if jFile
        }catch(HeadlessException | IOException e){
        }// catch
    } // writeCatalogo

}
