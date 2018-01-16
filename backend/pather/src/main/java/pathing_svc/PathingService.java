package pathing_svc;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PathingService {
    public String getPath() {
        String fileName = "test.out";
        StringBuilder result = new StringBuilder();
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("./" + fileName);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                result.append(line);
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return result.toString();
    }
}
