import java.io.*;
import java.nio.file.AccessDeniedException;
import java.net.Socket;
import java.security.AllPermission;
import java.util.Date;

class ConnectionManager extends Thread {
    private final String redirect = "public/302.html";
    private final String Secret_file = "secret/403_status.html";
    private final String SERVER_ERROR = "500_PAGE.html";
    private final String NOT_IMPLEMENTED = "public/not_implemented.html";
    private final String NOT_FOUND = "public/not_found.html";
    private final String HOME_PAGE = "public/index.html";
    private final String CLOWN_PAGE = "public/clowns.html";
    private final String PAGE_A = "public/a/index.html";
    private final String PAGE_B = "public/a/b/index.html";
    private final String PAGE_C = "public/a/b/c/index.html";
    private final String FAKE_A = "public/a/a.html";
    private final String PAGE_B_FILE = "public/a/b/b.html";
    private final String PAGE_C_FILE = "public/a/b/cc.html";
    private final String FUN_PAGE = "public/fun.html";
    private final String NAMED_PAGE = "public/named.html";
    private final String FAKE_C = "public/a/b/c.html";
    private final String FAKE_B = "public/a/b.html";
    private final String CLOWN_PNG = "public/clown.png";
    private final String WORLD_PNG = "public/world.png";
    private final String BEE_PNG = "public/a/b/bee.png";
    private BufferedReader reader;
    private PrintWriter writer;
    private BufferedOutputStream dataWriter;
    private Socket socket;

    public ConnectionManager(Socket socket, BufferedReader reader, PrintWriter writer, BufferedOutputStream dataWriter) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.dataWriter = dataWriter;
    }

    @Override
    public void run() {
        try {
            // reads first line of request from client:
            String request = reader.readLine();
            // String output = reader.readLine();
            System.out.println(request);
            if (request == null)
                return;
            // extracts method and file url
            String requestMethod = request.split(" ")[0];
            String fileRequested = request.split(" ")[1];
            boolean requestMethodIsNotGET = !requestMethod.equals("GET");
            if (requestMethodIsNotGET) {
                sendResponse(NOT_IMPLEMENTED);
            } else {

                if (fileRequested.equals("/")) {
                    sendResponse(HOME_PAGE);
                }
                if (fileRequested.equals("/clown")) {
                    sendResponse(CLOWN_PAGE);
                }
                if (fileRequested.equals("/clown/")) {
                    sendResponse(CLOWN_PAGE);
                }
                if (fileRequested.equals("/a/")) {
                    sendResponse(PAGE_A);
                }
                if (fileRequested.equals("/a")) {
                    sendResponse(PAGE_A);
                }
                if (fileRequested.equals("/a/a")) {
                    sendResponse(FAKE_A);
                }
                if (fileRequested.equals("/a/b/")) {
                    sendResponse(PAGE_B);
                }
                if (fileRequested.equals("/a/b")) {
                    sendResponse(PAGE_B);
                }
                if (fileRequested.equals("/a/b/")) {
                    sendResponse(PAGE_B);
                }
                if (fileRequested.equals("/a/b/c")) {
                    sendResponse(PAGE_C);
                }
                if (fileRequested.equals("/a/a.html")) {
                    sendResponse(FAKE_A);
                }
                if (fileRequested.equals("/a/b.html")) {
                    sendResponse(FAKE_B);
                }
                if (fileRequested.equals("/a/b/c.html")) {
                    sendResponse(FAKE_C);
                }
                if (fileRequested.equals("/a/b/b.html")) {
                    sendResponse(PAGE_B_FILE);
                }
                if (fileRequested.equals("/a/b/c/c.html")) {
                    sendResponse(PAGE_C_FILE);
                }
                if (fileRequested.equals("/fun")) {
                    sendResponse(FUN_PAGE);
                }
                if (fileRequested.equals("/fun.html")) {
                    sendResponse(FUN_PAGE);
                }
                if (fileRequested.equals("/fun/")) {
                    sendResponse(FUN_PAGE);
                }
                if (fileRequested.equals("/named")) {
                    sendResponse(NAMED_PAGE);
                }
                if (fileRequested.equals("/named.html")) {
                    sendResponse(NAMED_PAGE);
                }
                if (fileRequested.equals("/named/")) {
                    sendResponse(NAMED_PAGE);
                }
                if (fileRequested.equals("/clown.png")) {
                    sendImageResponse(CLOWN_PNG);
                }
                if (fileRequested.equals("/clown.png/")) {
                    sendImageResponse(CLOWN_PNG);
                }
                if (fileRequested.equals("/world.png")) {
                    sendImageResponse(WORLD_PNG);
                }
                if (fileRequested.equals("/world.png/")) {
                    sendImageResponse(WORLD_PNG);
                }
                if (fileRequested.equals("/bee.png")) {
                    sendImageResponse(BEE_PNG);
                }
                if (fileRequested.equals("/bee.png/")) {
                    sendImageResponse(BEE_PNG);
                }
                if (fileRequested.equals("/a/b/bee.png")) {
                    sendImageResponse(BEE_PNG);
                }
                if (fileRequested.equals("/secret")){
                    sendResponse(Secret_file);
                }
                if (fileRequested.equals("/secret")){
                    sendResponse(Secret_file);
                }
                if (fileRequested.equals("/c")){
                    sendResponse(redirect);
                }
                else {
                    sendResponse(NOT_FOUND);
                }
            }
            /*else {
                sendResponse(SERVER_ERROR);
            }*/
        } catch(IOException ex) {
            //System.err.println(ex.getMessage());
            //ex.printStackTrace();

            sendResponse(SERVER_ERROR);
        }
        try {
            // closing resources
            this.reader.close();
            this.writer.close();
            this.dataWriter.close();

        } catch (IOException e) {
            System.err.println("An error occurred during closing IO sockets");
            //sendResponse(SERVER_ERROR);
        }
        System.out.println("Connection closed: " + socket);
    }
    private void sendResponse(String fileName) {
        // reads body file
        File file = new File(fileName);
        int fileLength = (int) file.length();
        byte[] fileData = readFileData(file, fileLength);
        sendHeader(fileName, fileLength);
        sendBody(fileData, fileLength);
    }

    private void sendBody(byte[] fileData, int fileLength) {
        try {
            dataWriter.write(fileData, 0, fileLength);
            dataWriter.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendHeader(String fileName, int fileLength) {
        if (fileName.equals(NOT_FOUND)) {
            writer.println("HTTP/1.1 404 File Not Found");
        }
        /*if (fileName.equals())
        //if (file.equals)*/
        
        if (fileName.equals("SERVER_ERROR")){
            writer.println("HTTP/1.1 500 internal server error");
        }
         
        else {
            writer.println("HTTP/1.1 200 OK");
        }
        writer.println("Date: " + new Date());
        writer.println("content-type: text/html");
        writer.println("content-length: " + fileLength);
        // blank line between headers and content
        writer.println();
        // flush character output stream buffer
        writer.flush();
    }
    private byte[] readFileData(File file, int fileLength) {
        byte[] fileData = new byte[fileLength];
        try {
            try (FileInputStream inputFile = new FileInputStream(file)) {
                inputFile.read(fileData);
            }
        } catch (Exception e) {
            System.err.println("File reading error: " + file.getName());
            System.err.println(e.getMessage());
            //e = e.getMessage();
            //if (e.equals(Permisio))
            //if (e.getMessage().e);
            /*writer.println("HTTP/1.1 500 Internal Server Error");
            writer.println("Date: " + new Date());
            writer.println("content-type: text/html");*/
            System.err.println(file.getAbsolutePath());
        }
        return fileData;
    }

    private void sendImageResponse(String requestFile) {
        try {
            String content = getContentType(requestFile);
            File imageFile = new File(requestFile);
            // reads data of requested file;
            FileInputStream fis = new FileInputStream(imageFile);
            byte[] data = new byte[(int) imageFile.length()];
            fis.read(data);
            //fis.close();
            DataOutputStream binaryOutput = new DataOutputStream(socket.getOutputStream());
            writer.println("HTTP/1.1 200 Ok");
            writer.println("Date: " + new Date());
            writer.println("content-type: " + content);
            writer.println("content-length: " + data.length);
            // a blank line between headers and content 
            writer.println();
            writer.flush();
            binaryOutput.write(data);
            binaryOutput.flush();
            binaryOutput.close();
        } catch (Exception e){
            System.err.println("error");
            System.err.println(e.getMessage());
        }
    }

    private String getContentType(String fileRequested) {
        //String contentMimeType = "text/html"
		if (fileRequested.endsWith(".png") || fileRequested.endsWith("png"))
			return "image/png";
		else
			return "image/png";
	}
    //private void sendErrorResponse()
}