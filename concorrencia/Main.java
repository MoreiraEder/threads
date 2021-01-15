import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String args[]) {

        Properties sharedProperties = new Properties();

        Thread threadB = new Thread(
            new Runnable(){            
                public void run() {      
                    System.out.println("Thread B executando.");              
                    try {
                        synchronized(sharedProperties) {
                            Thread.sleep(5000);

                            synchronized(this) {
                                notify(); // Deve acordar a thread A
                            }
    
                            Properties localProperties = new Properties();
                            String currDir = System.getProperty("user.dir");
                            File file = new File("\\\\"+ currDir +"\\file.properties");
                            InputStream inputStream = new FileInputStream(file);
                            localProperties.load(inputStream);
                            sharedProperties.putAll(localProperties);
                            System.out.println("sharedProperties foi atualizado.");
                        }                        
                    }
                    catch(IOException | InterruptedException e) {
                        e.printStackTrace();
                    }                    
                }
            },
            "Thread B"
        );

        Thread threadA = new Thread(
            new Runnable(){                
                public void run() {    
                    System.out.println("Thread A executando.");                
                    try {                        
                        
                        synchronized(threadB) {
                            System.out.println("Thread A no sleep de 5 seg.");
                            Thread.sleep(5000);
                            System.out.println("Thread A em wait.");
                            threadB.wait();
                            System.out.println("Thread A saiu do wait.");
                        }
                        
                        synchronized(sharedProperties) {                            
                            System.out.println("Conteúdo do sharedProperties: " + sharedProperties.toString());
                            synchronized(this) {
                                this.notify();
                            }                  
                        }
                    }
                    catch (InterruptedException e) {
                        System.out.println("Falha ao executar a thread A.");
                    }       
                }
            },
            "Thread A"
        );

        Thread threadC = new Thread(
            new Runnable(){              
                public void run() {                    
                    System.out.println("Thread C executando.");
                    try {
                        synchronized(threadA) {
                            System.out.println("Thread C esperando thread A.");
                            threadA.wait();
                            System.exit(0);
                        }
                    } catch (InterruptedException interruptedException) {
                        System.out.println("Falha ao executar thread C.");
                    }
                }
            },
            "Thread C"
        );
        
        threadA.start();          
        threadB.start();
        threadC.start();
        System.out.println("Esperando as threads.");        
    }
}