
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main extends Application {
    public static WebEngine webEngine;
    public static WebView browser;
    public static int load_count=0;
    public static void main(String[] args) {
        System.setProperty("javafx.macosx.embedded", "true");
        java.awt.Toolkit.getDefaultToolkit();
        Application.launch(args);
    }

    public static String readcode(String file){
        try {
            FileInputStream fin=new FileInputStream("./"+file);
            byte[] bys=new byte[fin.available()];
            fin.read(bys);
            fin.close();
            return new String(bys,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void start(Stage primaryStage) {
        browser = new WebView();
        
        webEngine = browser.getEngine();

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }
        // Now you can access an https URL without having the certificate in the truststore
        /*try {
            URL url = new URL("https://hostname/index.html");
        } catch (MalformedURLException e) {
        }*/

        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
            if (newExc != null) {
                newExc.printStackTrace();
            }
        });
        webEngine.getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == State.SUCCEEDED) {
                        load_count++;
                        System.out.println("finished loading");
                        String html = (String) webEngine.executeScript("document.documentElement.outerHTML");

                        /*String data = (String) webEngine.executeScript("GetCookie(\"loginoa\")");
                        System.out.println(data);*/

                        if(html.contains("用户登录-中南大学——统一身份认证")) {
                            webEngine.executeScript(readcode("login.txt"));
                        }
                        //webEngine.executeScript(JSInterface.readcode("code_getjson.txt"));

                        if(load_count==3)
                            webEngine.load("https://wxxy.csu.edu.cn/ncov/wap/default/index");

                        if(load_count==4){
                            webEngine.executeScript(readcode("auto_fill.txt"));
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            webEngine.executeScript("vm.save();");
                                            System.out.println(webEngine.executeScript("vm.hasFlag").toString());
                                            System.exit(0);
                                        }
                                    });
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //System.exit(0);
                                }
                            }.start();
                        }
                    }
                    System.out.println(""+newValue);
                });

        // begin loading...
        webEngine.load("https://wxxy.csu.edu.cn/ncov/wap/default/index");



        /*Group root = new Group();
        Scene scene = new Scene(root, 1200, 800);
        root.getChildren().add(browser);

        primaryStage.setScene(scene);
        primaryStage.show();*/
    }
}

/*public class Main {
    public static void main(String[] args) {
        WebEngine webEngine = new WebEngine();
        webEngine.getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == State.SUCCEEDED) {
                        System.out.println("finished loading");
                    }
                }); // addListener()

        // begin loading...
        webEngine.load("https://wxxy.csu.edu.cn/ncov/wap/default/index");
        JSInterface jsi=new JSInterface();
        JSObject win=(JSObject) webEngine.executeScript("window");
        win.setMember("java",jsi);
        webEngine.executeScript(JSInterface.readcode("code_getjson.txt"));
        webEngine.executeScript("savejson()");

        //webEngine.executeScript("");
    }

    /*@Override
    public void start(Stage primaryStage) {
        WebEngine webEngine = new WebEngine();
        webEngine.getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == State.SUCCEEDED) {
                        System.out.println("finished loading");
                    }
                }); // addListener()

        // begin loading...
        webEngine.load("https://wxxy.csu.edu.cn/ncov/wap/default/index");


        Group root = new Group();
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}*/