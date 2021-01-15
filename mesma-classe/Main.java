public class Main {
    public static void main(String[] args) {

        Thread myThread = new Thread(
            new Runnable(){
                public void run() {
                    System.out.println("I am running!");
                }
            }
        );

        myThread.start();
    }    
}